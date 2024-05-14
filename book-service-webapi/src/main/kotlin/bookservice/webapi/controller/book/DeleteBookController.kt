package bookservice.webapi.controller.book

import bookservice.core.repository.BookRepository
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getOrThrow
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

/**
 * 書籍の削除 API 実装
 */
@RestController
class DeleteBookController(private val bookRepository: BookRepository) : DeleteBookApi {
    override fun deleteById(bookId: UUID): ResponseEntity<Unit> {
        val book = bookRepository.findById(bookId)
            .getOrThrow {
                logger.error(it)
                ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
            }

        return if (book != null) {
            bookRepository.delete(book)
            ResponseEntity.noContent().build()
        } else {
            throw ErrorResponseException(HttpStatus.NOT_FOUND)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteBookController::class.java)
    }
}
