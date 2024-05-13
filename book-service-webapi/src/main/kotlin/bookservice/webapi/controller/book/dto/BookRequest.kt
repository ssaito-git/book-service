package bookservice.webapi.controller.book.dto

import bookservice.core.entity.Book
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.UUID

data class BookRequest(
    @field:UUID
    val authorId: String,

    @field:NotBlank
    @field:Size(max = Book.TITLE_MAX_SIZE)
    val title: String,

    @field:NotBlank
    @field:Size(max = Book.PUBLISHER_NAME_MAX_SIZE)
    val publisherName: String,
)
