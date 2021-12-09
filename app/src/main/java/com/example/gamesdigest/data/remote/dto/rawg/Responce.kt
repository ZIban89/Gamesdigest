package com.example.gamesdigest.data.remote.dto.rawg

import com.squareup.moshi.Json

data class Responce(

    @field:Json(name = "results") val results: List<GenreDto>
)
