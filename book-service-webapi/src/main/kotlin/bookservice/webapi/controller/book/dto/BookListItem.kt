package bookservice.webapi.controller.book.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 書籍リストアイテム
 */
@Schema(name = "BookListItem")
data class BookListItem(
    /**
     * 書籍 のID
     */
    @Schema(required = true, description = "書籍の ID")
    val id: String,

    /**
     * 著者の ID
     */
    @Schema(required = true, description = "著者の ID")
    val authorId: String,

    /**
     * タイトル
     */
    @Schema(required = true, description = "タイトル")
    val title: String,

    /**
     * タイトルかな
     */
    @Schema(required = true, description = "タイトルかな")
    val titleKana: String,

    /**
     * 出版社名
     */
    @Schema(required = true, description = "出版社名")
    val publisherName: String,
)
