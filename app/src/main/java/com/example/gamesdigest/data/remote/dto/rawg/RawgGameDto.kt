package com.example.gamesdigest.data.remote.dto.rawg

import com.squareup.moshi.Json

data class RawgGameDto(
    val id: Int,
    val name: String?,
    val released: String?,
    @field:Json(name = "background_image") val backgroundImage: String?,
    val rating: Double?,
    @field:Json(name = "rating_top") val ratingTop: Int?,
    @field:Json(name = "ratings_count") val ratingsCount: Int?,
    val metacritic: Int?,
    @field:Json(name = "parent_platforms") val parentPlatforms: List<ParentPlatform>
)
