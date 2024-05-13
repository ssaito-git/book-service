package bookservice.webapi.controller.book.dto

import bookservice.core.entity.Book
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Book")
data class BookResponse(
    @Schema(required = true, description = "書籍の ID")
    val id: String,

    @Schema(required = true, description = "著者の ID")
    val authorId: String,

    @Schema(required = true, description = "タイトル")
    val title: String,

    @Schema(required = true, description = "出版社名")
    val publisherName: String,
) {
    companion object {
        fun from(book: Book): BookResponse {
            return BookResponse(
                book.id.toString(),
                book.authorId.toString(),
                book.title,
                book.publisherName,
            )
        }
    }
}
