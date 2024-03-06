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
    val packageName: Property<String> = objects.property(String::class.java)
    val force: Property<Boolean> = objects.property(Boolean::class.java)

    init {
        clientDefaultUri.convention(project.name)
        clientJarPrefix.convention(project.name)
        packageName.convention(project.name.replace('-', '.'))
        force.convention(false)
    }
}

