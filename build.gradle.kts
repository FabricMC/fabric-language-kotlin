import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJar
import net.fabricmc.loom.task.RunClientTask
import net.fabricmc.loom.task.RunServerTask
import net.fabricmc.loom.util.ModRemapper

plugins {
    kotlin("jvm") version Kotlin.version
    idea
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "4.0.3"
    id("net.minecrell.licenser") version "0.4.1"
    id("fabric-loom") version Fabric.Loom.version
}

base {
    archivesBaseName = Constants.modid
}

val buildNumber = System.getenv("BUILD_NUMBER") ?: "local"

group = Constants.group
description = Constants.description
version = Constants.modVersion + "-" + buildNumber

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

minecraft {

}

tasks.getByName<ProcessResources>("processResources") {
    filesMatching("mod.json") {
        expand(
            mutableMapOf(
                "version" to version
            )
        )
    }
}

license {
    header = file("HEADER")
    include("**/*.java")
    include("**/*.kt")
}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = Minecraft.version)

    mappings(group = "net.fabricmc", name = "yarn", version = "${Minecraft.version}.${Fabric.Yarn.version}")

    modCompile(group = "net.fabricmc", name = "fabric-loader", version = Fabric.version) { isTransitive = false }

    shadow(Kotlin.stdLib)
    shadow(Kotlin.reflect)
    shadow(KotlinX.Coroutines.dependency)

    // required for yarn to find for test client
    implementation(Kotlin.stdLib)
    implementation(Kotlin.reflect)
    implementation(KotlinX.Coroutines.dependency)
}

val shadowJar by tasks.getting(ShadowJar::class) {
    classifier = ""
    configurations = listOf(
        project.configurations.shadow.get()
    )
    exclude("META-INF")
}

val build = tasks.getByName<Task>("build") {
    dependsOn(shadowJar)
}

val remapJar = tasks.getByName<RemapJar>("remapJar") {
    (this as Task).dependsOn(shadowJar)
    jar = shadowJar.archivePath
}

val testJar = tasks.create<Jar>("testJar") {
    classifier = "tests"
    from(sourceSets.maybeCreate("test").output)
}

val remapTestJar = (tasks.create("remapTestJar", RemapJar::class) as RemapJar).apply {
    (this as Task).dependsOn(testJar)
    jar = testJar.archivePath
}

tasks.getByName("runClient") {
    dependsOn(remapTestJar)
}

tasks.getByName("runServer") {
    dependsOn(remapTestJar)
}

fun shadowComponents(publication: MavenPublication, vararg configurations: Configuration) {
    publication.artifact(remapJar.jar) {
        builtBy(remapJar)
    }
    publication.pom.withXml {
        val dependenciesNode = asNode().appendNode("dependencies")

        project.configurations.shadow.allDependencies.forEach {
            if (it !is SelfResolvingDependency) {
                val dependencyNode = dependenciesNode.appendNode("dependency")
                dependencyNode.appendNode("groupId", it.group)
                dependencyNode.appendNode("artifactId", it.name)
                dependencyNode.appendNode("version", it.version)
                dependencyNode.appendNode("scope", "runtime")
            }
        }
        configurations.forEach { configuration ->
            println("processing: $configuration")
            configuration.dependencies.forEach { dependency ->
                if (dependency !is SelfResolvingDependency) {
                    val dependencyNode = dependenciesNode.appendNode("dependency")
                    dependencyNode.appendNode("groupId", dependency.group)
                    dependencyNode.appendNode("artifactId", dependency.name)
                    dependencyNode.appendNode("version", dependency.version)
                    dependencyNode.appendNode("scope", configuration.name)
                }
            }
        }
    }
}

publishing {
    publications {
        create("default", MavenPublication::class.java) {
            groupId = project.group.toString()
            artifactId = project.name.toLowerCase()
            version = project.version.toString()

            shadowComponents(this, configurations.modCompile.get())
        }
    }
    repositories {
        maven(url = "http://mavenupload.modmuss50.me/") {
            val mavenPass: String? = project.properties["mavenPass"] as String?
            mavenPass?.let {
                credentials {
                    username = "buildslave"
                    password = mavenPass
                }
            }
        }
    }
}