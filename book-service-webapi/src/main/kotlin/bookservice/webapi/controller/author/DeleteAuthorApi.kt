package bookservice.webapi.controller.author

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.UUID

/**
 * 著者の削除 API
 */
@Tag(name = "Author", description = "")
interface DeleteAuthorApi {
    /**
     * 指定された ID の著者を削除する。
     *
     * @param authorId 著者 ID
     * @return レスポンス
     */
    @Operation(
        summary = "ID に一致する著者を削除する",
        description = "",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "",
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
    @DeleteMapping("/authors/{authorId}")
    fun deleteById(
        @Parameter(description = "著者の ID", required = true) @PathVariable authorId: UUID,
    ): ResponseEntity<Unit>
}
