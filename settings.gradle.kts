plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "book-service"

include(
    "book-service-core",
    "book-service-db",
    "book-service-webapi",
)
