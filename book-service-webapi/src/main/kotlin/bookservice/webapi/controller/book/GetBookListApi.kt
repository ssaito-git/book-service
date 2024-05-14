package bookservice.webapi.controller.book

import bookservice.webapi.controller.book.dto.BookListResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Min
import org.hibernate.validator.constraints.Range
import org.hibernate.validator.constraints.UUID
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * 書籍のリスト取得 API
 */
@Tag(name = "Book", description = "")
interface GetBookListApi {
    /**
     * 指定した条件で書籍のリストを取得する。
     *
     * @param title タイトル
     * @param publisherName 出版社名
     * @param authorId 著者 ID
     * @param limit 取得件数
     * @param offset オフセット
     * @return レスポンス
     */
    @Operation(
        summary = "書籍のリストを取得する",
        description = "",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "",
                content = [Content(schema = Schema(implementation = BookListResponse::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "",
                content = [Content()],
            ),
        ],
    )
    @GetMapping("/books", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(
        @Parameter(
            description = "タイトル（部分一致）",
        ) @RequestParam(required = false) title: String?,
        @Parameter(
            description = "発行者名（部分一致）",
        ) @RequestParam(required = false) publisherName: String?,
        @Parameter(
            description = "著者 ID",
        ) @RequestParam(required = false) @UUID authorId: String?,
        @Parameter(
            description = "取得する書籍の件数",
        ) @RequestParam(defaultValue = "30", required = false) @Range(min = 1, max = 50) limit: Int,
        @Parameter(
            description = "オフセット",
        ) @RequestParam(defaultValue = "0", required = false) @Min(0) offset: Int,
    ): ResponseEntity<BookListResponse>
}
