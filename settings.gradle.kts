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
val existsLibrary = rootDir.resolve("build").resolve("libs").run {
    if(exists() && isDirectory) {
        listFiles().any {
            !it.name.endsWith("-dev.jar")
                && !it.name.endsWith("-sources.jar")
                && !it.name.endsWith("-sources-dev.jar")
                && it.name.endsWith(".jar")
                && it.name.startsWith(Constants.modid + "-" + Constants.modVersion)
        }
    } else false
}
if (includeSample != "false" && existsLibrary) {
    include("sample-mod")
}
