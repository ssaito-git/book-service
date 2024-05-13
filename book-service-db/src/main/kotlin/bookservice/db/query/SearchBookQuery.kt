package bookservice.db.query

import bookservice.core.entity.Book
import org.jooq.DSLContext
import java.util.UUID

/**
 * 書籍の検索クエリ
 *
 * @property dslContext DSL コンテキスト
 */
class SearchBookQuery(private val dslContext: DSLContext) {
    /**
     * 検索する。
     *
     * @param parameter 検索パラメーター
     * @return 検索結果
     */
    fun execute(parameter: Parameter): Result {
        TODO()
    }

    /**
     * 検索パラメーター
     *
     * @property title タイトル（部分一致）
     * @property publisherName 出版社名（部分一致）
     * @property authorId 著者 ID
     * @property limit 取得件数
     * @property offset オフセット
     */
    data class Parameter(
        val title: String?,
        val publisherName: String?,
        val authorId: UUID?,
        val limit: Long,
        val offset: Long,
    )

    /**
     * 検索結果
     *
     * @property books 書籍のリスト
     * @property hasMore さらに書籍が存在するか
     */
    data class Result(
        val books: List<Book>,
        val hasMore: Boolean,
    )
}
