package com.example.gamesdigest.data.remote.dto.rawg.genres

import com.squareup.moshi.Json

data class Responce(

    @field:Json(name = "results") val results: List<Genre>
)
