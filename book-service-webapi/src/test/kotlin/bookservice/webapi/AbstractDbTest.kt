package bookservice.webapi

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

abstract class AbstractDbTest {
    companion object {
        private val container = PostgreSQLContainer(DockerImageName.parse("postgres:latest")).apply {
            withInitScript("init.sql")
            start()
        }

        @JvmStatic
        @BeforeAll
        fun flywayMigrate() {
            Flyway.configure()
                .dataSource(container.jdbcUrl, container.username, container.password)
                .load()
                .migrate()
        }

        @JvmStatic
        @DynamicPropertySource
        fun dbProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { container.jdbcUrl }
            registry.add("spring.datasource.username") { container.username }
            registry.add("spring.datasource.password") { container.password }
        }
    }
}
