package bookservice.webapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * アプリケーション
 */
@SpringBootApplication(scanBasePackages = ["bookservice.webapi", "bookservice.db"])
class MainApplication

/**
 * メイン関数
 */
fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    runApplication<MainApplication>(*args)
}
