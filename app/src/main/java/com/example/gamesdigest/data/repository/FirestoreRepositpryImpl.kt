package com.example.gamesdigest.data.repository

import android.util.Log
import com.example.gamesdigest.common.asFlow
import com.example.gamesdigest.data.remote.dto.firestore.SubscriptionDto
import com.example.gamesdigest.domain.repository.FirestoreRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirestoreRepositpryImpl @Inject constructor(
    private val collection: CollectionReference
) : FirestoreRepository {

    override fun saveSubscription(email: String, gameId: Int, gameName: String, price: Float) {
        val parametersMap = HashMap<String, Any>()
        parametersMap += "email" to email
        parametersMap += "gameId" to gameId
        parametersMap += "gameEditionName" to gameName
        parametersMap += "price" to price
        collection
            .add(parametersMap)
    }

    override fun getSubscriptionsByEmail(email: String) = collection
        .whereEqualTo("email", email)
        .orderBy("gameEditionName", Query.Direction.ASCENDING)
        .asFlow()
        .map { it ->
            it.map {
                it.toObject(SubscriptionDto::class.java)
            }
        }

    override fun deleteSubscriptionByDocumentId(id: String) {
        collection.document(id).delete()
        Log.d("TAGGG", "delete")
    }
}
