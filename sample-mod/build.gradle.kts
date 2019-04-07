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
    archivesBaseName = Constants.modid
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
    mavenCentral()
    jcenter()
}

//val provided = configurations.create("provided")
//sourceSets {
//    getByName("main") {
//        compileClasspath += provided
//    }
//}
//configurations.implementation.get().extendsFrom(configurations.modCompileMapped.get())

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = Minecraft.version)

    mappings(group = "net.fabricmc", name = "yarn", version = "${Minecraft.version}.${Fabric.Yarn.version}")

    modCompile(group = "net.fabricmc", name = "fabric-loader", version = Fabric.Loader.version)
    modCompile(group = "net.fabricmc", name = "fabric", version = Fabric.API.version + ".+")
    include(group = "net.fabricmc", name = "fabric", version = Fabric.API.version + ".+")

    modCompile(rootProject)
    compileOnly(rootProject)

    modCompile(group = "io.github.prospector.modmenu", name = "ModMenu", version = "+")
    
//    api(group = "net.fabricmc", name = "fabric-language-kotlin", version = Fabric.LanguageKotlin.version)

    // required until modCompile also adds to compileOnly
//    compileOnly(group = "net.fabricmc", name = "fabric-language-kotlin", version = Fabric.LanguageKotlin.version) {
//        isTransitive = true
//    }
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