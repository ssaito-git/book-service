package bookservice.webapi.controller.book

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * 書籍の削除 API 実装
 */
@RestController
class DeleteBookController : DeleteBookApi {
    override fun deleteById(bookId: UUID): ResponseEntity<Unit> {
        return ResponseEntity.noContent().build()
    }
}
