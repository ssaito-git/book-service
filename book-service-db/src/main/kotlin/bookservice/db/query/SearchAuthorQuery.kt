package bookservice.db.query

import bookservice.core.entity.Author
import org.jooq.DSLContext
import java.util.UUID

/**
 * 著者の検索クエリ
 *
 * @property dslContext DSL コンテキスト
 */
class SearchAuthorQuery(private val dslContext: DSLContext) {
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
     * @property authorId 著者 ID
     * @property name 著者名（部分一致）
     * @property limit 取得件数
     * @property offset オフセット
     */
    data class Parameter(
        val authorId: UUID?,
        val name: String,
        val limit: Long,
        val offset: Long,
    )

    /**
     * 検索結果
     *
     * @property authors 著者のリスト
     * @property hasMore さらに著者が存在するか
     */
    data class Result(
        val authors: List<Author>,
        val hasMore: Boolean,
    )
}
