import java.util.Properties

plugins {
    kotlin("jvm") version "1.4.21"
    id("fabric-loom") version "0.5.12"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// load props from parent project
val parentProps = rootDir.parentFile.resolve("gradle.properties").bufferedReader().use {
    Properties().apply {
        load(it)
    }
}

val modId: String by parentProps
val modVersion: String by parentProps
val group: String by parentProps
val minecraftVersion: String by parentProps

base {
    archivesBaseName = modId + "-test"
}


project.group = group
version = modVersion


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

//val versionProps = rootDir.resolve("versions.properties").bufferedReader().use {
//    Properties().apply {
//        load(it)
//    }
//}
//// load version of api and pick the minecraft part
//val minecraftVersion = versionProps["version.net.fabricmc.fabric-api..fabric-api"].toString().substringAfterLast('-')

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = minecraftVersion)
    mappings(group = "net.fabricmc", name = "yarn", version = minecraftVersion+"+build.1", classifier = "v2")

    modImplementation("net.fabricmc:fabric-loader:0.10.8")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.28.0+1.16")

    modImplementation("$group:$modId:$modVersion+local")

//    modImplementation("io.github.prospector.modmenu:ModMenu:_")
}

//val publishToMavenLocal = rootProject.tasks.getByName<Task>("publishToMavenLocal")

//val remapJar = tasks.getByName<RemapJarTask>("remapJar") {
//    (this as Task).dependsOn(publishToMavenLocal)
//}

val fabricApiVersion = ""
val kotlinVersion = ""

tasks.getByName<ProcessResources>("processResources") {
    filesMatching("fabric.mod.json") {
        expand(
            mutableMapOf(
                "modid" to modId,
                "version" to modVersion,
                "kotlinVersion" to kotlinVersion,
                "fabricApiVersion" to fabricApiVersion
            )
        )
    }
}