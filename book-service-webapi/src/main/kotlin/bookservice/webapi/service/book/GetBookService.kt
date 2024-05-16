package bookservice.webapi.service.book

import bookservice.core.entity.Book
import bookservice.core.repository.BookRepository
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.binding
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.toResultOr
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * 書籍取得サービス
 */
@Service
class GetBookService(private val bookRepository: BookRepository) {
    /**
     * 指定した ID の書籍を取得する。
     *
     * @param bookId 書籍 ID
     * @return 書籍 ID が一致する書籍。取得に失敗した場合は [Error]。
     */
    fun getBook(bookId: UUID): Result<Book, Error> = binding {
        bookRepository.findById(bookId)
            .mapError {
                InternalError(it)
            }
            .andThen {
                it.toResultOr { NotFound }
            }
            .bind()
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
     * 書籍が存在しない
     */
    data object NotFound : Error
}
