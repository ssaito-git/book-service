package bookservice.webapi.controller.book.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "BookList")
data class BookListResponse(
    @Schema(required = true, description = "書籍のリスト")
    val products: List<BookResponse>,

    @Schema(required = true, description = "さらにプロダクトが存在するか")
    val hasMore: Boolean,
)
