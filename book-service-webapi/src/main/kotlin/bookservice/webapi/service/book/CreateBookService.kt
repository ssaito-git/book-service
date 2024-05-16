package bookservice.webapi.service.book

import bookservice.core.entity.Book
import bookservice.core.repository.AuthorRepository
import bookservice.core.repository.BookRepository
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.binding
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.toResultOr
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * 書籍作成サービス
 */
@Service
class CreateBookService(
    private val authorRepository: AuthorRepository,
    private val bookRepository: BookRepository,
) {
    /**
     * 書籍を作成する。
     *
     * @param parameter パラメーター
     * @return 作成した書籍。失敗した場合は [Error]。
     */
    fun createBook(parameter: Parameter): Result<Book, Error> = binding {
        val author = authorRepository.findById(parameter.authorId)
            .mapError {
                InternalError(it)
            }
            .andThen {
                it.toResultOr { ValidationError("著者が存在しません") }
            }
            .bind()

        val book = Book.create(
            UUID.randomUUID(),
            author.id,
            parameter.title,
            parameter.titleKana,
            parameter.publisherName,
        ).mapError {
            ValidationError(it)
        }.bind()

        bookRepository.save(book)

        book
    }

    /**
     * パラメーター
     *
     * @property authorId 著者 ID
     * @property title タイトル
     * @property titleKana タイトルかな
     * @property publisherName 出版社名
     */
    data class Parameter(
        val authorId: UUID,
        val title: String,
        val titleKana: String,
        val publisherName: String,
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
     * バリデーションエラー
     *
     * @property message メッセージ
     */
    data class ValidationError(val message: String) : Error
}
