package bookservice.webapi.controller.book

import bookservice.webapi.service.book.DeleteBookService
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
class DeleteBookController(private val deleteBookService: DeleteBookService) : DeleteBookApi {
    override fun deleteById(bookId: UUID): ResponseEntity<Unit> {
        deleteBookService.deleteBook(bookId)
            .getOrThrow {
                when (it) {
                    is DeleteBookService.InternalError -> {
                        logger.error(it.message)
                        ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
                    }
                    DeleteBookService.NotFound -> {
                        ErrorResponseException(HttpStatus.NOT_FOUND)
                    }
                }
            }

        return ResponseEntity.noContent().build()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteBookController::class.java)
    }
}
