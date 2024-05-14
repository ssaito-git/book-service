package bookservice.webapi.controller.author

import bookservice.core.entity.Author
import bookservice.core.repository.AuthorRepository
import bookservice.webapi.controller.author.dto.AuthorResponse
import bookservice.webapi.controller.author.dto.PostAuthorRequest
import com.github.michaelbull.result.getOrThrow
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

/**
 * 書籍の登録 API 実装
 */
@RestController
class PostAuthorController(private val authorRepository: AuthorRepository) : PostAuthorApi {
    override fun post(body: PostAuthorRequest): ResponseEntity<AuthorResponse> {
        val author = Author.create(
            UUID.randomUUID(),
            body.name,
            body.nameKana,
            body.birthDate,
            body.deathDate,
        ).getOrThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST, it)
        }

        authorRepository.save(author)

        return ResponseEntity.ok(AuthorResponse.from(author))
    }
}
