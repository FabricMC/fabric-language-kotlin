object Jetbrains {

    object Annotations {
        const val version = "20.0.0"
        const val dependency = "org.jetbrains:annotations:$version"
    }


    object Kotlin {
        const val version = "1.4.0"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
        const val stdLibJkd7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
        const val stdLibJkd8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
    }

    object KotlinX {
        object Coroutines {
            const val version = "1.3.9"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val jdk8 = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$version"
        }

    }
}

object Fabric {
    object Loader {
        const val version = "0.10.8" // https://maven.fabricmc.net/net/fabricmc/fabric-loader/
    }

    object Loom {
        const val version = "0.5.12"
    }
}

object CurseGradle {
    const val version = "1.4.0"
}
