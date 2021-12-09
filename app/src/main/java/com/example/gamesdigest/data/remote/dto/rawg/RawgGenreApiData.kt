package com.example.gamesdigest.data.remote.dto.rawg

data class RawgGenreApiData(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<GenreDto>
)
