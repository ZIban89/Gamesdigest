package com.example.gamesdigest.domain.model

data class GameDetails(
    val id: Int,
    val name: String,
    val released: String,
    val backgroundImage: String,
    val rating: Double,
    val ratingTop: Int,
    val ratingsCount: Int,
    val metacritic: Int?,
    val playtime: Int,
    val genres: List<Genre>,
    val parentPlatforms: List<Platform>,
    val tags: List<Tag>,
    val website: String,
    val description: String,
    val publishers: List<Publisher>,
    val slug: String
)
