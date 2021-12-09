package com.example.gamesdigest.data.remote.dto.rawg

import com.squareup.moshi.Json

data class GenreDto(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "name") val name: String
)
