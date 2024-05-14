package bookservice.webapi.controller.author

import bookservice.webapi.controller.author.dto.AuthorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.UUID

/**
 * 著者の取得 API
 */
@Tag(name = "Author", description = "")
interface GetAuthorApi {
    /**
     * 指定した ID の著者を取得する。
     *
     * @param bookId 著者 ID
     * @return レスポンス
     */
    @Operation(
        summary = "ID に一致する著者を取得する",
        description = "",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "",
                content = [Content(schema = Schema(implementation = AuthorResponse::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "",
                content = [Content()],
            ),
            ApiResponse(
                responseCode = "404",
                description = "",
                content = [Content()],
            ),
        ],
    )
    @GetMapping("/authors/{authorId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getById(
        @Parameter(description = "著者の ID", required = true) @PathVariable authorId: UUID,
    ): ResponseEntity<AuthorResponse>
}
