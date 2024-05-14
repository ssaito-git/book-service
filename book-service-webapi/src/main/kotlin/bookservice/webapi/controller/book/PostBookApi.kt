package bookservice.webapi.controller.book

import bookservice.webapi.controller.book.dto.BookResponse
import bookservice.webapi.controller.book.dto.PostBookRequest
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
 * 書籍の登録 API
 */
@Tag(name = "Book", description = "")
interface PostBookApi {
    /**
     * 書籍を登録する。
     *
     * @param body リクエストボディ
     * @return レスポンス
     */
    @Operation(
        summary = "新しい書籍を登録する",
        description = "",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "",
                content = [Content(schema = Schema(implementation = BookResponse::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "",
                content = [Content()],
            ),
        ],
    )
    @PostMapping(
        "/books",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun post(
        @RequestBody @Validated body: PostBookRequest,
    ): ResponseEntity<BookResponse>
}
