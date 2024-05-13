package bookservice.core.repository

import bookservice.core.entity.Author
import com.github.michaelbull.result.Result
import java.util.UUID

/**
 * 著者リポジトリ
 */
interface AuthorRepository {
    /**
     * ID に一致する著者を取得する。
     *
     * @param id 著者の ID
     * @return ID に一致する著者。存在しない場合は null。
     */
    fun findById(id: UUID): Result<Author?, String>

    /**
     * 著者を保存する。
     *
     * @param author 著者
     */
    fun save(author: Author)

    /**
     * 著者を削除する。
     *
     * @param author 著者
     */
    fun delete(author: Author)
}
