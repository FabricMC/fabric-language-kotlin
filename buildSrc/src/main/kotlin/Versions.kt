object Kotlin {
    const val version = "1.3.10"
    const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
    const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
}

object KotlinX {
    object Coroutines {
        const val version = "1.0.1"
        const val dependency = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    }
}

object Minecraft {
    const val version = "18w48b"
}

object Fabric {
    const val version = "0.1.0.48"
    object Loom {
        const val version = "0.0.13-SNAPSHOT"
    }
    object Pomf {
        const val version = "5"
    }
}
