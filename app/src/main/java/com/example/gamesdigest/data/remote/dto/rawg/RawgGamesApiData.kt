package com.example.gamesdigest.data.remote.dto.rawg

data class RawgGamesApiData(
    val count: Int,
    val description: String,
    val next: String?,
    val previous: String?,
    val results: List<RawgGameDto>
)
