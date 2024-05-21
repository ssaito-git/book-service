package bookservice.webapi.controller.author

import bookservice.webapi.controller.author.dto.AuthorResponse
import bookservice.webapi.controller.author.dto.PatchAuthorRequest
import bookservice.webapi.extension.toUndefinable
import bookservice.webapi.service.author.PartialUpdateAuthorService
import com.github.michaelbull.result.getOrThrow
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

/**
 * 著者の更新 API 実装
 */
@RestController
class PatchAuthorController(
    private val partialUpdateAuthorService: PartialUpdateAuthorService,
) : PatchAuthorApi {
    override fun patch(body: PatchAuthorRequest, authorId: UUID): ResponseEntity<AuthorResponse> {
        val parameter = PartialUpdateAuthorService.Parameter(
            authorId,
            body.name.toUndefinable(),
            body.nameKana.toUndefinable(),
            body.birthDate.toUndefinable(),
            body.deathDate.toUndefinable(),
        )

        val author = partialUpdateAuthorService.partialUpdateAuthor(parameter)
            .getOrThrow {
                when (it) {
                    is PartialUpdateAuthorService.InternalError -> {
                        logger.error(it.message)
                        ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
                    }
                    PartialUpdateAuthorService.NotFound -> {
                        ResponseStatusException(HttpStatus.NOT_FOUND, "著者が存在しません")
                    }
                    is PartialUpdateAuthorService.ValidationError -> {
                        ResponseStatusException(HttpStatus.BAD_REQUEST, it.message)
                    }
                }
            }

        return ResponseEntity.ok(AuthorResponse.from(author))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PatchAuthorController::class.java)
    }
}
