[![maven-badge](https://img.shields.io/maven-metadata/v/https/maven.fabricmc.net/net/fabricmc/fabric-language-kotlin/maven-metadata.xml.svg?style=flat-square&logo=Kotlin)](https://maven.fabricmc.net/net/fabricmc/fabric-language-kotlin)

# fabric-language-kotlin
Fabric language module for [Kotlin](https://kotlinlang.org/). Adds support for using a Kotlin `object` as the main mod class and bundles the Kotlin libraries and runtime for you.

## Usage
Add it as a dependency:

build.gradle.kts
```kotlin
configurations.runtimeOnly.extendsFrom(configurations.modCompile)

dependencies {
	modCompile(group = "net.fabricmc", name = "fabric-language-kotlin", version = "1.3.10-27")
}
```

build.gradle
```groovy
configurations.runtimeOnly.extendsFrom(configurations.modCompile)

dependencies {
	modCompile(group: "net.fabricmc", name: "fabric-language-kotlin", version: "1.3.10-27")
}
```

Set the language adapter for your mod to use by setting the `languageAdapter` property in the `fabric.mod.json` file:

```json
{
    "languageAdapter": "net.fabricmc.language.kotlin.KotlinLanguageAdapter"
}
```

Add a dependency entry to your `fabric.mod.json` file:

```json
{
	"requires": {
		"fabric-language-kotlin": {
			"version": ">=1.3.10"
		}
	}
}
```

## Available Versions

https://maven.fabricmc.net/net/fabricmc/fabric-language-kotlin/