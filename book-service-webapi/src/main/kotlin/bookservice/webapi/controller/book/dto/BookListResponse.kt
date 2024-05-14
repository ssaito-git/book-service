package bookservice.webapi.controller.book.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 書籍リストレスポンス
 */
@Schema(name = "BookList")
data class BookListResponse(
    /**
     * 書籍のリスト
     */
    @Schema(required = true, description = "書籍のリスト")
    val books: List<BookListItem>,

    /**
     * さらに書籍が存在するか
     */
    @Schema(required = true, description = "さらに書籍が存在するか")
    val hasMore: Boolean,
)
