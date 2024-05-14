package bookservice.webapi.controller.author

import bookservice.core.repository.AuthorRepository
import bookservice.webapi.controller.author.dto.AuthorResponse
import com.github.michaelbull.result.getOrThrow
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

/**
 * 著者の取得 API 実装
 */
@RestController
class GetAuthorController(private val authorRepository: AuthorRepository) : GetAuthorApi {
    override fun getById(authorId: UUID): ResponseEntity<AuthorResponse> {
        val author = authorRepository.findById(authorId)
            .getOrThrow {
                logger.error(it)
                ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
            }

        return if (author != null) {
            ResponseEntity.ok(AuthorResponse.from(author))
        } else {
            throw ErrorResponseException(HttpStatus.NOT_FOUND)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GetAuthorController::class.java)
    }
}
