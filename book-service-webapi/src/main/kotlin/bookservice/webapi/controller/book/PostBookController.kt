package bookservice.webapi.controller.book

import bookservice.webapi.controller.book.dto.BookRequest
import bookservice.webapi.controller.book.dto.BookResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PostBookController : PostBookApi {
    override fun post(body: BookRequest): ResponseEntity<BookResponse> {
        return ResponseEntity.noContent().build()
    }
}
