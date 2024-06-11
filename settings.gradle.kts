rootProject.name = "Account-Ledger-Library-Kotlin"
include(":common-lib:common-lib",":account-ledger-lib",":account-ledger-lib-multi-platform:lib")
pluginManagement {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
        }
        google()
    }
}
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
        }
        maven {
            url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
        }
        google()
    }
}
