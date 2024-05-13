package bookservice.db.repository

import bookservice.core.entity.Author
import bookservice.core.repository.AuthorRepository
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import org.jooq.DSLContext
import org.jooq.impl.DSL.excluded
import samplespringboot.db.jooq.tables.references.AUTHORS
import java.util.UUID

/**
 * 著者リポジトリの実装
 *
 * @property dslContext DSL コンテキスト
 */
class AuthorRepositoryImpl(private val dslContext: DSLContext) : AuthorRepository {
    override fun findById(id: UUID): Result<Author?, String> {
        return dslContext.selectFrom(AUTHORS)
            .where(AUTHORS.ID.eq(id))
            .fetchOne()
            ?.let { record ->
                Author.create(
                    record.id,
                    record.name,
                ).mapError {
                    "登録されているデータが不正です。[table=${AUTHORS.name}, id=${record.id}, message=$it]"
                }
            } ?: Ok(null)
    }

    override fun save(author: Author) {
        dslContext.insertInto(AUTHORS, AUTHORS.ID, AUTHORS.NAME)
            .values(author.id, author.name)
            .onConflict(AUTHORS.ID)
            .doUpdate()
            .set(AUTHORS.ID, excluded(AUTHORS.ID))
            .set(AUTHORS.NAME, excluded(AUTHORS.NAME))
            .execute()
    }

    override fun delete(author: Author) {
        dslContext.deleteFrom(AUTHORS)
            .where(AUTHORS.ID.eq(author.id))
            .execute()
    }
}
