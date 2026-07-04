pluginManagement {
	repositories {
		maven("https://maven.fabricmc.net/") {
			name = "Fabric"
		}
		mavenCentral()
		gradlePluginPortal()
	}

	plugins {
		id("net.fabricmc.fabric-loom") version providers.gradleProperty("loom_version").get()
		id("org.jetbrains.kotlin.jvm") version providers.gradleProperty("kotlin_version").get()
	}
}

rootProject.name = "fabric-language-kotlin"