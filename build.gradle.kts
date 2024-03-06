import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    `java-gradle-plugin`
    `maven-publish`
    id("org.springdoc.openapi-gradle-plugin") version "1.8.0"
    id("org.openapi.generator") version "7.2.0"
    id("com.gradle.plugin-publish") version "1.2.1"
//    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "ru.aedilay"

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
        create("client-generator") {
            id = "ru.aedilay.client-generator"
            version = "1.0.0"
            displayName = "spring client generator"
            description = "Learning project for geting in touch with creating custom gradle plugins. This plugin is for generating feign client for service controllers"
            tags = listOf("demo")
            implementationClass = "ru.aedilay.ClientGeneratorPlugin"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("myLibrary") {
            from(components["java"])
            groupId = "ru.aedilay"
            artifactId = "client-generator"
            version = "1.0.0"
        }
    }
}

project.tasks.withType<BootJar> {
    enabled = false
}
