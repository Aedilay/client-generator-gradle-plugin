import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    id("org.springdoc.openapi-gradle-plugin") version "1.8.0"
    id("org.openapi.generator") version "7.2.0"
    id("com.gradle.plugin-publish") version "1.2.1"
//    id("com.github.johnrengelman.shadow") version "6.1.0"
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
    website = "https://github.com/Aedilay/client-generator-gradle-plugin"
    vcsUrl = "https://github.com/Aedilay/client-generator-gradle-plugin.git"
    plugins {
        create("generate-client") {
            id = "ru.aedilay.client-generator"
            version = "1.0.0"
            displayName = "spring client generator"
            description = "plugin for generating feign client for service controllers"
            tags = listOf("demo")
            implementationClass = "ru.aedilay.ClientGeneratorPlugin"
        }
    }
}

project.tasks.withType<BootJar> {
    enabled = false
}
