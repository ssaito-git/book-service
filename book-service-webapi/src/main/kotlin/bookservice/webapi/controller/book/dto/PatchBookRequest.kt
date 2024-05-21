package bookservice.webapi.controller.book.dto

import bookservice.core.entity.Book
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.UUID
import org.openapitools.jackson.nullable.JsonNullable

/**
 * 書籍の更新リクエスト
 */
data class PatchBookRequest(
    /**
     * 著者 ID
     */
    @Schema(required = false, description = "著者 ID", type = "String")
    @field:UUID(message = "UUID として無効な形式です")
    val authorId: JsonNullable<String>,

    /**
     * タイトル
     */
    @Schema(required = false, description = "タイトル", type = "String")
    @field:Size(max = Book.TITLE_MAX_SIZE)
    val title: JsonNullable<String>,

    /**
     * タイトルかな
     */
    @Schema(required = false, description = "タイトルかな", type = "String")
    @field:Size(max = Book.TITLE_MAX_SIZE)
    val titleKana: JsonNullable<String>,

    /**
     * 出版社名
     */
    @Schema(required = false, description = "出版社名", type = "String")
    @field:Size(max = Book.PUBLISHER_NAME_MAX_SIZE)
    val publisherName: JsonNullable<String>,
)
