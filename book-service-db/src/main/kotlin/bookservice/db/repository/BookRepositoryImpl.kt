package bookservice.db.repository

import bookservice.core.entity.Book
import bookservice.core.repository.BookRepository
import bookservice.db.jooq.tables.references.BOOKS
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import org.jooq.DSLContext
import org.jooq.impl.DSL.excluded
import java.util.UUID

/**
 * 書籍リポジトリの実装
 *
 * @property dslContext DSL コンテキスト
 */
class BookRepositoryImpl(private val dslContext: DSLContext) : BookRepository {
    override fun findById(id: UUID): Result<Book?, String> {
        return dslContext.selectFrom(BOOKS)
            .where(BOOKS.ID.eq(id))
            .fetchOne()
            ?.let { record ->
                Book.create(
                    record.id,
                    record.authorId,
                    record.title,
                    record.titleKana,
                    record.publisherName,
                ).mapError {
                    "登録されているデータが不正です。[table=${BOOKS.name}, id=${record.id}, message=$it]"
                }
            } ?: Ok(null)
    }

    override fun save(book: Book) {
        dslContext.insertInto(BOOKS, BOOKS.ID, BOOKS.AUTHOR_ID, BOOKS.TITLE, BOOKS.TITLE_KANA, BOOKS.PUBLISHER_NAME)
            .values(book.id, book.authorId, book.title, book.titleKana, book.publisherName)
            .onConflict(BOOKS.ID)
            .doUpdate()
            .set(BOOKS.AUTHOR_ID, excluded(BOOKS.AUTHOR_ID))
            .set(BOOKS.TITLE, excluded(BOOKS.TITLE))
            .set(BOOKS.TITLE_KANA, excluded(BOOKS.TITLE_KANA))
            .set(BOOKS.PUBLISHER_NAME, excluded(BOOKS.PUBLISHER_NAME))
            .execute()
    }

    override fun delete(book: Book) {
        dslContext.deleteFrom(BOOKS)
            .where(BOOKS.ID.eq(book.id))
            .execute()
    }
}
