plugins {
    alias(libs.plugins.docker.compose)
}

repositories {
    mavenCentral()
}

/**
 * Docker Compose の設定
 */
dockerCompose {
    /**
     * ローカル開発環境
     */
    createNested("localDev").apply {
        useComposeFiles.add("docker/compose.yml")
    }
}

/**
 * ローカル開発環境の開始
 *
 * - DB コンテナ開始
 * - DB マイグレーション
 */
tasks.register("localDevUp") {
    dependsOn("localDevComposeUp")
    dependsOn(":book-service-db:localDevFlywayMigrate")
}

/**
 * ローカル開発環境の終了
 *
 * - DB コンテナ終了
 */
tasks.register("localDevDown") {
    dependsOn("localDevComposeDown")
}
