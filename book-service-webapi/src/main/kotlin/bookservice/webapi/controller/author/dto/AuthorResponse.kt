package bookservice.webapi.controller.author.dto

import bookservice.core.entity.Author
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

/**
 * 著者のレスポンス
 */
data class AuthorResponse(
    /**
     * 著者 のID
     */
    @Schema(required = true, description = "著者の ID")
    val id: String,

    /**
     * 名前
     */
    @Schema(required = true, description = "名前")
    val name: String,

    /**
     * 名前かな
     */
    @Schema(required = true, description = "名前かな")
    val nameKana: String,

    /**
     * 生年月日
     */
    @Schema(required = true, description = "生年月日")
    val birthDate: LocalDate?,

    /**
     * 没年月日
     */
    @Schema(required = true, description = "没年月日")
    val deathDate: LocalDate?,
) {
    companion object {
        /**
         * 著者エンティティからレスポンスを生成する。
         *
         * @param author 著者
         * @return レスポンス
         */
        fun from(author: Author): AuthorResponse {
            return AuthorResponse(
                author.id.toString(),
                author.name,
                author.nameKana,
                author.birthDate,
                author.deathDate,
            )
        }
    }
}
