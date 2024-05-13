package bookservice.webapi.controller.book

import bookservice.db.repository.BookRepositoryImpl
import bookservice.webapi.controller.book.dto.BookResponse
import com.github.michaelbull.result.mapBoth
import org.jooq.DSLContext
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class GetBookController(private val dslContext: DSLContext) : GetBookApi {
    override fun getById(bookId: UUID): ResponseEntity<BookResponse> {
        val bookRepository = BookRepositoryImpl(dslContext)

        return bookRepository.findById(bookId)
            .mapBoth(
                {
                    if (it != null) {
                        ResponseEntity.ok(BookResponse.from(it))
                    } else {
                        throw ErrorResponseException(HttpStatus.NOT_FOUND)
                    }
                },
                {
                    throw ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR)
                },
            )
    }
}
