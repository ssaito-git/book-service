package bookservice.core.repository

import bookservice.core.entity.Book
import com.github.michaelbull.result.Result
import java.util.UUID

/**
 * 書籍リポジトリ
 */
interface BookRepository {
    /**
     * ID に一致する書籍を取得する。
     *
     * @param id 書籍の ID
     * @return ID に一致する書籍。存在しない場合は null。
     */
    fun findById(id: UUID): Result<Book?, String>

    /**
     * 書籍を保存する。
     *
     * @param book 書籍
     */
    fun save(book: Book)

    /**
     * 書籍を削除する。
     *
     * @param book 書籍
     */
    fun delete(book: Book)
}
