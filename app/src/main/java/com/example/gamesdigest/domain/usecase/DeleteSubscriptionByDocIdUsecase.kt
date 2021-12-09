package com.example.gamesdigest.domain.usecase

import com.example.gamesdigest.domain.repository.FirestoreRepository
import javax.inject.Inject

class DeleteSubscriptionByDocIdUsecase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    operator fun invoke(id: String) {
        firestoreRepository.deleteSubscriptionByDocumentId(id)
    }
}
