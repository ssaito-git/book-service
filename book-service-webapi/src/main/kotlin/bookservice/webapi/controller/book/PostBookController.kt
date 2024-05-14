package bookservice.webapi.controller.book

import bookservice.core.entity.Book
import bookservice.core.repository.AuthorRepository
import bookservice.core.repository.BookRepository
import bookservice.webapi.controller.book.dto.BookResponse
import bookservice.webapi.controller.book.dto.PostBookRequest
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
class PostBookController(
    private val authorRepository: AuthorRepository,
    private val bookRepository: BookRepository,
) : PostBookApi {
    override fun post(body: PostBookRequest): ResponseEntity<BookResponse> {
        val author = authorRepository.findById(UUID.fromString(body.authorId))
            .getOrThrow {
                logger.error(it)
                ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
            }

        if (author == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "著者が存在しません")
        }

        val book = Book.create(
            UUID.randomUUID(),
            author.id,
            body.title,
            body.titleKana,
            body.publisherName,
        ).getOrThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST, it)
        }

        bookRepository.save(book)

        return ResponseEntity.ok(BookResponse.from(book))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PostBookController::class.java)
    }
}
