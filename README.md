[![maven-badge](https://img.shields.io/maven-metadata/v/https/maven.fabricmc.net/net/fabricmc/fabric-language-kotlin/maven-metadata.xml.svg?style=flat-square&logo=Kotlin)](https://maven.fabricmc.net/net/fabricmc/fabric-language-kotlin)

# fabric-language-kotlin
Fabric language module for [Kotlin](https://kotlinlang.org/). Adds support for using a Kotlin `object` as the main mod class and bundles the Kotlin libraries and runtime for you.

## Usage
Add it as a dependency:

build.gradle.kts
```kotlin
repositories {
    maven(url = "http://maven.fabricmc.net/") {
        name = "Fabric"
    }
    maven(url = "https://kotlin.bintray.com/kotlinx") {
        name = "Kotlin X"
    }
}

dependencies {
	modCompile(group = "net.fabricmc", name = "fabric-language-kotlin", version = "1.3.20+")
	compileOnly(group = "net.fabricmc", name = "fabric-language-kotlin", version = "1.3.20+")
}
```

build.gradle
```groovy
repositories {
    maven {
        url = "http://maven.fabricmc.net/"
        name = "Fabric"
    }
    maven {
        url = "https://kotlin.bintray.com/kotlinx"
        name = "Kotlin X"
    }
}

dependencies {
	modCompile(group: "net.fabricmc", name: "fabric-language-kotlin", version: "1.3.20+")
	compileOnly(group: "net.fabricmc", name: "fabric-language-kotlin", version: "1.3.20+")
}
```

Set the language adapter for your mod to use by setting the `languageAdapter` property in the `fabric.mod.json` file:
and
Add a dependency entry to your `fabric.mod.json` file:

```json
{
    "languageAdapter": "net.fabricmc.language.kotlin.KotlinLanguageAdapter",
	"requires": {
		"fabric-language-kotlin": {
			"version": ">=1.3.20"
		}
	}
}
```

the version is ignored right now anyways but this is how it should work.. in theory

## Available Versions

https://maven.fabricmc.net/net/fabricmc/fabric-language-kotlin/