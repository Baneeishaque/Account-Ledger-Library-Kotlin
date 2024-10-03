import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

val ktorVersion: String = "2.1.2"

plugins {

    kotlin(module = "jvm")
    kotlin(module = "plugin.serialization")
    `java-library`
}

repositories {

    mavenCentral()
    maven {

        url = uri(path = "https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }
    maven {

        url = uri(path = "https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }
}

dependencies {

    implementation(dependencyNotation = platform(notation = "org.jetbrains.kotlin:kotlin-bom"))

    implementation(dependencyNotation = project(path = ":common-lib:common-lib"))
    implementation(dependencyNotation = project(path = ":account-ledger-lib-multi-platform:lib"))

    implementation(dependencyNotation = "com.squareup.retrofit2:retrofit:2.11.0")
    implementation(dependencyNotation = "com.squareup.retrofit2:converter-gson:2.11.0")

    implementation(dependencyNotation = "org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
    implementation(dependencyNotation = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    implementation(dependencyNotation = "com.github.doyaaaaaken:kotlin-csv-jvm:1.9.3")
    implementation(dependencyNotation = "com.massisframework:j-text-utils:0.3.4")
    implementation(dependencyNotation = "io.github.cdimascio:dotenv-kotlin:6.4.1")

    implementation(dependencyNotation = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0")

    implementation(dependencyNotation = "io.ktor:ktor-client-core:$ktorVersion")
    implementation(dependencyNotation = "io.ktor:ktor-client-cio:$ktorVersion")

    implementation(dependencyNotation = "io.ktor:ktor-client-logging:$ktorVersion")
    implementation(dependencyNotation = "ch.qos.logback:logback-classic:1.5.6")

    implementation(dependencyNotation = "io.ktor:ktor-client-auth:$ktorVersion")

    implementation(dependencyNotation = "io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation(dependencyNotation = "io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
}

testing {

    suites {

        val test: JvmTestSuite by getting(type = JvmTestSuite::class) {

            useKotlinTest()
        }
    }
}

kotlin{

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
