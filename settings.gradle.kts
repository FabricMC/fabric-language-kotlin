pluginManagement {
    repositories {
        maven(url = "http://maven.fabricmc.net"){
            name = "Fabric"
        }
        gradlePluginPortal()
    }
}
rootProject.name = Constants.modid

val includeSample: String by settings
if (includeSample == "true") {
    include("sample-mod")
}
