package bookservice.webapi.controller.author

import bookservice.webapi.controller.author.dto.AuthorResponse
import bookservice.webapi.controller.author.dto.PatchAuthorRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import java.util.UUID

/**
 * 著者の更新 API
 */
@Tag(name = "Author", description = "")
interface PatchAuthorApi {
    /**
     * 著者を更新する。
     *
     * @param body リクエストボディ
     * @return レスポンス
     */
    @Operation(
        summary = "著者を更新する",
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
    @PatchMapping(
        "/authors/{authorId}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun patch(
        @RequestBody @Validated body: PatchAuthorRequest,
        @Parameter(description = "著者の ID", required = true) @PathVariable authorId: UUID,
    ): ResponseEntity<AuthorResponse>
}
