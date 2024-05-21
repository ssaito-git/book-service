package bookservice.webapi.controller.author

import bookservice.webapi.controller.author.dto.AuthorResponse
import bookservice.webapi.controller.author.dto.PostAuthorRequest
import bookservice.webapi.service.author.CreateAuthorService
import com.github.michaelbull.result.getOrThrow
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

/**
 * 書籍の登録 API 実装
 */
@RestController
class PostAuthorController(private val createAuthorService: CreateAuthorService) : PostAuthorApi {
    override fun post(body: PostAuthorRequest): ResponseEntity<AuthorResponse> {
        val parameter = CreateAuthorService.Parameter(
            body.name,
            body.nameKana,
            body.birthDate,
            body.deathDate,
        )

        val author = createAuthorService.createAuthor(parameter)
            .getOrThrow {
                when (it) {
                    is CreateAuthorService.ValidationError -> {
                        ResponseStatusException(HttpStatus.BAD_REQUEST, it.message)
                    }
                }
            }

        return ResponseEntity.ok(AuthorResponse.from(author))
    }
}
