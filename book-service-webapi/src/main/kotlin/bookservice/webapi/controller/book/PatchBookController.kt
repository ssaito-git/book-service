package bookservice.webapi.controller.book

import bookservice.webapi.controller.book.dto.BookResponse
import bookservice.webapi.controller.book.dto.PatchBookRequest
import bookservice.webapi.extension.toUndefinable
import bookservice.webapi.service.book.PartialUpdateBookService
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
    private val updateBookService: PartialUpdateBookService,
) : PatchBookApi {
    override fun patch(body: PatchBookRequest, bookId: UUID): ResponseEntity<BookResponse> {
        val authorId = body.authorId.toUndefinable().map {
            runCatching { UUID.fromString(it) }.getOrElse {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, it.message)
            }
        }

        val parameter = PartialUpdateBookService.Parameter(
            bookId,
            authorId,
            body.title.toUndefinable(),
            body.titleKana.toUndefinable(),
            body.publisherName.toUndefinable(),
        )

        val updatedBook = updateBookService.updateBook(parameter)
            .getOrThrow {
                when (it) {
                    is PartialUpdateBookService.InternalError -> {
                        logger.error(it.message)
                        ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
                    }
                    PartialUpdateBookService.NotFound -> {
                        ResponseStatusException(HttpStatus.NOT_FOUND, "書籍が存在しません")
                    }
                    is PartialUpdateBookService.ValidationError -> {
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
