package bookservice.db.repository

import bookservice.core.entity.Book
import com.github.database.rider.core.api.configuration.DBUnit
import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.core.api.dataset.ExpectedDataSet
import com.github.database.rider.junit5.api.DBRider
import com.github.michaelbull.result.mapBoth
import org.jooq.impl.DSL
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.util.UUID

@DBRider
@DBUnit(caseSensitiveTableNames = true)
class BookRepositoryTest : AbstractDbTest() {
    @Test
    @DataSet("datasets/books/books.yml")
    fun `登録されている書籍情報を取得する`() {
        val bookRepository = BookRepositoryImpl(DSL.using(connectionHolder.connection))

        bookRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000002"))
            .mapBoth(
                {
                    assertEquals(
                        Book(
                            UUID.fromString("00000000-0000-0000-0000-000000000002"),
                            UUID.fromString("00000000-0000-0000-0000-000000000001"),
                            "タイトル 2",
                            "出版社 2",
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
    @DataSet("datasets/books/books.yml")
    fun `登録されていない書籍情報を取得する`() {
        val bookRepository = BookRepositoryImpl(DSL.using(connectionHolder.connection))

        bookRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"))
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
    @DataSet("datasets/books/books.yml")
    @ExpectedDataSet("datasets/books/expectedInsertBooks.yml")
    fun `新しい書籍を登録する`() {
        val bookRepository = BookRepositoryImpl(DSL.using(connectionHolder.connection))

        val book = Book.create(
            UUID.fromString("00000000-0000-0000-0000-000000000003"),
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            "タイトル 3",
            "出版社 3",
        )

        bookRepository.save(book.value)
    }

    @Test
    @DataSet("datasets/books/books.yml")
    @ExpectedDataSet("datasets/books/expectedUpdateBooks.yml")
    fun `書籍を更新する`() {
        val bookRepository = BookRepositoryImpl(DSL.using(connectionHolder.connection))

        val book = Book.create(
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            UUID.fromString("00000000-0000-0000-0000-000000000002"),
            "タイトル 1 更新",
            "出版社 1 更新",
        )

        bookRepository.save(book.value)
    }

    @Test
    @DataSet("datasets/books/books.yml")
    @ExpectedDataSet("datasets/books/expectedDeleteBook.yml")
    fun `書籍を削除する`() {
        val bookRepository = BookRepositoryImpl(DSL.using(connectionHolder.connection))

        val book = Book.create(
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            "タイトル 1",
            "出版社 1",
        )

        bookRepository.delete(book.value)
    }
}
