import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.matthewprenger.cursegradle.CurseArtifact
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseUploadTask
import com.matthewprenger.cursegradle.Options
import moe.nikky.counter.CounterExtension

plugins {
    kotlin("jvm") version Jetbrains.Kotlin.version
    idea
    `maven-publish`
    id("moe.nikky.persistentCounter") version "0.0.7-SNAPSHOT"
    id("com.github.johnrengelman.shadow") version "4.0.4"
    id("net.minecrell.licenser") version "0.4.1"
    id("com.matthewprenger.cursegradle") version "1.1.2"
    id("fabric-loom") version Fabric.Loom.version apply false
}

base {
    archivesBaseName = Constants.modid
}

counter {
    variable(id = "buildNumber", key = Constants.modVersion) {
        default = 0
    }
}
val counter: CounterExtension = extensions.getByType()

val buildNumber by counter.map
val actualBuildNumber = buildNumber + 1

group = Constants.group
description = Constants.description
version = "${Constants.modVersion}-$actualBuildNumber"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.getByName<ProcessResources>("processResources") {
    filesMatching("fabric.mod.json") {
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

configurations.api.extendsFrom(configurations.shadow)

repositories {
    maven(url = "http://maven.fabricmc.net") {
        name = "Fabric"
    }
    maven(url = "https://libraries.minecraft.net/") {
        name = "Mojang"
    }
    maven(url = "https://kotlin.bintray.com/kotlinx/") {
        name = "KotlinX"
    }
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(group = "net.fabricmc", name = "fabric-loader", version = Fabric.Loader.version)

    shadow(Jetbrains.Kotlin.stdLibJkd8)
    shadow(Jetbrains.Kotlin.reflect)
    shadow(Jetbrains.annotations)
    shadow(Jetbrains.KotlinX.coroutinesCore)
    shadow(Jetbrains.KotlinX.coroutinesJdk8)
}

val shadowJar by tasks.getting(ShadowJar::class) {
    classifier = ""
    configurations = listOf(
        project.configurations.shadow
    )
    exclude("META-INF")
}

fun MavenPublication.shadowComponents() {
    pom.withXml {
        val dependenciesNode = asNode().appendNode("dependencies")

        project.configurations.shadow.allDependencies.forEach {
            if (it !is SelfResolvingDependency) {
                val dependencyNode = dependenciesNode.appendNode("dependency")
                dependencyNode.appendNode("groupId", it.group)
                dependencyNode.appendNode("artifactId", it.name)
                dependencyNode.appendNode("version", it.version)
                dependencyNode.appendNode("scope", "compileOnly")
            }
        }
    }
}

val sourcesJar = tasks.create<Jar>("sourcesJar") {
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

val javadoc = tasks.getByName<Javadoc>("javadoc") {}
val javadocJar = tasks.create<Jar>("javadocJar") {
    classifier = "javadoc"
    from(javadoc)
}

publishing {
    publications {
        create("shadow", MavenPublication::class.java) {

            groupId = project.group.toString()
            artifactId = project.name.toLowerCase()
            version = project.version.toString()

            artifact(shadowJar)
            artifact(sourcesJar)
            artifact(javadocJar)

            shadowComponents()
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
val curse_api_key: String? by project
if (curse_api_key != null && project.hasProperty("release")) {
    val CURSEFORGE_RELEASE_TYPE: String by project
    val CURSEFORGE_ID: String by project
    curseforge {
        options(closureOf<Options> {
            forgeGradleIntegration = false
        })
        apiKey = curse_api_key
        project(closureOf<CurseProject> {
            id = CURSEFORGE_ID
            releaseType = CURSEFORGE_RELEASE_TYPE
            addGameVersion("1.14-Snapshot")

            val changelog_file: String? by project
            if (changelog_file != null) {
                println("changelog = $changelog_file")
                changelogType = "markdown"
                changelog = file(changelog_file as String)
            }
            mainArtifact(shadowJar.archivePath, closureOf<CurseArtifact> {
                displayName = "Fabric Language Kotlin $version"
            })
        })
    }
    project.afterEvaluate {
        tasks.getByName<CurseUploadTask>("curseforge${CURSEFORGE_ID}") {
            dependsOn(shadowJar)
        }
    }
}