package bookservice.webapi.controller.author.dto

import bookservice.core.entity.Author
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

/**
 * 著者の更新リクエスト
 */
data class PatchAuthorRequest(
    /**
     * 著者名
     */
    @Schema(required = false, description = "著者名")
    @field:Size(max = Author.NAME_MAX_SIZE)
    val name: String?,

    /**
     * 著者名かな
     */
    @Schema(required = false, description = "著者名かな")
    @field:Size(max = Author.NAME_KANA_MAX_SIZE)
    val nameKana: String?,

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
