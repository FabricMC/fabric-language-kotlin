import org.gradle.api.internal.AbstractTask

plugins {
    kotlin("jvm")// version Jetbrains.Kotlin.version
    idea
    id("fabric-loom")// version Fabric.Loom.version
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

minecraft { }

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

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = Minecraft.version)

    mappings(group = "net.fabricmc", name = "yarn", version = "${Minecraft.version}+build.${Fabric.Yarn.version}")

    modCompile(group = "net.fabricmc", name = "fabric-loader", version = Fabric.Loader.version)
    modCompile(group = "net.fabricmc", name = "fabric", version = Fabric.API.version + ".+")
    include(group = "net.fabricmc", name = "fabric", version = Fabric.API.version + ".+")

    modCompile(rootProject)
    compileOnly(rootProject)

    modCompile(group = "io.github.prospector.modmenu", name = "ModMenu", version = "+")
}

val shadowJar = rootProject.tasks.getByName<AbstractTask>("shadowJar")
shadowJar.execute()

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