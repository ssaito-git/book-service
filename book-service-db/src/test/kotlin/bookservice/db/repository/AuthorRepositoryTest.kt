package bookservice.db.repository

import bookservice.core.entity.Author
import bookservice.db.AbstractDbTest
import com.github.database.rider.core.api.configuration.DBUnit
import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.core.api.dataset.ExpectedDataSet
import com.github.database.rider.junit5.api.DBRider
import com.github.michaelbull.result.mapBoth
import org.jooq.impl.DSL
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.time.LocalDate
import java.util.UUID

@DBRider
@DBUnit(caseSensitiveTableNames = true)
class AuthorRepositoryTest : AbstractDbTest() {
    @Test
    @DataSet("datasets/authors/authors.yml")
    fun `登録されている著者情報を取得する`() {
        val authorRepository = AuthorRepositoryImpl(DSL.using(connectionHolder.connection))

        authorRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))
            .mapBoth(
                {
                    assertEquals(
                        Author(
                            UUID.fromString("00000000-0000-0000-0000-000000000001"),
                            "著者 1",
                            "ちょしゃ 1",
                            LocalDate.parse("1800-01-01"),
                            LocalDate.parse("1900-01-01"),
                        ),
                        it,
                    )
                },
                {
                    fail(it)
                },
            )
    }

    @Test
    @DataSet("datasets/authors/authors.yml")
    fun `登録されていない著者情報を取得する`() {
        val authorRepository = AuthorRepositoryImpl(DSL.using(connectionHolder.connection))

        authorRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"))
            .mapBoth(
                {
                    assertEquals(
                        null,
                        it,
                    )
                },
                {
                    fail(it)
                },
            )
    }

    @Test
    @DataSet("datasets/authors/authors.yml")
    @ExpectedDataSet("datasets/authors/expectedInsertAuthors.yml")
    fun `新しい著者を登録する`() {
        val authorRepository = AuthorRepositoryImpl(DSL.using(connectionHolder.connection))

        val author = Author.create(
            UUID.fromString("00000000-0000-0000-0000-000000000003"),
            "著者 3",
            "ちょしゃ 3",
            LocalDate.parse("1800-01-01"),
            LocalDate.parse("1900-01-01"),
        )

        authorRepository.save(author.value)
    }

    @Test
    @DataSet("datasets/authors/authors.yml")
    @ExpectedDataSet("datasets/authors/expectedUpdateAuthors.yml")
    fun `著者を更新する`() {
        val authorRepository = AuthorRepositoryImpl(DSL.using(connectionHolder.connection))

        val author = Author.create(
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            "著者 1 更新",
            "ちょしゃ 1 こうしん",
            LocalDate.parse("1850-12-31"),
            null,
        ).value

        authorRepository.save(author)
    }

    @Test
    @DataSet("datasets/authors/authors.yml")
    @ExpectedDataSet("datasets/authors/expectedDeleteAuthors.yml")
    fun `著者を削除する`() {
        val authorRepository = AuthorRepositoryImpl(DSL.using(connectionHolder.connection))

        val author = Author.create(
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            "著者 1",
            "ちょしゃ 1",
            LocalDate.parse("1800-01-01"),
            LocalDate.parse("1900-01-01"),
        ).value

        authorRepository.delete(author)
    }
}
