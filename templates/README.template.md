# fabric-language-kotlin

[![maven-badge](https://img.shields.io/maven-metadata/v/https/maven.fabricmc.net/net/fabricmc/fabric-language-kotlin/maven-metadata.xml.svg?style=flat-square&logo=Kotlin&label=Maven)](https://maven.fabricmc.net/net/fabricmc/fabric-language-kotlin)
[![modrinth-badge](https://img.shields.io/modrinth/dt/fabric-language-kotlin?label=Modrinth&logo=Modrinth&style=flat-square)](https://modrinth.com/mod/fabric-language-kotlin/versions)
[![curseforge-badge](https://curse.nikky.moe/api/img/308769/files?logo&style=flat-square&label=Curseforge)](https://minecraft.curseforge.com/projects/308769/files)

Fabric language module for [Kotlin](https://kotlinlang.org/). Adds support for Kotlin exclusive entrypoints and bundles the Kotlin Stdlib as well as common kotlinx libraries.

## Usage

### Dependency

Add it as a dependency to your Gradle project:

```kotlin
dependencies {
    modImplementation("net.fabricmc:fabric-language-kotlin:${MOD_VERSION}")
}
```

### Adapter

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
        "fabric-language-kotlin": ">=${MOD_VERSION}"
    }
}
```

For more info reference the [fabric.mod.json documentation](https://fabricmc.net/wiki/documentation:fabric_mod_json).

Do not forget to set the `schemaVersion` to `1` or it will fall back to schema `0` and will not attempt to load entrypoints.

### Entrypoint samples

<table>
<tr>
<th><i>Kind</i></th>
<th>Class reference</th>
<th>Function reference</th>
<th>Field reference</th>
</tr>

<tr>
<td><code>class</code></td>
<td>

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

</td>

<td>



</td>

</tr>
<tr>
<td><code>object</code></td>
<td>

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

</td>
<td>

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

</td>
<td>

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

</td>
</tr>
<tr>
<td><code>companion object</code></td>
<td>

```json
{
    "adapter": "kotlin",
    "value": "mymod.MyMod\$Companion"
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

</td>
<td>

```json
{
    "adapter": "kotlin",
    "value": "mymod.MyMod\$Companion::init"
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

</td>
<td>

```json
{
    "adapter": "kotlin",
    "value": "mymod.MyMod\$Companion::initializer"
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

</td>
</tr>
<tr>
<td><code>top level</code></td>
<td></td>
<td>

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

</td>
</tr>
</table>

Companion objects can be used by appending `\$Companion` to the class.
**Take care of `processResources` there**, it might try to expand it, in that case escape it.

## Bundled libraries

```
org.jetbrains.kotlin:kotlin-stdlib-jdk8:${KOTLIN_VERSION}
org.jetbrains.kotlin:kotlin-reflect:${KOTLIN_VERSION}
org.jetbrains.kotlinx:kotlinx-coroutines-core:${COROUTINES_VERSION}
org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${COROUTINES_VERSION}
org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:${SERIALIZATION_VERSION}
org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:${SERIALIZATION_VERSION}
org.jetbrains.kotlinx:kotlinx-serialization-cbor-jvm:${SERIALIZATION_VERSION}
org.jetbrains.kotlinx:atomicfu-jvm:${ATOMICFU_VERSION}
org.jetbrains.kotlinx:kotlinx-datetime-jvm:${DATETIME_VERSION}
```

## Available Versions

https://maven.fabricmc.net/net/fabricmc/fabric-language-kotlin/

## Updating README

- Update the readme in `templates/README.template.md`.
- Run `./gradlew processMDTemplates`.
