package bookservice.webapi.service.author

import bookservice.core.entity.Author
import bookservice.core.repository.AuthorRepository
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapError
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

/**
 * 著者作成サービス
 */
@Service
class CreateAuthorService(private val authorRepository: AuthorRepository) {
    /**
     * 著者を作成する。
     *
     * @param parameter パラメーター
     * @return 作成した著者。失敗した場合は [Error]。
     */
    fun createAuthor(parameter: Parameter): Result<Author, Error> {
        return Author.create(
            UUID.randomUUID(),
            parameter.name,
            parameter.nameKana,
            parameter.birthDate,
            parameter.deathDate,
        ).mapError {
            ValidationError(it)
        }.andThen {
            authorRepository.save(it)
            Ok(it)
        }
    }

    /**
     * パラメーター
     *
     * @property name 名前
     * @property nameKana 名前かな
     * @property birthDate 生年月日
     * @property deathDate 没年月日
     */
    data class Parameter(
        val name: String,
        val nameKana: String,
        val birthDate: LocalDate?,
        val deathDate: LocalDate?,
    )

    /**
     * エラー
     */
    sealed interface Error

    /**
     * バリデーションエラー
     *
     * @property message メッセージ
     */
    data class ValidationError(val message: String) : Error
}
