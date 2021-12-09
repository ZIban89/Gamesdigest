package com.example.gamesdigest.presentation.subscription

data class SubscriptionParameters(
    val email: String,
    val gameId: Int,
    val gameName: String,
    val price: Float
)
