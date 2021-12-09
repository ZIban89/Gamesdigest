package com.example.gamesdigest.presentation.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesdigest.common.Resource
import com.example.gamesdigest.common.isValidEmail
import com.example.gamesdigest.domain.model.UiState
import com.example.gamesdigest.domain.usecase.AlertWhenPriceLessUseCase
import com.example.gamesdigest.domain.usecase.SaveSubscriptionIntoFirestoreUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionDialogViewModel @Inject constructor(
    private val alertWhenPriceLessUseCase: AlertWhenPriceLessUseCase,
    private val saveSubscriptionIntoFirestoreUsecase: SaveSubscriptionIntoFirestoreUsecase
) : ViewModel() {

    private val _subscriptionState = MutableSharedFlow<UiState<Boolean>>()
    val subscriptionState = _subscriptionState.asSharedFlow()

    private val _subscriptionParams = MutableSharedFlow<SubscriptionParameters>()

    init {
        subscribe()
    }

    fun subscribe(email: String, gameId: Int, gameName: String, desiredPrice: String) {
        val price = desiredPrice.toFloatOrNull()
        if (email.isValidEmail() && price != null) {
            viewModelScope.launch {
                _subscriptionParams.emit(SubscriptionParameters(email, gameId, gameName, price))
            }
        } else {
            viewModelScope.launch {
                _subscriptionState.emit(
                    UiState(
                        error = "Check you fields and try again"
                    )
                )
            }
        }
    }

    private fun subscribe() {
        _subscriptionParams.onEach { params ->
            alertWhenPriceLessUseCase(params.email, params.gameId, params.price).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _subscriptionState.emit(
                            UiState(
                                data = result.data ?: false
                            )
                        )
                        saveSubscriptionIntoFirestoreUsecase(
                            params.email,
                            params.gameId,
                            params.gameName,
                            params.price
                        )
                    }
                    is Resource.Error -> {
                        _subscriptionState.emit(
                            UiState(
                                error = result.message ?: "An unexpected error occured"
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _subscriptionState.emit(UiState(isLoading = true))
                    }
                }
            }.launchIn(viewModelScope)
        }.launchIn(viewModelScope)
    }
}
