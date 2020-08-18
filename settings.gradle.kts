import de.fayard.dependencies.bootstrapRefreshVersionsAndDependencies
pluginManagement {
    repositories {
        maven(url = "http://maven.fabricmc.net") {
            name = "Fabric"
        }
        gradlePluginPortal()
    }
}
buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies.classpath("de.fayard:dependencies:0.5.8")
}
bootstrapRefreshVersionsAndDependencies()

//val props = rootDir.resolve("gradle.properties").bufferedReader().use {
//    java.util.Properties().apply {
//        load(it)
//    }
//}
//
//val modId: String by props
//val modVersion: String by props
//
//rootProject.name = modId