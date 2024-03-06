package ru.ventra

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.GradleBuild
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.getByType
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import org.springdoc.openapi.gradle.plugin.OpenApiExtension

class ClientGeneratorPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            plugins.apply(SPRINGDOC_PLUGIN)
            extensions.create(EXTENSION_NAME, ClientGeneratorExtension::class.java)
            afterEvaluate {
                generateClient(this)
            }
        }
    }

    fun generateClient(project: Project) {
        val extension: ClientGeneratorExtension =
            project.extensions.getByName(EXTENSION_NAME) as ClientGeneratorExtension

        if (!STAND_BRANCHES.contains(System.getenv("BRANCH_NAME")) && !extension.force.get()) {
            return
        }

        val specDirectory = project.layout.buildDirectory.dir("docs")
        val generatedClientDirectory = project.layout.buildDirectory.dir("generated")
        val branchName = System.getenv("BRANCH_NAME") ?: DEFAULT_BRANCH
        val generatedClientName = "${extension.clientJarPrefix.get()}-client-$branchName.jar"
        val newClientUrlBlock = "url: http://${extension.clientDefaultUri.get()}:8080"

        val openApiExtension = project.extensions.getByType<OpenApiExtension>()

        with(openApiExtension) {
            apiDocsUrl.set(DEFAULT_OPENAPI_SPEC_URL)
            outputDir.set(specDirectory)
            outputFileName.set(SPEC_FILE_NAME)
            customBootRun {
                args.set(mutableListOf("--spring.profiles.active=build-client", "--logging.level.root=OFF"))
            }
        }

        val buildClient = project.tasks.register("jarClient", GradleBuild::class.java) {
            setDir(generatedClientDirectory.get().toString())
            tasks = mutableListOf("clean", "jar")
        }

        val copyClientToMainBuild = project.tasks.register("copyClientToMainBuild", Copy::class.java) {
            dependsOn(buildClient)
            from("${generatedClientDirectory.get()}/build/libs") {
                include(DEFAULT_GENERATOR_JAR)
            }
            into("${project.layout.buildDirectory.get()}/libs")
            rename(DEFAULT_GENERATOR_JAR, generatedClientName)
        }

        val generateClient = project.tasks.register("generateClient", GenerateTask::class.java) {
            doFirst {
                val file = specDirectory.get().file(SPEC_FILE_NAME).asFile
                val text = file.readText()
                val modifiedText = text.replace(
                    DEFAULT_OPENAPI_URL_BOCK,
                    newClientUrlBlock
                )
                file.writeText(modifiedText)
            }
            inputSpec.set(specDirectory.get().file(SPEC_FILE_NAME).asFile.absolutePath)
            outputDir.set(generatedClientDirectory.get().asFile.absolutePath)
            packageName.set(extension.packageName.get())
            modelPackage.set("${extension.packageName.get()}.model")
            apiPackage.set("${extension.packageName.get()}.api")
            packageName.set(extension.packageName.get())
            generatorName.set(GENERATOR_TYPE)
            configOptions.set(
                mapOf(
                    "interfaceOnly" to "true",
                    "library" to "spring-cloud",
                    "serializableModel" to "true",
                    "serializationLibrary" to "jackson",
                    "enumPropertyNaming" to "UPPERCASE",
                    "beanQualifiers" to "true",
                    "useTags" to "true"
                )
            )
            finalizedBy(buildClient, copyClientToMainBuild)
        }

        project.tasks.named("build") {
            finalizedBy("generateOpenApiDocs")
        }

        project.tasks.named("generateOpenApiDocs") {
            finalizedBy(generateClient)
        }

        project.tasks.named("forkedSpringBootRun") {
            dependsOn(
                project.tasks.named("jar"),
                project.tasks.named("bootJar"),
                project.tasks.named("test")
            )
        }
    }
}
