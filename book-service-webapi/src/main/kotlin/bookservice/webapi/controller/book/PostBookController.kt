package bookservice.webapi.controller.book

import bookservice.webapi.controller.book.dto.BookResponse
import bookservice.webapi.controller.book.dto.PostBookRequest
import bookservice.webapi.service.book.CreateBookService
import com.github.michaelbull.result.getOrThrow
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

/**
 * 書籍の登録 API 実装
 */
@RestController
class PostBookController(private val createBookService: CreateBookService) : PostBookApi {
    override fun post(body: PostBookRequest): ResponseEntity<BookResponse> {
        val authorId = UUID.fromString(body.authorId)

        val parameter = CreateBookService.Parameter(
            authorId,
            body.title,
            body.titleKana,
            body.publisherName,
        )

        val book = createBookService.createBook(parameter)
            .getOrThrow {
                when (it) {
                    is CreateBookService.InternalError -> {
                        logger.error(it.message)
                        ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
                    }

                    is CreateBookService.ValidationError -> {
                        ResponseStatusException(HttpStatus.BAD_REQUEST, it.message)
                    }
                }
            }

        return ResponseEntity.ok(BookResponse.from(book))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PostBookController::class.java)
    }
}
