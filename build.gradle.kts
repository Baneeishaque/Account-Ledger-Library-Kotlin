plugins {
    
    // TODO : Use BoM
    // val kotlinVersion = "1.9.20-mercury-653"
    val kotlinVersion = "2.0.0-Beta1-16"
    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
}
