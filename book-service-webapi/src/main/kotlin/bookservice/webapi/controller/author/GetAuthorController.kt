package bookservice.webapi.controller.author

import bookservice.webapi.controller.author.dto.AuthorResponse
import bookservice.webapi.service.author.GetAuthorService
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
class GetAuthorController(private val getAuthorService: GetAuthorService) : GetAuthorApi {
    override fun getById(authorId: UUID): ResponseEntity<AuthorResponse> {
        val author = getAuthorService.getAuthor(authorId)
            .getOrThrow {
                when (it) {
                    is GetAuthorService.InternalError -> {
                        logger.error(it.message)
                        ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
                    }

                    GetAuthorService.NotFound -> {
                        ErrorResponseException(HttpStatus.NOT_FOUND)
                    }
                }
            }

        return ResponseEntity.ok(AuthorResponse.from(author))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GetAuthorController::class.java)
    }
}
