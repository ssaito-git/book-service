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
 * @property titleKana タイトル（かな）
 * @property publisherName 出版社名
 */
data class Book(
    val id: UUID,
    val authorId: UUID,
    val title: String,
    val titleKana: String,
    val publisherName: String,
) {
    /**
     * 著者 ID を更新する。
     */
    fun setAuthorId(authorId: UUID): Result<Book, String> {
        return Ok(this.copy(authorId = authorId))
    }

    /**
     * タイトルを更新する。
     */
    fun setTitle(title: String): Result<Book, String> {
        if (title.length > TITLE_MAX_SIZE) {
            return Err("タイトルが $TITLE_MAX_SIZE 文字より大きいです")
        }

        return Ok(this.copy(title = title))
    }

    /**
     * タイトル（かな）を更新する。
     */
    fun setTitleKana(titleKana: String): Result<Book, String> {
        if (titleKana.length > TITLE_KANA_MAX_SIZE) {
            return Err("タイトル（かな）が $TITLE_KANA_MAX_SIZE 文字より大きいです")
        }

        return Ok(this.copy(titleKana = titleKana))
    }

    /**
     * 出版社名を更新する。
     */
    fun setPublisherName(publisherName: String): Result<Book, String> {
        if (publisherName.length > PUBLISHER_NAME_MAX_SIZE) {
            return Err("出版社名が $PUBLISHER_NAME_MAX_SIZE 文字より大きいです")
        }

        return Ok(this.copy(publisherName = publisherName))
    }

    companion object {
        /**
         * タイトルの最大サイズ
         */
        const val TITLE_MAX_SIZE = 100

        /**
         * タイトル（かな）の最大サイズ
         */
        const val TITLE_KANA_MAX_SIZE = 100

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
         * @param titleKana タイトル（かな）
         * @param publisherName 出版社名
         * @return バリデーションエラーがない場合は書籍。バリデーションエラーがある場合はエラーメッセージ。
         */
        fun create(
            id: UUID,
            authorId: UUID,
            title: String,
            titleKana: String,
            publisherName: String,
        ): Result<Book, String> {
            if (title.length > TITLE_MAX_SIZE) {
                return Err("タイトルが $TITLE_MAX_SIZE 文字より大きいです")
            }

            if (titleKana.length > TITLE_KANA_MAX_SIZE) {
                return Err("タイトル（かな）が $TITLE_KANA_MAX_SIZE 文字より大きいです")
            }

            if (publisherName.length > PUBLISHER_NAME_MAX_SIZE) {
                return Err("出版社名が $PUBLISHER_NAME_MAX_SIZE 文字より大きいです")
            }

            return Ok(Book(id, authorId, title, titleKana, publisherName))
        }
    }
}
