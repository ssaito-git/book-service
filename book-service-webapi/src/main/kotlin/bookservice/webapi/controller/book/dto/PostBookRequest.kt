package bookservice.webapi.controller.book.dto

import bookservice.core.entity.Book
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.UUID

/**
 * 書籍の作成リクエスト
 */
data class PostBookRequest(
    /**
     * 著者 ID
     */
    @Schema(required = true, description = "著者 ID")
    @field:UUID
    val authorId: String,

    /**
     * タイトル
     */
    @Schema(required = true, description = "タイトル")
    @field:NotBlank
    @field:Size(max = Book.TITLE_MAX_SIZE)
    val title: String,

    /**
     * タイトルかな
     */
    @Schema(required = true, description = "タイトルかな")
    @field:NotBlank
    @field:Size(max = Book.TITLE_MAX_SIZE)
    val titleKana: String,

    /**
     * 出版社名
     */
    @Schema(required = true, description = "出版社名")
    @field:NotBlank
    @field:Size(max = Book.PUBLISHER_NAME_MAX_SIZE)
    val publisherName: String,
)
