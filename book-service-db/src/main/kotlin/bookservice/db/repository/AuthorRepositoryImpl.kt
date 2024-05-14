package bookservice.db.repository

import bookservice.core.entity.Author
import bookservice.core.repository.AuthorRepository
import bookservice.db.jooq.tables.references.AUTHORS
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import org.jooq.DSLContext
import org.jooq.impl.DSL.excluded
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
                    record.nameKana,
                    record.birthDate,
                    record.deathDate,
                ).mapError {
                    "登録されているデータが不正です。[table=${AUTHORS.name}, id=${record.id}, message=$it]"
                }
            } ?: Ok(null)
    }

    override fun save(author: Author) {
        dslContext.insertInto(
            AUTHORS,
            AUTHORS.ID,
            AUTHORS.NAME,
            AUTHORS.NAME_KANA,
            AUTHORS.BIRTH_DATE,
            AUTHORS.DEATH_DATE,
        )
            .values(
                author.id,
                author.name,
                author.nameKana,
                author.birthDate,
                author.deathDate,
            )
            .onConflict(AUTHORS.ID)
            .doUpdate()
            .set(AUTHORS.ID, excluded(AUTHORS.ID))
            .set(AUTHORS.NAME, excluded(AUTHORS.NAME))
            .set(AUTHORS.NAME_KANA, excluded(AUTHORS.NAME_KANA))
            .set(AUTHORS.BIRTH_DATE, excluded(AUTHORS.BIRTH_DATE))
            .set(AUTHORS.DEATH_DATE, excluded(AUTHORS.DEATH_DATE))
            .execute()
    }

    override fun delete(author: Author) {
        dslContext.deleteFrom(AUTHORS)
            .where(AUTHORS.ID.eq(author.id))
            .execute()
    }
}
