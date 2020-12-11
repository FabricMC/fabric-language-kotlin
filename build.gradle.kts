import com.matthewprenger.cursegradle.CurseArtifact
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseUploadTask
import com.matthewprenger.cursegradle.Options
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask
import java.util.Properties

plugins {
    kotlin("jvm") version Jetbrains.Kotlin.version
    id("moe.nikky.persistentCounter") version "0.0.8-SNAPSHOT"
    id("net.minecrell.licenser") version "0.4.1"
    id("com.matthewprenger.cursegradle") version CurseGradle.version
    id("fabric-loom") version Fabric.Loom.version
    `maven-publish`
}

val modId: String by project
val modVersion: String by project
val group: String by project
val description: String by project
val minecraftVersion: String by project

base {
    archivesBaseName = modId
}

val branch = System.getenv("GIT_BRANCH")
    ?.takeUnless { it == "master" }
    ?.let { "-$it" }
    ?: ""

val buildNumber = counter.variable(id = "buildNumber", key = Constants.modVersion + branch)

project.group = group
project.description = description
version = System.getenv("BUILD_NUMBER")?.let { "${modVersion}+build.$buildNumber" }
    ?: "${modVersion}+local"

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

fun DependencyHandlerScope.includeAndExpose(dep: Any) {
    modApi(dep)
    include(dep)
}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = minecraftVersion)
    mappings(group = "net.fabricmc", name = "yarn", version = "$minecraftVersion+build.7", classifier = "v2")

    modImplementation("net.fabricmc:fabric-loader:${Fabric.Loader.version}")

    includeAndExpose(kotlin("stdlib", Jetbrains.Kotlin.version))
    includeAndExpose(kotlin("stdlib-jdk8", Jetbrains.Kotlin.version))
    includeAndExpose(kotlin("stdlib-jdk7", Jetbrains.Kotlin.version))
    includeAndExpose(kotlin("reflect", Jetbrains.Kotlin.version))
    includeAndExpose(Jetbrains.Annotations.dependency)
    includeAndExpose(Jetbrains.KotlinX.Coroutines.core)
    includeAndExpose(Jetbrains.KotlinX.Coroutines.jdk8)
}

val remapJar = tasks.getByName<RemapJarTask>("remapJar")
val remapSourcesJar = tasks.getByName<RemapSourcesJarTask>("remapSourcesJar")

val sourcesJar = tasks.create<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create("main", MavenPublication::class.java) {

            groupId = project.group.toString()
            artifactId = project.name.toLowerCase()
            version = project.version.toString()

            artifact(remapJar) {
                builtBy(remapJar)
            }
            artifact(sourcesJar) {
                builtBy(remapSourcesJar)
            }
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
            addGameVersion("Fabric")

            val changelog_file: String? by project
            if (changelog_file != null) {
                println("changelog = $changelog_file")
                changelogType = "markdown"
                changelog = file(changelog_file as String)
            }
            mainArtifact(
                file("${project.buildDir}/libs/${base.archivesBaseName}-${version}.jar"),
                closureOf<CurseArtifact> {
                    displayName = "Fabric Language Kotlin $version"
                })
        })
    }
    project.afterEvaluate {
        tasks.getByName<CurseUploadTask>("curseforge${CURSEFORGE_ID}") {
            dependsOn(remapJar)
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
            "KOTLIN_VERSION" to Jetbrains.Kotlin.version,
            "LOADER_VERSION" to Fabric.Loader.version,
            "BUNDLED_STDLIB" to Jetbrains.Kotlin.version,
            "BUNDLED_REFLECT" to Jetbrains.Kotlin.version,
            "BUNDLED_ANNOTATIONS" to Jetbrains.Annotations.version,
            "BUNDLED_COROUTINES_CORE" to Jetbrains.KotlinX.Coroutines.version,
            "BUNDLED_COROUTINES_JDK8" to Jetbrains.KotlinX.Coroutines.version
        )
    }
    destinationDir = rootDir
}

task<DefaultTask>("depsize") {
    group = "help"
    description = "prints dependency sizes"
    doLast {
        val formatStr = "%,10.2f"
        val size = configurations.default.resolve()
            .map { it.length() / (1024.0 * 1024.0) }.sum()

        val out = buildString {
            append("Total dependencies size:".padEnd(45))
            append("${String.format(formatStr, size)} Mb\n\n")
            configurations
                .default
                .resolve()
                .sortedWith(compareBy { -it.length() })
                .forEach {
                    append(it.name.padEnd(45))
                    append("${String.format(formatStr, (it.length() / 1024.0))} kb\n")
                }
        }
        println(out)
    }
}
