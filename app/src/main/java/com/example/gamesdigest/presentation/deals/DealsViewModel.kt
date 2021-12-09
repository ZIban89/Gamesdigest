package com.example.gamesdigest.presentation.deals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesdigest.common.Resource
import com.example.gamesdigest.domain.model.Deals
import com.example.gamesdigest.domain.model.UiState
import com.example.gamesdigest.domain.usecase.GetDealsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DealsViewModel @Inject constructor(
    private val getDealsUseCase: GetDealsUseCase
) :
    ViewModel() {

    private val _dealsState = MutableStateFlow(UiState<Deals>())
    val dealsState: StateFlow<UiState<Deals>> = _dealsState

    fun getDeals(id: Int) {
        getDealsUseCase(id).onEach {
            when (it) {
                is Resource.Success -> {
                    _dealsState.value = UiState(data = it.data)
                }
                is Resource.Error -> {
                    _dealsState.value = UiState(error = it.message ?: "Something went wrong")
                }
                is Resource.Loading -> {
                    _dealsState.value = UiState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
