val ktorVersion: String = "2.1.2"

plugins {

    id("org.jetbrains.kotlin.jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
    `java-library`
}

repositories {

    mavenCentral()
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }
    maven {
        url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }
}

dependencies {

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation(project(":common-lib:common-lib"))
    implementation(project(":account-ledger-lib-multi-platform:lib"))

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.9.0")
    implementation("com.massisframework:j-text-utils:0.3.4")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")

    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.4.7")

    implementation("io.ktor:ktor-client-auth:$ktorVersion")

    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
}

testing {

    suites {
        val test by getting(JvmTestSuite::class) {
            useKotlinTest()
        }
    }
}
