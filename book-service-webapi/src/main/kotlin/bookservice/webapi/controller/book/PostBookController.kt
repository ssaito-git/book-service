package bookservice.webapi.controller.book

import bookservice.webapi.controller.book.dto.BookResponse
import bookservice.webapi.controller.book.dto.PostBookRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

/**
 * 書籍の登録 API 実装
 */
@RestController
class PostBookController : PostBookApi {
    override fun post(body: PostBookRequest): ResponseEntity<BookResponse> {
        return ResponseEntity.noContent().build()
    }
}
