package bookservice.core.entity

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.util.UUID

/**
 * 著者
 *
 * @property id 著者の ID
 * @property name 著者名
 */
data class Author(val id: UUID, val name: String) {
    companion object {
        /**
         * 著者名の最大サイズ
         */
        const val NAME_MAX_SIZE = 100

        /**
         * 著者を作成する
         *
         * @param id 著者の ID
         * @param name 著者名
         * @return バリデーションエラーがない場合は著者。バリデーションエラーがある場合はエラーメッセージ。
         */
        fun create(id: UUID, name: String): Result<Author, String> {
            if (name.length > NAME_MAX_SIZE) {
                return Err("著者名が 100 文字より大きいです")
            }

            return Ok(Author(id, name))
        }
    }
}
