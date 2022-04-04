import com.matthewprenger.cursegradle.CurseArtifact
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseUploadTask
import com.matthewprenger.cursegradle.Options
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask

plugins {
    kotlin("jvm") version "1.6.20"
    id("org.cadixdev.licenser") version "0.6.1"
    id("com.matthewprenger.cursegradle") version "1.4.0"
    id("fabric-loom") version "0.10-SNAPSHOT"
    `maven-publish`
}

val modId: String by project
val modVersion: String by project
val group: String by project
val description: String by project
val minecraftVersion: String by project
val loaderVersion: String by project
val kotlinVersion: String by project
val coroutinesVersion: String by project
val serializationVersion: String by project

base {
    archivesBaseName = modId
}

val ciBuild: Boolean = System.getenv("CI") != null

project.group = group
project.description = description
version = "${modVersion}+kotlin.${kotlinVersion}"

if (!ciBuild) {
    version = version as String + ".local"
}

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
    header.set(project.resources.text.fromFile(file("HEADER")))
    include("**/*.java")
    include("**/*.kt")
}

fun DependencyHandlerScope.includeAndExpose(dep: Any) {
    modApi(dep)
    include(dep)
}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = minecraftVersion)
    mappings(group = "net.fabricmc", name = "yarn", version = "$minecraftVersion+build.5", classifier = "v2")

    modImplementation("net.fabricmc:fabric-loader:${loaderVersion}")

    includeAndExpose(kotlin("stdlib", kotlinVersion))
    includeAndExpose(kotlin("stdlib-jdk8", kotlinVersion))
    includeAndExpose(kotlin("stdlib-jdk7", kotlinVersion))
    includeAndExpose(kotlin("reflect", kotlinVersion))
    includeAndExpose("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    includeAndExpose("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$coroutinesVersion")
    includeAndExpose("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion")
    includeAndExpose("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:$serializationVersion")
    includeAndExpose("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:$serializationVersion")
    includeAndExpose("org.jetbrains.kotlinx:kotlinx-serialization-cbor-jvm:$serializationVersion")
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create("main", MavenPublication::class.java) {

            groupId = project.group.toString()
            artifactId = project.name.toLowerCase()
            version = project.version.toString()

            from(components["java"])
        }
    }

    val maven_url: String? = System.getenv("MAVEN_URL")

    if (maven_url != null) {
        repositories {
            maven(url = maven_url) {
                val mavenPass: String? = System.getenv("MAVEN_PASSWORD")
                mavenPass?.let {
                    credentials {
                        username = System.getenv("MAVEN_USERNAME")
                        password = mavenPass
                    }
                }
            }
        }
    }
}
val curse_api_key: String? = System.getenv("CURSEFORGE_API_KEY")
if (curse_api_key != null && project.hasProperty("release")) {
    val CURSEFORGE_ID: String by project
    curseforge {
        options(closureOf<Options> {
            forgeGradleIntegration = false
        })
        apiKey = curse_api_key
        project(closureOf<CurseProject> {
            id = CURSEFORGE_ID
            releaseType = "beta"
            addGameVersion("1.14")
            addGameVersion("1.14.1")
            addGameVersion("1.14.2")
            addGameVersion("1.14.3")
            addGameVersion("1.14.4")
            addGameVersion("1.15")
            addGameVersion("1.16")
            addGameVersion("1.16.1")
            addGameVersion("1.16.2")
            addGameVersion("1.16.3")
            addGameVersion("1.16.4")
            addGameVersion("1.17")
            addGameVersion("1.17.1")
            addGameVersion("1.18")
            addGameVersion("1.18.1")
            addGameVersion("Fabric")

            changelog = "See https://github.com/FabricMC/fabric-language-kotlin/commits/master for a changelog"

            mainArtifact(
                file("${project.buildDir}/libs/${base.archivesName.get()}-${version}.jar"),
                closureOf<CurseArtifact> {
                    displayName = "Fabric Language Kotlin $version"
                })
        })
    }
    project.afterEvaluate {
        tasks.getByName<CurseUploadTask>("curseforge${CURSEFORGE_ID}") {
            dependsOn(tasks.getByName<RemapJarTask>("remapJar"))
        }
    }
}

tasks.create<Copy>("processMDTemplates") {
    group = "documentation"
    from(rootDir.resolve("templates"))
    include("**/*.template.md")
    filesMatching("**/*.template.md") {
        name = sourceName.substringBeforeLast(".template.md") + ".md"
        expand(
            "MOD_VERSION" to "${modVersion}+kotlin.${kotlinVersion}",
            "LOADER_VERSION" to loaderVersion,
            "KOTLIN_VERSION" to kotlinVersion,
            "COROUTINES_VERSION" to coroutinesVersion,
            "SERIALIZATION_VERSION" to serializationVersion
        )
    }
    destinationDir = rootDir
}
