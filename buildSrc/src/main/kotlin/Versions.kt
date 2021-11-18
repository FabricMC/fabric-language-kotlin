object Jetbrains {

    object Kotlin {
        const val version = "1.5.31"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
        const val stdLibJkd7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
        const val stdLibJkd8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
    }

    object KotlinX {
        object Coroutines {
            const val version = "1.5.2"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val coreJvm = "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$version"
            const val jdk8 = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$version"
        }
        object Serialization {
            const val version = "1.3.0"
            const val coreJvm = "org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:$version"
            const val jsonJvm = "org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:$version"
            const val cborJvm = "org.jetbrains.kotlinx:kotlinx-serialization-cbor-jvm:$version"
        }
    }
}

object Fabric {
    object Loader {
        const val version = "0.11.7" // https://maven.fabricmc.net/net/fabricmc/fabric-loader/
    }

    object Loom {
        const val version = "0.7-SNAPSHOT"
    }
}

object CurseGradle {
    const val version = "1.4.0"
}
