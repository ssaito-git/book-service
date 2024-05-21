package bookservice.webapi.service.book

import bookservice.core.repository.BookRepository
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.binding
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.toResultOr
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * 書籍削除サービス
 */
@Service
class DeleteBookService(private val bookRepository: BookRepository) {
    /**
     * 書籍を削除する。
     *
     * @param bookId 書籍 ID
     * @return 削除に失敗した場合は [Error]。
     */
    fun deleteBook(bookId: UUID): Result<Unit, Error> = binding {
        val book = bookRepository.findById(bookId)
            .mapError {
                InternalError(it)
            }
            .andThen {
                it.toResultOr { NotFound }
            }
            .bind()

        bookRepository.delete(book)
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
