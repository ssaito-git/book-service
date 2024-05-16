package bookservice.webapi.controller.book

import bookservice.webapi.controller.book.dto.BookResponse
import bookservice.webapi.service.book.GetBookService
import com.github.michaelbull.result.getOrThrow
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

/**
 * 書籍の取得 API 実装
 */
@RestController
class GetBookController(private val getBookService: GetBookService) : GetBookApi {
    override fun getById(bookId: UUID): ResponseEntity<BookResponse> {
        val book = getBookService.getBook(bookId).getOrThrow {
            when (it) {
                is GetBookService.InternalError -> {
                    logger.error(it.message)
                    ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
                }
                GetBookService.NotFound -> {
                    ResponseStatusException(HttpStatus.NOT_FOUND)
                }
            }
        }

        return ResponseEntity.ok(BookResponse.from(book))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GetBookController::class.java)
    }
}
