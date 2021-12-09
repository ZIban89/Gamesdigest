package com.example.gamesdigest.domain.model

import com.example.gamesdigest.data.remote.dto.rawg.ParentPlatform

data class Game(
    val id: Int,
    val name: String,
    val released: String,
    val backgroundImage: String,
    val rating: Double,
    val ratingTop: Int,
    val ratingsCount: Int,
    val metacritic: Int,
    val parentPlatforms: List<ParentPlatform?>
)
