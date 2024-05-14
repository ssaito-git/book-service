package bookservice.webapi.controller.author

import bookservice.core.repository.AuthorRepository
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
class DeleteAuthorController(private val authorRepository: AuthorRepository) : DeleteAuthorApi {
    override fun deleteById(authorId: UUID): ResponseEntity<Unit> {
        val author = authorRepository.findById(authorId)
            .getOrThrow {
                logger.error(it)
                ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
            }

        return if (author != null) {
            authorRepository.delete(author)
            ResponseEntity.noContent().build()
        } else {
            throw ErrorResponseException(HttpStatus.NOT_FOUND)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteAuthorController::class.java)
    }
}
