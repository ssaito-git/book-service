package bookservice.webapi.controller.book

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
 * 書籍の削除 API
 */
@Tag(name = "Book", description = "")
interface DeleteBookApi {
    /**
     * 指定された ID の書籍を削除する。
     *
     * @param bookId 書籍 ID
     * @return レスポンス
     */
    @Operation(
        summary = "ID に一致する書籍を削除する",
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
    @DeleteMapping("/books/{bookId}")
    fun deleteById(
        @Parameter(description = "書籍の ID", required = true) @PathVariable bookId: UUID,
    ): ResponseEntity<Unit>
}
