package bookservice.webapi.config

import org.openapitools.jackson.nullable.JsonNullableModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Jackson の設定
 */
@Configuration
class JacksonConfig {
    /**
     * jackson-databind-nullable を定義する。
     */
    @Bean
    fun jsonNullableModule(): JsonNullableModule {
        val module = JsonNullableModule()
        return module
    }
}
