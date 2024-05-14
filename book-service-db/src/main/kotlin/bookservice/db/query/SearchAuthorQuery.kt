package bookservice.db.query

import bookservice.db.extension.add
import bookservice.db.jooq.tables.references.AUTHORS
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.UUID

/**
 * 著者の検索クエリ
 *
 * @property dslContext DSL コンテキスト
 */
@Component
class SearchAuthorQuery(private val dslContext: DSLContext) {
    /**
     * 検索する。
     *
     * @param parameter 検索パラメーター
     * @return 検索結果
     */
    fun execute(parameter: Parameter): QueryResult {
        val condition = DSL.noCondition().add { c ->
            parameter.name?.let {
                c.and(AUTHORS.NAME.contains(it))
            }
        }

        val count = dslContext.fetchCount(AUTHORS, condition)

        val rows = dslContext.selectFrom(AUTHORS)
            .where(condition)
            .offset(parameter.offset)
            .limit(parameter.limit)
            .fetchArray()
            .map {
                ResultRow(
                    it.id,
                    it.name,
                    it.nameKana,
                    it.birthDate,
                    it.deathDate,
                )
            }

        val hasMore = parameter.offset + parameter.limit < count

        return QueryResult(rows, hasMore)
    }

    /**
     * 検索パラメーター
     *
     * @property name 著者名（部分一致）
     * @property limit 取得件数
     * @property offset オフセット
     */
    data class Parameter(
        val name: String?,
        val limit: Long,
        val offset: Long,
    )

    /**
     * 検索結果
     *
     * @property authors 著者のリスト
     * @property hasMore さらに著者が存在するか
     */
    data class QueryResult(
        val authors: List<ResultRow>,
        val hasMore: Boolean,
    )

    /**
     * レコード
     *
     * @property id 著者 ID
     * @property name 名前
     * @property nameKana 名前（かな）
     * @property birthData 生年月日。不明の場合は null。
     * @property deathData 没年月日。不明の場合は null。
     */
    data class ResultRow(
        val id: UUID,
        val name: String,
        val nameKana: String,
        val birthData: LocalDate?,
        val deathData: LocalDate?,
    )
}
