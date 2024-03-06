plugins {
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.2.0"
    kotlin("jvm") version "1.8.20"
}

group = "ru.aedilay"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.springdoc.openapi-gradle-plugin:org.springdoc.openapi-gradle-plugin.gradle.plugin:1.8.0")
    implementation("org.openapitools:openapi-generator-gradle-plugin:7.2.0")
}

gradlePlugin {
    website = "https://github.com/Aedilay/client-generator-gradle-plugin"
    vcsUrl = "https://github.com/Aedilay/client-generator-gradle-plugin.git"
    plugins {
        create("ClientGenerator") {
            id = "ru.aedilay.client-generator"
            version = "1.0.0"
            displayName = "spring client generator"
            description =
                "Learning project for geting in touch with creating custom gradle plugins. This plugin is for generating feign client for service controllers"
            tags = listOf("demo")
            implementationClass = "ru.aedilay.ClientGeneratorPlugin"
        }
    }
}

val jvmVersion: JavaLanguageVersion = JavaLanguageVersion.of(17)

java {
    toolchain.languageVersion.set(jvmVersion)
    withSourcesJar()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "$jvmVersion"
    }
}
