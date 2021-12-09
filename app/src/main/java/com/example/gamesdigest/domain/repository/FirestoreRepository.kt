package com.example.gamesdigest.domain.repository

import com.example.gamesdigest.data.remote.dto.firestore.SubscriptionDto
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    fun saveSubscription(email: String, gameId: Int, gameName: String, price: Float)
    fun getSubscriptionsByEmail(email: String): Flow<List<SubscriptionDto>>
    fun deleteSubscriptionByDocumentId(id: String)
}
