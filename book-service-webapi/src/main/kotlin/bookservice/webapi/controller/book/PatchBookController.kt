package bookservice.webapi.controller.book

import bookservice.webapi.controller.book.dto.BookResponse
import bookservice.webapi.controller.book.dto.PatchBookRequest
import bookservice.webapi.service.book.UpdateBookService
import com.github.michaelbull.result.getOrThrow
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

/**
 * 書籍の更新 API 実装
 */
@RestController
class PatchBookController(
    private val updateBookService: UpdateBookService,
) : PatchBookApi {
    override fun patch(body: PatchBookRequest, bookId: UUID): ResponseEntity<BookResponse> {
        val authorId = runCatching { UUID.fromString(body.authorId) }.getOrNull()

        val parameter = UpdateBookService.Parameter(
            bookId,
            authorId,
            body.title,
            body.titleKana,
            body.publisherName,
        )

        val updatedBook = updateBookService.updateBook(parameter)
            .getOrThrow {
                when (it) {
                    is UpdateBookService.InternalError -> {
                        logger.error(it.message)
                        ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
                    }
                    UpdateBookService.NotFound -> {
                        ResponseStatusException(HttpStatus.NOT_FOUND, "書籍が存在しません")
                    }
                    is UpdateBookService.ValidationError -> {
                        ResponseStatusException(HttpStatus.BAD_REQUEST, it.message)
                    }
                }
            }

        return ResponseEntity.ok(BookResponse.from(updatedBook))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PatchBookController::class.java)
    }
}
