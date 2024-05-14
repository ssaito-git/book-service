package bookservice.webapi.controller.book

import bookservice.core.repository.BookRepository
import bookservice.webapi.controller.book.dto.BookResponse
import com.github.michaelbull.result.getOrThrow
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

/**
 * 書籍の取得 API 実装
 */
@RestController
class GetBookController(private val bookRepository: BookRepository) : GetBookApi {
    override fun getById(bookId: UUID): ResponseEntity<BookResponse> {
        val book = bookRepository.findById(bookId)
            .getOrThrow {
                logger.error(it)
                ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
            }

        return if (book != null) {
            ResponseEntity.ok(BookResponse.from(book))
        } else {
            throw ErrorResponseException(HttpStatus.NOT_FOUND)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GetBookController::class.java)
    }
}
