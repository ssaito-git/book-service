package bookservice.db.query

import bookservice.db.AbstractDbTest
import com.github.database.rider.core.api.configuration.DBUnit
import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.junit5.api.DBRider
import org.jooq.impl.DSL
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

@DBRider
@DBUnit(caseSensitiveTableNames = true)
class SearchBookQueryTest : AbstractDbTest() {
    @Test
    @DataSet("datasets/query/search_book_query/books.yml")
    fun `条件指定なし。さらに書籍が存在する`() {
        val query = SearchBookQuery(DSL.using(connectionHolder.connection))

        val parameter = SearchBookQuery.Parameter(
            null,
            null,
            null,
            3,
            0,
        )

        val result = query.execute(parameter)

        assertEquals(
            SearchBookQuery.QueryResult(
                listOf(
                    resultRowTemplate(1, 1),
                    resultRowTemplate(2, 2),
                    resultRowTemplate(3, 1),
                ),
                true,
            ),
            result,
        )
    }

    @Test
    @DataSet("datasets/query/search_book_query/books.yml")
    fun `条件指定なし。さらに書籍が存在しない`() {
        val query = SearchBookQuery(DSL.using(connectionHolder.connection))

        val parameter = SearchBookQuery.Parameter(
            null,
            null,
            null,
            3,
            27,
        )

        val result = query.execute(parameter)

        assertEquals(
            SearchBookQuery.QueryResult(
                listOf(
                    resultRowTemplate(28, 2),
                    resultRowTemplate(29, 1),
                    resultRowTemplate(30, 2),
                ),
                false,
            ),
            result,
        )
    }

    @Test
    @DataSet("datasets/query/search_book_query/books.yml")
    fun `タイトルのみ指定`() {
        val query = SearchBookQuery(DSL.using(connectionHolder.connection))

        val parameter = SearchBookQuery.Parameter(
            "タイトル 2",
            null,
            null,
            3,
            0,
        )

        val result = query.execute(parameter)

        assertEquals(
            SearchBookQuery.QueryResult(
                listOf(
                    resultRowTemplate(2, 2),
                    resultRowTemplate(20, 2),
                    resultRowTemplate(21, 1),
                ),
                true,
            ),
            result,
        )
    }

    @Test
    @DataSet("datasets/query/search_book_query/books.yml")
    fun `出版社名のみ指定`() {
        val query = SearchBookQuery(DSL.using(connectionHolder.connection))

        val parameter = SearchBookQuery.Parameter(
            null,
            "出版社 3",
            null,
            3,
            0,
        )

        val result = query.execute(parameter)

        assertEquals(
            SearchBookQuery.QueryResult(
                listOf(
                    resultRowTemplate(3, 1),
                    resultRowTemplate(30, 2),
                ),
                false,
            ),
            result,
        )
    }

    @Test
    @DataSet("datasets/query/search_book_query/books.yml")
    fun `著者 ID のみ指定`() {
        val query = SearchBookQuery(DSL.using(connectionHolder.connection))

        val parameter = SearchBookQuery.Parameter(
            null,
            null,
            UUID.fromString("00000000-0000-0000-0000-000000000002"),
            3,
            0,
        )

        val result = query.execute(parameter)

        assertEquals(
            SearchBookQuery.QueryResult(
                listOf(
                    resultRowTemplate(2, 2),
                    resultRowTemplate(4, 2),
                    resultRowTemplate(6, 2),
                ),
                true,
            ),
            result,
        )
    }

    @Test
    @DataSet("datasets/query/search_book_query/books.yml")
    fun `すべての条件を指定`() {
        val query = SearchBookQuery(DSL.using(connectionHolder.connection))

        val parameter = SearchBookQuery.Parameter(
            "タイトル 2",
            "出版社 2",
            UUID.fromString("00000000-0000-0000-0000-000000000002"),
            3,
            0,
        )

        val result = query.execute(parameter)

        assertEquals(
            SearchBookQuery.QueryResult(
                listOf(
                    resultRowTemplate(2, 2),
                    resultRowTemplate(20, 2),
                    resultRowTemplate(22, 2),
                ),
                true,
            ),
            result,
        )
    }

    @Test
    @DataSet("datasets/query/search_book_query/books.yml")
    fun `条件に一致する書籍が存在しない`() {
        val query = SearchBookQuery(DSL.using(connectionHolder.connection))

        val parameter = SearchBookQuery.Parameter(
            "存在しないタイトル",
            "存在しない出版社",
            UUID.fromString("00000000-0000-0000-0000-000000000003"),
            5,
            0,
        )

        val result = query.execute(parameter)

        assertEquals(
            SearchBookQuery.QueryResult(
                emptyList(),
                false,
            ),
            result,
        )
    }

    private fun resultRowTemplate(bookNumber: Int, authorNumber: Int) = SearchBookQuery.ResultRow(
        UUID.fromString("00000000-0000-0000-0000-${bookNumber.toString().padStart(12, '0')}"),
        UUID.fromString("00000000-0000-0000-0000-${authorNumber.toString().padStart(12, '0')}"),
        "タイトル $bookNumber",
        "たいとる $bookNumber",
        "出版社 $bookNumber",
    )
}
