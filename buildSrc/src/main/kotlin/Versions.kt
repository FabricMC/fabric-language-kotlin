object Jetbrains {
    private const val annotationsVersion = "17.0.0"
    const val annotations = "org.jetbrains:annotations:$annotationsVersion"

    object Kotlin {
        const val version = "1.3.72"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
        const val stdLibJkd7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
        const val stdLibJkd8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
    }

    object KotlinX {
        private const val coroutinesVersion = "1.3.9"
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
        const val coroutinesJdk8 = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion"
    }
}

object Fabric {
    object Loader {
        const val version = "0.8.8+build.202" // https://maven.fabricmc.net/net/fabricmc/fabric-loader/
    }

    object API {
        const val version = "0.13.1+build.370-1.16"
    }

    object Loom {
        const val version = "0.4-SNAPSHOT"
    }

    object YarnMappings {
        const val version = "${Minecraft.version}+build.1"
    }
}

object Minecraft {
    const val version = "1.16"
}

object CurseGradle{
    const val version = "1.4.0"
}
