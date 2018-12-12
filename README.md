# fabric-language-kotlin
Fabric language module for [Kotlin](https://kotlinlang.org/). Adds support for using a Kotlin `object` as the main mod class and bundles the Kotlin libraries and runtime for you.

## Usage
Add it as a dependency:

```groovy
dependencies {
	compile "net.fabricmc:fabric-language-kotlin:1.3.10-1"
}
```

Set the language adapter for your mod to use by setting the `languageAdapter` property in the `mod.json` file:

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
			"version": ">=1.3.10-1"
		}
	}
}
```

## Available Versions

https://maven.fabricmc.net/net/fabricmc/fabric-language-kotlin/