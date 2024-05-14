package bookservice.webapi.controller.author.dto

import bookservice.core.entity.Author
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

/**
 * 著者の作成リクエスト
 */
data class PostAuthorRequest(
    /**
     * 名前
     */
    @Schema(required = true, description = "名前")
    @field:NotBlank
    @field:Size(max = Author.NAME_MAX_SIZE)
    val name: String,

    /**
     * 名前かな
     */
    @Schema(required = true, description = "名前かな")
    @field:NotBlank
    @field:Size(max = Author.NAME_KANA_MAX_SIZE)
    val nameKana: String,

    /**
     * 生年月日
     */
    @Schema(required = false, description = "生年月日")
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val birthDate: LocalDate?,

    /**
     * 没年月日
     */
    @Schema(required = false, description = "没年月日")
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val deathDate: LocalDate?,
)
