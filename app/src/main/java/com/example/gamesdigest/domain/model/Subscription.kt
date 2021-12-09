package com.example.gamesdigest.domain.model

data class Subscription(
    val id: String,
    val gameId: Long,
    val gameEditionName: String,
    val desiredPrice: Double
)
