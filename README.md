[![maven-badge](https://img.shields.io/maven-metadata/v/https/maven.fabricmc.net/net/fabricmc/fabric-language-kotlin/maven-metadata.xml.svg?style=flat-square&logo=Kotlin)](https://maven.fabricmc.net/net/fabricmc/fabric-language-kotlin)
[![Files](https://curse.nikky.moe/api/img/308769/files?logo&style=flat-square)](https://minecraft.curseforge.com/projects/308769/files)
[![Download](https://curse.nikky.moe/api/img/308769?logo&style=flat-square)](https://curse.nikky.moe/api/url/308769?version=1.14-Snapshot)

# fabric-language-kotlin
Fabric language module for [Kotlin](https://kotlinlang.org/). Adds support for using a Kotlin `object` as the main mod class and bundles the Kotlin libraries and runtime for you.

## Usage
Add it as a dependency:

Repositories for build.gradle.kts
```kotlin
repositories {
    // [...]
    maven(url = "http://maven.fabricmc.net/") {
        name = "Fabric"
    }
}
```

Repositories for build.gradle:
```groovy
repositories {
    // [...]
    maven {
        url = "http://maven.fabricmc.net/"
        name = "Fabric"
    }
}
```

Dependencies (build.gradle and build.gradle.kts):

```kotlin
dependencies {
    // [...]
    modImplementation(group = "net.fabricmc", name = "fabric-language-kotlin", version = "1.4.21+build.1")
}
```

Use the `kotlin` adapter for your mod by setting the `adapter` property in the `fabric.mod.json` file. 
Remember to the add a dependency entry to your `fabric.mod.json` file:

```json
{
    "schemaVersion":  1, 
    "entrypoints": {
        "main": [
            {
                "adapter": "kotlin",
                "value": "package.ClassName"
            }
        ]
    },
    "depends": {
        "fabric-language-kotlin": ">=1.4.21"
    }
}
```

For more info reference [format:modjson](https://fabricmc.net/wiki/format:modjson).

Do not forget to set the `schemaVersion` to `1` or it will fall back to schema `0` and will not attempt to load entrypoints.

### entrypoints samples

#### class reference

As a `class`

<details>
  <summary>Click to view code</summary><p>

```json
{
    "adapter": "kotlin",
    "value": "mymod.MyMod"
}
```

```kotlin
package mymod
class MyMod : ModInitializer {
    override fun onInitialize() {
        TODO()
    }
}
```
</p></details>

As an `object`

<details>
  <summary>Click to view code</summary><p>

```json
{
    "adapter": "kotlin",
    "value": "mymod.MyMod"
}
```

```kotlin
package mymod
object MyMod : ModInitializer {
    override fun onInitialize() {
        TODO()
    }
}
```
</p></details>

As a `companion object`

<details>
  <summary>Click to view code</summary><p>

```json
{
    "adapter": "kotlin",
    "value": "mymod.MyMod$Companion"
}
```

```kotlin
package mymod
class MyMod {
    companion object : ModInitializer {
        override fun onInitialize() {
            TODO()
        }
    }
}
```
</p></details>

#### function reference

Functions do not get returned but executed, 
so they have to only contain initialization code, 
not return a initializer type.

In an `object`

<details>
  <summary>Click to view code</summary><p>

```json
{
    "adapter": "kotlin",
    "value": "mymod.MyMod::init"
}
```

```kotlin
package mymod
object MyMod  {
    fun init() {
        TODO()
    }
}
```
</p></details>

In a `companion object`
<details>
  <summary>Click to view code</summary><p>

```json
{
    "adapter": "kotlin",
    "value": "mymod.MyMod$Companion::init"
}
```

```kotlin
package mymod
class MyMod  {
    companion object {
        fun init() {
            TODO()
        }
    }
}
```
</p></details>

As a top level function

<details>
  <summary>Click to view code</summary><p>

The classname gets constructed by taking the filename and appending `Kt`.
```json
{
    "adapter": "kotlin",
    "value": "mymod.MyModKt::init"
}
```

File: `src/main/kotlin/mymod/MyMod.kt`
```kotlin
package mymod

fun init() {
    TODO()
}
```
</p></details>

#### field reference

In an `object`

<details>
  <summary>Click to view code</summary><p>

```json
{
    "adapter": "kotlin",
    "value": "mymod.MyMod::initializer"
}
```

```kotlin
package mymod
object MyMod  {
    val initializer = ModInitializer {
        TODO()
    }
}
```
</p></details>

In a `companion object`

<details>
  <summary>Click to view code</summary><p>

```json
{
    "adapter": "kotlin",
    "value": "mymod.MyMod$Companion::initializer"
}
```

```kotlin
package mymod
class MyMod  {
    companion object {
        val initializer = ModInitializer {
            TODO()
        }
    }
}
```
</p></details>

Companion objects can be used by appending `$Companion` to the class.
Take care of `processResource` there, it might try to expand it, in that case escape it.

See examples in [sample-mod/fabric.mod.json](https://github.com/FabricMC/fabric-language-kotlin/blob/master/sample-mod/src/main/resources/fabric.mod.json).

## Bundled libraries

```
org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.21
org.jetbrains.kotlin:kotlin-reflect:1.4.21
org.jetbrains:annotations:20.0.0
org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2
org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.4.2
```

## Available Versions

https://maven.fabricmc.net/net/fabricmc/fabric-language-kotlin/

## Updating README

- Update the readme in `temaplates/README.template.md`.
- Run `./gradlew processMDTemplates`.
