package bookservice.webapi.controller.author

import bookservice.webapi.controller.author.dto.AuthorListResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Min
import org.hibernate.validator.constraints.Range
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * 著者のリスト取得 API
 */
@Tag(name = "Author", description = "")
interface GetAuthorListApi {
    /**
     * 指定した条件で著者のリストを取得する。
     *
     * @param name 名前
     * @param limit 取得件数
     * @param offset オフセット
     * @return レスポンス
     */
    @Operation(
        summary = "著者のリストを取得する",
        description = "",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "",
                content = [Content(schema = Schema(implementation = AuthorListResponse::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "",
                content = [Content()],
            ),
        ],
    )
    @GetMapping("/authors", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(
        @Parameter(
            description = "名前（部分一致）",
        ) @RequestParam(required = false) name: String?,
        @Parameter(
            description = "取得する著者の件数",
        ) @RequestParam(defaultValue = "30", required = false) @Range(min = 1, max = 50) limit: Long,
        @Parameter(
            description = "オフセット",
        ) @RequestParam(defaultValue = "0", required = false) @Min(0) offset: Long,
    ): ResponseEntity<AuthorListResponse>
}
