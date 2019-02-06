object Jetbrains {
    private const val annotationsVersion = "16.0.3"
    const val annotations = "org.jetbrains:annotations:$annotationsVersion"

    object Kotlin {
        const val version = "1.3.20"
        const val stdLibJkd8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
    }

    object KotlinX {
        private const val coroutinesVersion = "1.1.1"
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
        const val coroutinesJdk8 = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion"
    }
}

object Fabric {
    object Loader {
        const val version = "0.3.4.+"
    }

    object API {
        const val version = "0.1.5"
    }

    object Loom {
        const val version = "0.2.0-SNAPSHOT"
    }

    object Yarn {
        const val version = "+"
    }
    object LanguageKotlin {
        const val version = "${Jetbrains.Kotlin.version}+"
    }
}

object Minecraft {
    const val version = "19w05a"
}