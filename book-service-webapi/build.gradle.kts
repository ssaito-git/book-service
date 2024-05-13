plugins {
    kotlin("jvm")
    `book-service-detekt`
}

repositories {
    mavenCentral()
}

dependencies {
}

kotlin {
    jvmToolchain(21)
}
