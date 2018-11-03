import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.LoomGradleExtension

plugins {
    kotlin("jvm") version Kotlin.version
    id("idea")
    id("maven")
    id("com.github.johnrengelman.shadow") version "2.0.4"
    id("net.minecrell.licenser") version "0.4.1"
    id("fabric-loom") version Fabric.Loom.version
}

base {
    archivesBaseName = Constants.modid
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

apply(from = "https://github.com/FabricMC/fabric-docs/raw/master/gradle/maven.gradle")

configure<LoomGradleExtension> {
    version = Minecraft.version
    fabricVersion = Fabric.version
    pomfVersion = Fabric.Pomf.version
}

license {
    header = file("HEADER")
    include("**/*.java")
    include("**/*.kt")
}

repositories {
    jcenter()
}

dependencies {
    compile(Kotlin.stdLib)
    compile(Kotlin.reflect)
    compile(KotlinX.Coroutines.dependency)
}

val shadowJar by tasks.getting(ShadowJar::class) {
    classifier = ""
    dependencies {
        include(dependency(Kotlin.stdLib))
        include(dependency(Kotlin.reflect))
        include(dependency(KotlinX.Coroutines.dependency))
    }
}

val build by tasks.getting(Task::class) {
    dependsOn(shadowJar)
}

artifacts {
    add("archives", shadowJar)
}