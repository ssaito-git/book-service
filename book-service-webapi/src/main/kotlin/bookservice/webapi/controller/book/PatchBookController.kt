package bookservice.webapi.controller.book

import bookservice.core.repository.AuthorRepository
import bookservice.core.repository.BookRepository
import bookservice.webapi.controller.book.dto.BookResponse
import bookservice.webapi.controller.book.dto.PatchBookRequest
import com.github.michaelbull.result.andThen
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
    private val authorRepository: AuthorRepository,
    private val bookRepository: BookRepository,
) : PatchBookApi {
    override fun patch(body: PatchBookRequest, bookId: UUID): ResponseEntity<BookResponse> {
        val book = bookRepository.findById(bookId)
            .getOrThrow {
                logger.error(it)
                ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
            }

        if (book == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "書籍が存在しません")
        }

        val authorId = body.authorId?.let { authorId ->
            val author = authorRepository.findById(UUID.fromString(authorId))
                .getOrThrow {
                    logger.error(it)
                    ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
                }

            if (author == null) {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "著者が存在しません")
            }

            author.id
        }

        val newBook = book.setAuthorId(authorId ?: book.authorId)
            .andThen {
                it.setTitle(body.title ?: book.title)
            }
            .andThen {
                it.setTitleKana(body.titleKana ?: book.titleKana)
            }
            .andThen {
                it.setPublisherName(body.publisherName ?: book.publisherName)
            }
            .getOrThrow {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, it)
            }

        bookRepository.save(newBook)

        return ResponseEntity.ok(BookResponse.from(newBook))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PatchBookController::class.java)
    }
}
