package bookservice.webapi.controller.book

import bookservice.db.query.SearchBookQuery
import bookservice.webapi.controller.book.dto.BookListItem
import bookservice.webapi.controller.book.dto.BookListResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * 書籍のリスト取得 API 実装
 */
@RestController
class GetBookListController(private val searchBookQuery: SearchBookQuery) : GetBookListApi {
    override fun get(
        title: String?,
        publisherName: String?,
        authorId: String?,
        limit: Long,
        offset: Long,
    ): ResponseEntity<BookListResponse> {
        val parameter = SearchBookQuery.Parameter(
            title,
            publisherName,
            authorId?.let { UUID.fromString(it) },
            limit,
            offset,
        )

        val result = searchBookQuery.execute(parameter)

        val bookListItems = result.books.map {
            BookListItem(
                it.id.toString(),
                it.authorId.toString(),
                it.title,
                it.titleKana,
                it.publisherName,
            )
        }

        return ResponseEntity.ok(BookListResponse(bookListItems, result.hasMore))
    }
}
