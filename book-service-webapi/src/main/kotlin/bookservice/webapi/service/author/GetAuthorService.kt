package bookservice.webapi.service.author

import bookservice.core.entity.Author
import bookservice.core.repository.AuthorRepository
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.toResultOr
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * 著者取得サービス
 */
@Service
class GetAuthorService(private val authorRepository: AuthorRepository) {
    /**
     * 指定した ID の著者を取得する。
     *
     * @param id 著者 ID
     * @return 著者 ID が一致する著者。取得に失敗した場合は [Error]。
     */
    fun getAuthor(id: UUID): Result<Author, Error> {
        return authorRepository.findById(id)
            .mapError {
                InternalError(it)
            }
            .andThen {
                it.toResultOr { NotFound }
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
