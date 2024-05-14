package bookservice.webapi.controller.author

import bookservice.webapi.controller.author.dto.AuthorResponse
import bookservice.webapi.controller.author.dto.PostAuthorRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

/**
 * 著者の登録 API
 */
@Tag(name = "Author", description = "")
interface PostAuthorApi {
    /**
     * 著者を登録する。
     *
     * @param body リクエストボディ
     * @return レスポンス
     */
    @Operation(
        summary = "著者を登録する",
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
        ],
    )
    @PostMapping(
        "/authors",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun post(
        @RequestBody @Validated body: PostAuthorRequest,
    ): ResponseEntity<AuthorResponse>
}
