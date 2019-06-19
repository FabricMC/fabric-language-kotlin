import net.fabricmc.loom.task.RemapJarTask

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

configurations.modCompile.extendsFrom(configurations.include)
//configurations.compileOnly.extendsFrom(configurations.include)
//configurations.compileOnly.extendsFrom(configurations.modCompile)

minecraft {

}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = Minecraft.version)
    mappings(group = "net.fabricmc", name = "yarn", version = "${Minecraft.version}+build.${Fabric.Yarn.version}")

    modCompile(group = "net.fabricmc", name = "fabric-loader", version = Fabric.Loader.version)
//    modCompile(group = "net.fabricmc", name = "fabric", version = Fabric.API.version + "+build.+")
//    include(group = "net.fabricmc", name = "fabric-api", version = Fabric.API.version + "+build.+")
    include(group = "net.fabricmc.fabric-api", name = "fabric-resource-loader-v0", version = "0.1.1" + "+")

    modCompile(group = "net.fabricmc", name = "fabric-language-kotlin", version = "${Jetbrains.Kotlin.version}+local")
//    modCompile(project(":"))

    include(group = "io.github.prospector.modmenu", name = "ModMenu", version = "+") { 
        
    }
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