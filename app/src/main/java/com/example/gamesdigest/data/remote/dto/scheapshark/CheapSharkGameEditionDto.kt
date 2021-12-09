package com.example.gamesdigest.data.remote.dto.scheapshark

import com.squareup.moshi.Json

data class CheapSharkGameEditionDto(
    val cheapest: String,
    @field:Json(name = "external") val name: String,
    val gameID: String,
    val thumb: String
)
