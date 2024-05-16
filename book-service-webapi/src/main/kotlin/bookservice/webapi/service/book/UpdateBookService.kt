package bookservice.webapi.service.book

import bookservice.core.entity.Book
import bookservice.core.repository.AuthorRepository
import bookservice.core.repository.BookRepository
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.binding
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.toResultOr
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * 書籍更新サービス
 */
@Service
class UpdateBookService(
    private val authorRepository: AuthorRepository,
    private val bookRepository: BookRepository,
) {
    /**
     * 書籍を更新する。
     *
     * @param parameter パラメーター
     * @return 更新に成功した場合は更新した後の書籍。失敗した場合は [Error]。
     */
    fun updateBook(parameter: Parameter): Result<Book, Error> = binding {
        val book = bookRepository.findById(parameter.bookId)
            .mapError {
                InternalError(it)
            }
            .andThen {
                it.toResultOr { NotFound }
            }
            .bind()

        val authorId = parameter.authorId?.let {
            authorRepository.findById(parameter.authorId)
                .mapError {
                    InternalError(it)
                }
                .andThen {
                    it.toResultOr { ValidationError("著者が存在しません") }
                }
                .map {
                    it.id
                }
        }?.bind()

        val updatedBook = book.setAuthorId(authorId ?: book.authorId)
            .andThen {
                it.setTitle(parameter.title ?: book.title)
            }
            .andThen {
                it.setTitleKana(parameter.titleKana ?: book.titleKana)
            }
            .andThen {
                it.setPublisherName(parameter.publisherName ?: book.publisherName)
            }
            .mapError {
                InternalError(it)
            }
            .bind()

        bookRepository.save(updatedBook)

        updatedBook
    }

    /**
     * パラメーター
     *
     * @property bookId 書籍 ID
     * @property authorId 著者 ID
     * @property title タイトル
     * @property titleKana タイトルかな
     * @property publisherName 出版社名
     */
    data class Parameter(
        val bookId: UUID,
        val authorId: UUID?,
        val title: String?,
        val titleKana: String?,
        val publisherName: String?,
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
     * 書籍が存在しない
     */
    data object NotFound : Error

    /**
     * バリデーションエラー
     *
     * @property message メッセージ
     */
    data class ValidationError(val message: String) : Error
}
