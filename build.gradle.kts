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
 * ローカル開発環境の起動
 *
 * - DB コンテナ起動
 * - DB マイグレーション
 */
tasks.register("localDev") {
    dependsOn("localDevComposeUp")
    dependsOn(":book-service-db:localDevFlywayMigrate")
}
