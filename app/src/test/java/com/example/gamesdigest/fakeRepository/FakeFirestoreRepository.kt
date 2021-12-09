package com.example.gamesdigest.fakeRepository

import com.example.gamesdigest.data.remote.dto.firestore.SubscriptionDto
import com.example.gamesdigest.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeFirestoreRepository : FirestoreRepository {

    var id = 0

    val subscriptions = HashMap<String, MutableList<SubscriptionDto>>()


    override fun saveSubscription(email: String, gameId: Int, gameName: String, price: Float) {
        val subscription =
            SubscriptionDto((++id).toString(), gameId.toLong(), gameName, price.toDouble())
        subscriptions.get(email)?.let {
            it.add(subscription)
            return
        }
        subscriptions += email to mutableListOf(subscription)
    }

    override fun getSubscriptionsByEmail(email: String): Flow<List<SubscriptionDto>> = flow {
        val list = subscriptions.get(email)?.toList() ?: emptyList()
        emit(list)
    }


    override fun deleteSubscriptionByDocumentId(id: String) {
        var email: String
        subscriptions.forEach { entry ->
            val iterator = entry.value.iterator()
            while (iterator.hasNext()) {
                if (iterator.next().id == id) {
                    iterator.remove()
                    email = entry.key
                    if (subscriptions.get(email)?.isEmpty() == true) subscriptions.remove(email)
                    break
                }
            }
        }
    }
}