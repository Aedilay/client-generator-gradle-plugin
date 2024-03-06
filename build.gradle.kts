import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    id("org.springdoc.openapi-gradle-plugin") version "1.8.0"
    id("org.openapi.generator") version "7.2.0"
    id("com.gradle.plugin-publish") version "1.2.1"
}

group = "ru.ventra"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.springdoc.openapi-gradle-plugin:org.springdoc.openapi-gradle-plugin.gradle.plugin:1.8.0")
    implementation("org.openapitools:openapi-generator-gradle-plugin:7.2.0")
}

gradlePlugin {
    plugins {
        create("generate-client") {
            id = "client-generator"
            version = "1.0.2"
            implementationClass = "ru.ventra.ClientGeneratorPlugin"
        }
    }
}

project.tasks.withType<BootJar> {
    enabled = false
}
