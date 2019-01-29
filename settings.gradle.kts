pluginManagement {
    repositories {
        maven(url = "http://maven.fabricmc.net"){
            name = "Fabric"
        }
        gradlePluginPortal()
    }
}
rootProject.name = Constants.modid

include("sample-mod")