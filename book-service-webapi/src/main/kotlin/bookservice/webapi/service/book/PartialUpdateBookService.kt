package bookservice.webapi.service.book

import bookservice.core.entity.Book
import bookservice.core.repository.AuthorRepository
import bookservice.core.repository.BookRepository
import bookservice.webapi.service.type.Undefinable
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.binding
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.toResultOr
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * 書籍更新サービス
 */
@Service
class PartialUpdateBookService(
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

        Ok(book)
            .andThen {
                parameter.authorId.fold(
                    { authorId -> setAuthorIdToBook(it, authorId) },
                    { Ok(it) },
                )
            }
            .andThen {
                parameter.title.fold(
                    { title -> it.setTitle(title).mapError { ValidationError(it) } },
                    { Ok(it) },
                )
            }
            .andThen {
                parameter.titleKana.fold(
                    { titleKana -> it.setTitleKana(titleKana).mapError { ValidationError(it) } },
                    { Ok(it) },
                )
            }
            .andThen {
                parameter.publisherName.fold(
                    { publisherName -> it.setPublisherName(publisherName).mapError { ValidationError(it) } },
                    { Ok(it) },
                )
            }
            .andThen {
                bookRepository.save(it)
                Ok(it)
            }
            .bind()
    }

    private fun setAuthorIdToBook(book: Book, authorId: UUID): Result<Book, Error> {
        return authorRepository.findById(authorId)
            .mapError {
                InternalError(it)
            }
            .andThen {
                it.toResultOr { ValidationError("著者が存在しません") }
            }
            .andThen { author ->
                book.setAuthorId(author.id).mapError { ValidationError(it) }
            }
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
        val authorId: Undefinable<UUID>,
        val title: Undefinable<String>,
        val titleKana: Undefinable<String>,
        val publisherName: Undefinable<String>,
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
