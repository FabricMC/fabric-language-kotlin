pluginManagement {
    repositories {
        mavenLocal()
        maven(url = "http://maven.modmuss50.me"){
            name = "Fabric"
        }
        gradlePluginPortal()
    }
}
rootProject.name = Constants.modid