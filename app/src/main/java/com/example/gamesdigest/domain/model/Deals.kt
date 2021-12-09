package com.example.gamesdigest.domain.model

data class Deals(
    val gameId: Int,
    val gameName: String,
    val thumb: String,
    val list: List<Deal>
)
