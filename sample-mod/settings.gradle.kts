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
