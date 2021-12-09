package com.example.gamesdigest.presentation.games

data class RawgGameFilter(
    val genre: String? = null,
    val releaseYearSince: String? = null,
    val minMetacriticRating: String? = null
)
