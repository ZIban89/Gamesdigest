package com.example.gamesdigest.presentation.subscriptions

import androidx.lifecycle.ViewModel
import com.example.gamesdigest.common.Resource
import com.example.gamesdigest.common.getViewModelScope
import com.example.gamesdigest.data.remote.SOMETHING_WENT_WRONG_MESSAGE
import com.example.gamesdigest.domain.model.Subscription
import com.example.gamesdigest.domain.model.UiState
import com.example.gamesdigest.domain.usecase.DeleteSubscriptionByDocIdUsecase
import com.example.gamesdigest.domain.usecase.GetSubscriptionsByEmailUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val getSubscriptionsByEmailUsecase: GetSubscriptionsByEmailUsecase,
    private val deleteSubscriptionByDocIdUsecase: DeleteSubscriptionByDocIdUsecase,
    private val coroutineScope: CoroutineScope? = null
) : ViewModel() {

    private val scope = getViewModelScope(coroutineScope)

    private val _subscriptionsState = MutableSharedFlow<UiState<List<Subscription>>>()
    val subscriptionsState = _subscriptionsState.asSharedFlow()

    fun getSubscriptionsByEmail(email: String) {

        getSubscriptionsByEmailUsecase(email)
            .onEach { result ->
                _subscriptionsState.emit(
                    when (result) {
                        is Resource.Error -> {
                            UiState(error = result.message ?: SOMETHING_WENT_WRONG_MESSAGE)
                        }
                        is Resource.Loading -> {
                            UiState(isLoading = true)
                        }
                        is Resource.Success -> {
                            UiState(data = result.data)
                        }
                    }
                )
            }
            .launchIn(scope)
    }

    fun deleteSubscriptionByDocId(id: String) {
        deleteSubscriptionByDocIdUsecase(id)
    }
}
