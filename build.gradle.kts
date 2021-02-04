import com.matthewprenger.cursegradle.CurseArtifact
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseUploadTask
import com.matthewprenger.cursegradle.Options
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask

plugins {
    kotlin("jvm") version Jetbrains.Kotlin.version
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

val buildNumber: String? = System.getenv("GITHUB_RUN_NUMBER")

project.group = group
project.description = description
version = buildNumber?.let { "${modVersion}+build.$buildNumber" }
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
    includeAndExpose(Jetbrains.KotlinX.Coroutines.coreJvm)
    includeAndExpose(Jetbrains.KotlinX.Coroutines.jdk8)
    includeAndExpose(Jetbrains.KotlinX.Serialization.coreJvm)
    includeAndExpose(Jetbrains.KotlinX.Serialization.jsonJvm)
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
            addGameVersion("1.17")
            addGameVersion("Fabric")

            changelog = "See https://github.com/FabricMC/fabric-language-kotlin/commits/master for a changelog"

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
            "BUNDLED_COROUTINES_JDK8" to Jetbrains.KotlinX.Coroutines.version,
            "BUNDLED_SERIALIZATION_CORE" to Jetbrains.KotlinX.Serialization.version,
            "BUNDLED_SERIALIZATION_JSON" to Jetbrains.KotlinX.Serialization.version
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
