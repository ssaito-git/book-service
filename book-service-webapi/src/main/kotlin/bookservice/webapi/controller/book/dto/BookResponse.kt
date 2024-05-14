package bookservice.webapi.controller.book.dto

import bookservice.core.entity.Book
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 書籍のレスポンス
 */
@Schema(name = "Book")
data class BookResponse(
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
     * タイトル
     */
    @Schema(required = true, description = "タイトル")
    val titleKana: String,

    /**
     * 出版社名
     */
    @Schema(required = true, description = "出版社名")
    val publisherName: String,
) {
    companion object {
        /**
         * 書籍エンティティからレスポンスを生成する。
         *
         * @param book 書籍
         * @return レスポンス
         */
        fun from(book: Book): BookResponse {
            return BookResponse(
                book.id.toString(),
                book.authorId.toString(),
                book.title,
                book.titleKana,
                book.publisherName,
            )
        }
    }
}
