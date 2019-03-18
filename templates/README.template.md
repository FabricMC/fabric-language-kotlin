[![maven-badge](https://img.shields.io/maven-metadata/v/https/maven.fabricmc.net/net/fabricmc/fabric-language-kotlin/maven-metadata.xml.svg?style=flat-square&logo=Kotlin)](https://maven.fabricmc.net/net/fabricmc/fabric-language-kotlin)
[![Files](https://curse.nikky.moe/api/img/308769/files?logo&style=flat-square)](https://minecraft.curseforge.com/projects/308769/files)
[![Download](https://curse.nikky.moe/api/img/308769?logo&style=flat-square)](https://curse.nikky.moe/api/url/308769?version=1.14-Snapshot)

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
    // other repos
    mavenCentral()
    jcenter()
}

dependencies {
    // TODO: loom 0.3.0 will allow using only modCompile
	modCompile(group = "net.fabricmc", name = "fabric-loader", version = "${LOADER_VERSION}")
    compileOnly(group = "net.fabricmc", name = "fabric-loader", version = "${LOADER_VERSION}")

    modCompile(group = "net.fabricmc", name = "fabric-language-kotlin", version = "${KOTLIN_VERSION}-SNAPSHOT")
	compileOnly(group = "net.fabricmc", name = "fabric-language-kotlin", version = "${KOTLIN_VERSION}-SNAPSHOT")
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
    // TODO: loom 0.3.0 will allow using only modCompile
	modCompile(group: "net.fabricmc", name: "fabric-loader", version: "${LOADER_VERSION}")
	compileOnly(group: "net.fabricmc", name: "fabric-loader", version: "${LOADER_VERSION}")

	modCompile(group: "net.fabricmc", name: "fabric-language-kotlin", version: "${KOTLIN_VERSION}-SNAPSHOT")
	compileOnly(group: "net.fabricmc", name: "fabric-language-kotlin", version: "${KOTLIN_VERSION}-SNAPSHOT")
}
```

use the `kotlin` adapter for your mod to use by setting the `adapter` property in the `fabric.mod.json` file:
and
Add a dependency entry to your `fabric.mod.json` file:

for more info reference [format:modjson](https://fabricmc.net/wiki/format:modjson)

```json
{
    "entrypoints": {
        "main": [
            {
                "adapter": "kotlin",
                "value": "package.ClassName"
            }
        ]
    },
    "requires": {
        "fabric-language-kotlin": ">=${KOTLIN_VERSION}"
    }
}
```

possible types for `value`:
 - `object` classes extending the proper initializer
 - `class` with a default constructor extending the proper initializer
 - `val` of a Initializer in some `object`, separated with `::`, example: `package.SomeClass\$Companion::initializer`
 - `fun` in some `object`, separated with `::`
 - top level `fun`, example : `package.MainKt::init`

Companion objects can be used by appending `\$Companion` to the class
take care of `processResource` there, it might try to expand it, in that case escape it

top level functions can be used by adding `Kt` to the filename

see examples in [sample-mod/fabric.mod.json](https://github.com/FabricMC/fabric-language-kotlin/blob/master/sample-mod/src/main/resources/fabric.mod.json)

## Available Versions

https://maven.fabricmc.net/net/fabricmc/fabric-language-kotlin/

## Updating README

update the readme in `temaplates/README.template.md`
run `./gradlew processMDTemplates`
