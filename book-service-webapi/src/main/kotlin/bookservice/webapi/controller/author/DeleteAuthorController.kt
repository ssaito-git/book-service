package bookservice.webapi.controller.author

import bookservice.webapi.service.author.DeleteAuthorService
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
class DeleteAuthorController(private val deleteAuthorService: DeleteAuthorService) : DeleteAuthorApi {
    override fun deleteById(authorId: UUID): ResponseEntity<Unit> {
        deleteAuthorService.deleteAuthor(authorId)
            .getOrThrow {
                when (it) {
                    is DeleteAuthorService.InternalError -> {
                        logger.error(it.message)
                        ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
                    }
                    DeleteAuthorService.NotFound -> {
                        ErrorResponseException(HttpStatus.NOT_FOUND)
                    }
                }
            }

        return ResponseEntity.noContent().build()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteAuthorController::class.java)
    }
}
