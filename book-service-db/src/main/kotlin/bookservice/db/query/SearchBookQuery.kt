package bookservice.db.query

import bookservice.db.extension.add
import bookservice.db.jooq.tables.references.BOOKS
import org.jooq.DSLContext
import org.jooq.impl.DSL
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
    fun execute(parameter: Parameter): QueryResult {
        val condition = DSL.noCondition().add { c ->
            parameter.title?.let {
                c.and(BOOKS.TITLE.contains(it))
            }
        }.add { c ->
            parameter.publisherName?.let {
                c.and(BOOKS.PUBLISHER_NAME.contains(it))
            }
        }.add { c ->
            parameter.authorId?.let {
                c.and(BOOKS.AUTHOR_ID.eq(it))
            }
        }

        val count = dslContext.fetchCount(BOOKS, condition)

        val rows = dslContext.selectFrom(BOOKS)
            .where(condition)
            .offset(parameter.offset)
            .limit(parameter.limit)
            .fetchArray()
            .map {
                ResultRow(
                    it.id,
                    it.authorId,
                    it.title,
                    it.titleKana,
                    it.publisherName,
                )
            }

        val hasMore = parameter.offset + parameter.limit < count

        return QueryResult(rows, hasMore)
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
    data class QueryResult(
        val books: List<ResultRow>,
        val hasMore: Boolean,
    )

    /**
     * レコード
     *
     * @property id 書籍 ID
     * @property authorId 著者 ID
     * @property title タイトル
     * @property titleKana タイトル（かな）
     * @property publisherName 発行者名
     */
    data class ResultRow(
        val id: UUID,
        val authorId: UUID,
        val title: String,
        val titleKana: String,
        val publisherName: String,
    )
}
