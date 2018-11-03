pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        maven(url = "http://maven.modmuss50.me"){
            name = "Fabric"
        }
        gradlePluginPortal()
    }
//    resolutionStrategy {
//        eachPlugin {
//            if(requested.id.id == "net.fabricmc") {
//                useModule("net.fabricmc:fabric-loom:${requested.version}")
//            }
//        }
//    }
}
rootProject.name = "fabric-language-kotlin"