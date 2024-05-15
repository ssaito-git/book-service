package bookservice.core.entity

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.time.LocalDate
import java.util.UUID

/**
 * 著者
 *
 * @property id 著者の ID
 * @property name 著者名
 * @property nameKana 著者名（かな）
 * @property birthDate 生年月日。不明の場合は null。
 * @property deathDate 没年月日。不明の場合は null。
 */
data class Author(
    val id: UUID,
    val name: String,
    val nameKana: String,
    val birthDate: LocalDate?,
    val deathDate: LocalDate?,
) {
    /**
     * 著者名を更新する。
     */
    fun setName(name: String): Result<Author, String> {
        if (name.length > NAME_MAX_SIZE) {
            return Err("著者名が $NAME_MAX_SIZE 文字より大きいです")
        }

        return Ok(this.copy(name = name))
    }

    /**
     * 著者名（かな）を更新する。
     */
    fun setNameKana(nameKana: String): Result<Author, String> {
        if (nameKana.length > NAME_KANA_MAX_SIZE) {
            return Err("著者名（かな）が $NAME_KANA_MAX_SIZE 文字より大きいです")
        }

        return Ok(this.copy(nameKana = nameKana))
    }

    /**
     * 生年月日を更新する。
     */
    fun setBirthDate(birthDate: LocalDate?): Result<Author, String> {
        return Ok(this.copy(birthDate = birthDate))
    }

    /**
     * 没年月日を更新する。
     */
    fun setDeathDate(deathDate: LocalDate?): Result<Author, String> {
        return Ok(this.copy(deathDate = deathDate))
    }

    companion object {
        /**
         * 著者名の最大サイズ
         */
        const val NAME_MAX_SIZE = 100

        /**
         * 著者名（かな）の最大サイズ
         */
        const val NAME_KANA_MAX_SIZE = 100

        /**
         * 著者を作成する
         *
         * @param id 著者の ID
         * @param name 著者名
         * @param nameKana 著者名（かな）
         * @param birthDate 生年月日
         * @param deathDate 没年月日
         * @return バリデーションエラーがない場合は著者。バリデーションエラーがある場合はエラーメッセージ。
         */
        fun create(
            id: UUID,
            name: String,
            nameKana: String,
            birthDate: LocalDate?,
            deathDate: LocalDate?,
        ): Result<Author, String> {
            if (name.length > NAME_MAX_SIZE) {
                return Err("著者名が $NAME_MAX_SIZE 文字より大きいです")
            }

            if (nameKana.length > NAME_KANA_MAX_SIZE) {
                return Err("著者名（かな）が $NAME_KANA_MAX_SIZE 文字より大きいです")
            }

            return Ok(Author(id, name, nameKana, birthDate, deathDate))
        }
    }
}
