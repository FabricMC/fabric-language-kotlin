import net.fabricmc.loom.task.RemapJarTask

plugins {
    kotlin("jvm")
    id("fabric-loom")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

base {
    archivesBaseName = Constants.modid + "-test"
}

group = Constants.group
version = Constants.modVersion


repositories {
    maven(url = "http://maven.fabricmc.net/") {
        name = "Fabric"
    }
    maven(url = "https://kotlin.bintray.com/kotlinx") {
        name = "Kotlinx"
    }
    mavenLocal()
    mavenCentral()
    jcenter()
}

minecraft {

}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = Minecraft.version)
    mappings(group = "net.fabricmc", name = "yarn", version = Fabric.YarnMappings.version)

    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = Fabric.Loader.version)
    modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = Fabric.API.version)

    modImplementation(project(":"))

    modImplementation(group = "io.github.prospector.modmenu", name = "ModMenu", version = "+")
}

val publishToMavenLocal = rootProject.tasks.getByName<Task>("publishToMavenLocal")

val remapJar = tasks.getByName<RemapJarTask>("remapJar") {
    (this as Task).dependsOn(publishToMavenLocal)
}

tasks.getByName<ProcessResources>("processResources") {
    filesMatching("fabric.mod.json") {
        expand(
            mutableMapOf(
                "modid" to Constants.modid,
                "version" to Constants.modVersion,
                "kotlinVersion" to Jetbrains.Kotlin.version,
                "fabricApiVersion" to Fabric.API.version
            )
        )
    }
}