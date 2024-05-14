import org.flywaydb.gradle.task.FlywayMigrateTask

plugins {
    kotlin("jvm")
    `java-library`
    alias(libs.plugins.flyway)
    alias(libs.plugins.jooq.codegen.gradle)
    alias(libs.plugins.docker.compose)
    `book-service-detekt`
}

buildscript {
    dependencies {
        // Flyway プラグインのタスクで利用する
        classpath(libs.postgresql)
        classpath(libs.flyway.database.postgresql)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":book-service-core"))

    // jOOQ
    implementation(libs.jooq)

    // PostgreSQL JDBC Driver
    implementation(libs.postgresql)

    // Flyway
    testImplementation(libs.flyway.core)
    testRuntimeOnly(libs.flyway.database.postgresql)

    // JUnit
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    // Testcontainers
    testImplementation(platform(libs.testcontainers.bom))
    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.postgresql)

    // Database Rider
    testImplementation(libs.rider.core)
    testImplementation(libs.rider.junit5)

    // Apache Commons CSV
    testImplementation(libs.commons.csv)
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

sourceSets {
    main {
        kotlin {
            // jOOQ で生成したコードを追加
            val outputDirectory = layout.buildDirectory.dir("generated-jooq")
            srcDir(outputDirectory.get().asFile)
        }
    }
}

/**
 * Docker Compose の設定
 */
dockerCompose {
    createNested("postgres").apply {
        useComposeFiles.addAll("../docker/compose.yml", "../docker/compose.jooq.yml")
        startedServices.add("db")
    }
}

/**
 * Flyway の設定
 */
flyway {
    url = "jdbc:postgresql://localhost:5432/book_service"
    user = "postgres"
    password = "secret"
    defaultSchema = "book_service_schema"
}

/**
 * jOOQ の設定
 */
jooq {
    configuration {
        jdbc {
            url = "jdbc:postgresql://localhost:5432/book_service"
            user = "postgres"
            password = "secret"
        }
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            target {
                val outputDirectory = layout.buildDirectory.dir("generated-jooq")
                packageName = "bookservice.db.jooq"
                directory = outputDirectory.get().asFile.path
            }
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                inputSchema = "book_service_schema"
                // Flyway が作成するバージョン管理テーブルは除外する
                excludes = "flyway_schema_history"
            }
            generate {
                isKotlinNotNullPojoAttributes = true
                isKotlinNotNullRecordAttributes = true
                isKotlinNotNullInterfaceAttributes = true
            }
        }
    }
}

/**
 * jOOQ コード生成タスク用の Flyway マイグレーションタスク
 */
val jooqFlywayMigrateTask = tasks.register<FlywayMigrateTask>("jooqFlywayMigrate") {
    user = "postgres"
    password = "secret"
    defaultSchema = "book_service_schema"
}

/**
 * jOOQ コード生成タスクのカスタマイズ
 *
 * コード生成で利用する DB は Docker で作成する。
 * 以下の順番でタスクを実行する。
 *
 * 1. postgresComposeUp - PostgreSQL のコンテナ起動
 * 2. jooqFlywayMigrate - Flyway でマイグレーション
 * 3. jooqCodegen - コード生成
 * 4. postgresComposeDown - PostgreSQL のコンテナ終了
 */
tasks.named("jooqCodegen") {
    dependsOn("postgresComposeUp")
    dependsOn("jooqFlywayMigrate")
    tasks["jooqFlywayMigrate"].mustRunAfter("postgresComposeUp")
    finalizedBy("postgresComposeDown")

    tasks["postgresComposeUp"].doLast {
        // ポート番号をランダムで設定する場合は以下の方法で設定されたポート番号を取得する
        val postgresServiceInfo =
            dockerCompose.nested("postgres").servicesInfos["db"]?.firstContainer
                ?: error("postgres container not found.")

        val url = "jdbc:postgresql://localhost:${postgresServiceInfo.ports[5432]}/book_service"

        jooqFlywayMigrateTask.get().url = url

        jooq.executions.getByName("").configuration.jdbc.url = url
    }
}

/**
 * ローカル開発環境の Flyway マイグレーションタスク
 *
 * 通常のマイグレーションと開発データ登録を実行する。
 */
tasks.register<FlywayMigrateTask>("localDevFlywayMigrate") {
    dependsOn("jooqCodegen")
    dependsOn("testClasses")
    tasks["compileKotlin"].mustRunAfter("jooqCodegen")

    url = "jdbc:postgresql://localhost:5432/book_service"
    user = "postgres"
    password = "secret"
    locations = arrayOf("classpath:db/migration", "classpath:/testdata")
    defaultSchema = "book_service_schema"
}
