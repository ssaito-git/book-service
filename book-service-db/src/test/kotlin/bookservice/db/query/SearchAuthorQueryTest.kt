import bookservice.db.AbstractDbTest
import bookservice.db.query.SearchAuthorQuery
import com.github.database.rider.core.api.configuration.DBUnit
import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.junit5.api.DBRider
import org.jooq.impl.DSL
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

@DBRider
@DBUnit(caseSensitiveTableNames = true)
class SearchAuthorQueryTest : AbstractDbTest() {
    @Test
    @DataSet("datasets/query/search_author_query/authors.yml")
    fun `条件指定なし。さらに著者が存在する`() {
        val query = SearchAuthorQuery(DSL.using(connectionHolder.connection))

        val parameter = SearchAuthorQuery.Parameter(
            null,
            3,
            0,
        )

        val result = query.execute(parameter)

        assertEquals(
            SearchAuthorQuery.QueryResult(
                listOf(
                    resultRowTemplate(1, "1800-01-01", null),
                    resultRowTemplate(2, "1800-01-02", "1900-01-02"),
                    resultRowTemplate(3, null, "1900-01-03"),
                ),
                true,
            ),
            result,
        )
    }

    @Test
    @DataSet("datasets/query/search_author_query/authors.yml")
    fun `条件指定なし。さらに著者が存在しない`() {
        val query = SearchAuthorQuery(DSL.using(connectionHolder.connection))

        val parameter = SearchAuthorQuery.Parameter(
            null,
            3,
            27,
        )

        val result = query.execute(parameter)

        assertEquals(
            SearchAuthorQuery.QueryResult(
                listOf(
                    resultRowTemplate(28, null, null),
                    resultRowTemplate(29, "1800-01-29", null),
                    resultRowTemplate(30, "1800-01-30", "1900-01-30"),
                ),
                false,
            ),
            result,
        )
    }

    @Test
    @DataSet("datasets/query/search_author_query/authors.yml")
    fun `著者名を指定`() {
        val query = SearchAuthorQuery(DSL.using(connectionHolder.connection))

        val parameter = SearchAuthorQuery.Parameter(
            "著者 2",
            3,
            0,
        )

        val result = query.execute(parameter)

        assertEquals(
            SearchAuthorQuery.QueryResult(
                listOf(
                    resultRowTemplate(2, "1800-01-02", "1900-01-02"),
                    resultRowTemplate(20, null, null),
                    resultRowTemplate(21, "1800-01-21", null),
                ),
                true,
            ),
            result,
        )
    }

    @Test
    @DataSet("datasets/query/search_author_query/authors.yml")
    fun `条件に一致する著者が存在しない`() {
        val query = SearchAuthorQuery(DSL.using(connectionHolder.connection))

        val parameter = SearchAuthorQuery.Parameter(
            "存在しない著者",
            3,
            0,
        )

        val result = query.execute(parameter)

        assertEquals(
            SearchAuthorQuery.QueryResult(
                emptyList(),
                false,
            ),
            result,
        )
    }

    private fun resultRowTemplate(
        authorNumber: Int,
        birthDate: String?,
        deathDate: String?,
    ) = SearchAuthorQuery.ResultRow(
        UUID.fromString("00000000-0000-0000-0000-${authorNumber.toString().padStart(12, '0')}"),
        "著者 $authorNumber",
        "ちょしゃ $authorNumber",
        if (birthDate != null) { LocalDate.parse(birthDate) } else { null },
        if (deathDate != null) { LocalDate.parse(deathDate) } else { null },
    )
}
