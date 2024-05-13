package bookservice.db.repository

import bookservice.core.entity.Book
import bookservice.core.repository.BookRepository
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import org.jooq.DSLContext
import org.jooq.impl.DSL.excluded
import samplespringboot.db.jooq.tables.references.BOOKS
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
                    record.publisherName,
                ).mapError {
                    "登録されているデータが不正です。[table=${BOOKS.name}, id=${record.id}, message=$it]"
                }
            } ?: Ok(null)
    }

    override fun save(book: Book) {
        dslContext.insertInto(BOOKS, BOOKS.ID, BOOKS.AUTHOR_ID, BOOKS.TITLE, BOOKS.PUBLISHER_NAME)
            .values(book.id, book.authorId, book.title, book.publisherName)
            .onConflict(BOOKS.ID)
            .doUpdate()
            .set(BOOKS.AUTHOR_ID, excluded(BOOKS.AUTHOR_ID))
            .set(BOOKS.TITLE, excluded(BOOKS.TITLE))
            .set(BOOKS.PUBLISHER_NAME, excluded(BOOKS.PUBLISHER_NAME))
            .execute()
    }

    override fun delete(book: Book) {
        dslContext.deleteFrom(BOOKS)
            .where(BOOKS.ID.eq(book.id))
            .execute()
    }
}
