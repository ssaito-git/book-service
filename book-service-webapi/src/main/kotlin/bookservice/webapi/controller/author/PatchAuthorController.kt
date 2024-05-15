package bookservice.webapi.controller.author

import bookservice.core.repository.AuthorRepository
import bookservice.webapi.controller.author.dto.AuthorResponse
import bookservice.webapi.controller.author.dto.PatchAuthorRequest
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
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
    private val authorRepository: AuthorRepository,
) : PatchAuthorApi {
    override fun patch(body: PatchAuthorRequest, authorId: UUID): ResponseEntity<AuthorResponse> {
        val author = authorRepository.findById(authorId)
            .getOrThrow {
                logger.error(it)
                ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
            }

        if (author == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "著者が存在しません")
        }

        val newAuthor = author.setName(body.name ?: author.name)
            .andThen {
                it.setNameKana(body.nameKana ?: author.nameKana)
            }
            .andThen {
                if (body.birthDate != null) {
                    it.setBirthDate(body.birthDate)
                } else {
                    Ok(it)
                }
            }
            .andThen {
                if (body.deathDate != null) {
                    it.setDeathDate(body.deathDate)
                } else {
                    Ok(it)
                }
            }
            .getOrThrow {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, it)
            }

        authorRepository.save(newAuthor)

        return ResponseEntity.ok(AuthorResponse.from(newAuthor))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PatchAuthorController::class.java)
    }
}
