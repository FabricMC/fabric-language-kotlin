object Kotlin {
    const val version = "1.3.0"
    const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
    const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
}

object KotlinX {
    object Coroutines {
        const val version = "1.0.0"
        const val dependency = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    }
}

object Fabric {
    const val version = "0.1.0.39"
    object Loom {
        const val version = "0.0.12-SNAPSHOT"
    }
    object Pomf {
        const val version = "17"
    }
}

object Constants {
    const val group = "net.fabricmc"
    const val modid = "fabric-language-kotlin"
    const val description = "Fabric language module for Kotlin"
    const val url = "https://github.com/FabricMC/fabric-language-kotlin"

    const val version = "0.1.0"
}

object Minecraft {
    const val version = "18w44a"
}