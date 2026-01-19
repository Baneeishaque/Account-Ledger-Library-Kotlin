import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

val ktorVersion: String = "3.4.0-eap-1520"
val retrofitVersion = "3.0.0"

plugins {
    kotlin(module = "jvm")
    kotlin(module = "plugin.serialization")
    `java-library`
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") }
}

dependencies {
    // Kotlin BOM
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Project modules
    implementation(project(":common-lib:common-lib"))
    implementation(project(":account-ledger-lib-multi-platform:lib"))

    // Retrofit + OkHttp
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // Kotlinx
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    // CSV and text utils
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.10.0")
    implementation("com.massisframework:j-text-utils:0.3.4")

    // Dotenv
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")

    // Ktor client
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.23")
}

testing {
    suites {
        val test: JvmTestSuite by getting(type = JvmTestSuite::class) {
            useKotlinTest()
        }
    }
}

kotlin {
    compilerOptions {
//        allWarningsAsErrors = true
        verbose = true
        apiVersion = KotlinVersion.KOTLIN_2_2
        languageVersion = KotlinVersion.KOTLIN_2_2
        javaParameters = true
        jvmTarget = JvmTarget.JVM_21
    }

    sourceSets.all {
        languageSettings.apply {
            languageVersion = KotlinVersion.KOTLIN_2_2.version
            apiVersion = KotlinVersion.KOTLIN_2_2.version
        }
    }
}
