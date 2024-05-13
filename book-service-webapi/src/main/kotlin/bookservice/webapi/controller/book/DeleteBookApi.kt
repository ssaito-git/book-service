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

@Tag(name = "Book", description = "")
interface DeleteBookApi {
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
        @Parameter(description = "Book ID", required = true) @PathVariable bookId: UUID,
    ): ResponseEntity<Unit>
}
