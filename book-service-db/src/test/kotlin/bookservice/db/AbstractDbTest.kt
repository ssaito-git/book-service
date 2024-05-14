package bookservice.db

import com.github.database.rider.core.api.connection.ConnectionHolder
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.sql.DriverManager
import java.util.Properties

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

        val connectionHolder = ConnectionHolder {
            val properties = Properties().apply {
                setProperty("user", container.username)
                setProperty("password", container.password)
                setProperty("currentSchema", "book_service_schema")
            }

            DriverManager.getConnection(container.jdbcUrl, properties)
        }
    }
}
