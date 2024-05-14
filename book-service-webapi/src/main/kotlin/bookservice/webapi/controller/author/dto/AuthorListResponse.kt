package bookservice.webapi.controller.author.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 著者リストレスポンス
 */
@Schema(name = "AuthorList")
data class AuthorListResponse(
    /**
     * 著者のリスト
     */
    @Schema(required = true, description = "著者のリスト")
    val authors: List<AuthorListItem>,

    /**
     * さらに著者が存在するか
     */
    @Schema(required = true, description = "さらに著者が存在するか")
    val hasMore: Boolean,
)
