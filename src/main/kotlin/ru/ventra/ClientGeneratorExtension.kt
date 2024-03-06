package ru.ventra

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

open class ClientGeneratorExtension @Inject constructor(
    objects: ObjectFactory,
    project: Project,
) {
    val clientDefaultUri: Property<String> = objects.property(String::class.java)
    val clientJarPrefix: Property<String> = objects.property(String::class.java)

    init {
        clientDefaultUri.convention(project.name)
        clientJarPrefix.convention(project.name)
    }
}

