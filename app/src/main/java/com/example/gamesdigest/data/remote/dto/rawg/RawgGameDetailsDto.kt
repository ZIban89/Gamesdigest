package com.example.gamesdigest.data.remote.dto.rawg

import com.squareup.moshi.Json

class RawgGameDetailsDto(
    val id: Int?,
    val name: String?,
    val released: String?,
    @field:Json(name = "background_image") val backgroundImage: String?,
    val rating: Double?,
    @field:Json(name = "rating_top") val ratingTop: Int?,
    @field:Json(name = "ratings_count") val ratingsCount: Int?,
    val metacritic: Int?,
    val playtime: Int?,
    val genres: List<GenreDto>,
    @field:Json(name = "parent_platforms") val parentPlatforms: List<ParentPlatform>,
    val tags: List<Tag>,
    val website: String?,
    val description: String?,
    val publishers: List<PublisherDto>,
    val slug: String?
)
