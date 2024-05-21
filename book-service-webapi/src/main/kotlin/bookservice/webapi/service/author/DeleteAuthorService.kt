package bookservice.webapi.service.author

import bookservice.core.repository.AuthorRepository
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.toResultOr
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * 著者削除サービス
 */
@Service
class DeleteAuthorService(private val authorRepository: AuthorRepository) {
    /**
     * 著者を削除する。
     *
     * @param id 著者 ID
     * @return 削除に失敗した場合は [Error]。
     */
    fun deleteAuthor(id: UUID): Result<Unit, Error> {
        return authorRepository.findById(id)
            .mapError {
                InternalError(it)
            }
            .andThen {
                it.toResultOr { NotFound }
            }
            .andThen {
                authorRepository.delete(it)
                Ok(Unit)
            }
    }

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
}
