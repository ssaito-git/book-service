package bookservice.webapi.service.author

import bookservice.core.entity.Author
import bookservice.core.repository.AuthorRepository
import bookservice.webapi.service.type.Undefinable
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.binding
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.toResultOr
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

/**
 * 著者部分更新サービス
 */
@Service
class PartialUpdateAuthorService(private val authorRepository: AuthorRepository) {
    /**
     * 著者を部分更新する。
     *
     * @param parameter パラメーター
     * @return 更新に成功した場合は更新した後の著者。失敗した場合は [Error]。
     */
    fun partialUpdateAuthor(parameter: Parameter): Result<Author, Error> = binding {
        val author = authorRepository.findById(parameter.authorId)
            .mapError {
                InternalError(it)
            }
            .andThen {
                it.toResultOr { NotFound }
            }
            .bind()

        Ok(author)
            .andThen {
                parameter.name.fold(
                    { name -> it.setName(name).mapError { ValidationError(it) } },
                    { Ok(it) },
                )
            }
            .andThen {
                parameter.nameKana.fold(
                    { nameKana -> it.setNameKana(nameKana).mapError { ValidationError(it) } },
                    { Ok(it) },
                )
            }
            .andThen {
                parameter.birthDate.fold(
                    { birthDate -> it.setBirthDate(birthDate).mapError { ValidationError(it) } },
                    { Ok(it) },
                )
            }
            .andThen {
                parameter.deathDate.fold(
                    { deathDate -> it.setDeathDate(deathDate).mapError { ValidationError(it) } },
                    { Ok(it) },
                )
            }
            .bind()
    }

    /**
     * パラメーター
     *
     * @property authorId 著者 ID
     * @property name 名前
     * @property nameKana 名前かな
     * @property birthDate 生年月日
     * @property deathDate 没年月日
     */
    data class Parameter(
        val authorId: UUID,
        val name: Undefinable<String>,
        val nameKana: Undefinable<String>,
        val birthDate: Undefinable<LocalDate?>,
        val deathDate: Undefinable<LocalDate?>,
    )

    /**
     * エラー
     */
    sealed interface Error

    /**
     * 内部エラー
     *
     * @property message メッセージ
     */
    data class InternalError(val message: String) : Error

    /**
     * 著者が存在しない
     */
    data object NotFound : Error

    /**
     * バリデーションエラー
     *
     * @property message メッセージ
     */
    data class ValidationError(val message: String) : Error
}
