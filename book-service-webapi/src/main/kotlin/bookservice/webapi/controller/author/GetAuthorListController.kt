package bookservice.webapi.controller.author

import bookservice.db.query.SearchAuthorQuery
import bookservice.webapi.controller.author.dto.AuthorListItem
import bookservice.webapi.controller.author.dto.AuthorListResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

/**
 * 著者のリスト取得 API 実装
 */
@RestController
class GetAuthorListController(private val searchAuthorQuery: SearchAuthorQuery) : GetAuthorListApi {
    override fun get(name: String?, limit: Long, offset: Long): ResponseEntity<AuthorListResponse> {
        val parameter = SearchAuthorQuery.Parameter(name, limit, offset)

        val result = searchAuthorQuery.execute(parameter)

        val authorListItems = result.authors.map {
            AuthorListItem(
                it.id.toString(),
                it.name,
                it.nameKana,
                it.birthData,
                it.deathData,
            )
        }

        return ResponseEntity.ok(AuthorListResponse(authorListItems, result.hasMore))
    }
}
