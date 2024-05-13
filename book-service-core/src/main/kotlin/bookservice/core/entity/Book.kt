package bookservice.core.entity

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.util.UUID

/**
 * 書籍
 *
 * @property id 書籍の ID
 * @property authorId 著者の ID
 * @property title タイトル
 * @property publisherName 出版社名
 */
data class Book(val id: UUID, val authorId: UUID, val title: String, val publisherName: String) {
    companion object {
        /**
         * タイトルの最大サイズ
         */
        const val TITLE_MAX_SIZE = 100

        /**
         * 出版社名の最大サイズ
         */
        const val PUBLISHER_NAME_MAX_SIZE = 100

        /**
         * 書籍を作成する
         *
         * @param id 書籍の ID
         * @param authorId 著者の ID
         * @param title タイトル
         * @param publisherName 出版社名
         * @return バリデーションエラーがない場合は書籍。バリデーションエラーがある場合はエラーメッセージ。
         */
        fun create(id: UUID, authorId: UUID, title: String, publisherName: String): Result<Book, String> {
            if (title.length > TITLE_MAX_SIZE) {
                return Err("タイトルが 100 文字より大きいです")
            }

            if (publisherName.length > PUBLISHER_NAME_MAX_SIZE) {
                return Err("出版社名が 100 文字より大きいです")
            }

            return Ok(Book(id, authorId, title, publisherName))
        }
    }
}
