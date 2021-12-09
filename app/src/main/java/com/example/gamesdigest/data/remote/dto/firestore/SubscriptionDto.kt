package com.example.gamesdigest.data.remote.dto.firestore

import com.google.firebase.firestore.DocumentId

data class SubscriptionDto(
    @DocumentId
    var id: String = "",
    var gameId: Long = 0,
    var gameEditionName: String = "",
    var price: Double = 0.0
)
