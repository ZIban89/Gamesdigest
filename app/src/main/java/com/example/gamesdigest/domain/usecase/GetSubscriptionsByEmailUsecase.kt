package com.example.gamesdigest.domain.usecase

import com.example.gamesdigest.common.Resource
import com.example.gamesdigest.common.toSubscription
import com.example.gamesdigest.data.remote.SOMETHING_WENT_WRONG_MESSAGE
import com.example.gamesdigest.domain.model.Subscription
import com.example.gamesdigest.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSubscriptionsByEmailUsecase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    operator fun invoke(email: String): Flow<Resource<List<Subscription>>> = flow {
        emit(Resource.Loading())
        try {
            firestoreRepository.getSubscriptionsByEmail(email).collect { list ->
                emit(Resource.Success(data = list.map { it.toSubscription() }))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: SOMETHING_WENT_WRONG_MESSAGE))
        }
    }
}
