package com.example.gamesdigest.domain.usecase

import com.example.gamesdigest.domain.repository.FirestoreRepository
import javax.inject.Inject

class SaveSubscriptionIntoFirestoreUsecase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {

    operator fun invoke(email: String, gameId: Int, gameName: String, price: Float) {
        firestoreRepository.saveSubscription(email, gameId, gameName, price)
    }
}
