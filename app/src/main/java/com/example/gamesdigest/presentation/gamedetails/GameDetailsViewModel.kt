package com.example.gamesdigest.presentation.gamedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesdigest.common.Resource
import com.example.gamesdigest.domain.model.GameDetails
import com.example.gamesdigest.domain.model.UiState
import com.example.gamesdigest.domain.usecase.RawgGameDetailsByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val rawgGameDetailsByIdUseCase: RawgGameDetailsByIdUseCase
) : ViewModel() {

    private val _gameDetailsState = MutableStateFlow(UiState<GameDetails>())
    val gameDetailsState: StateFlow<UiState<GameDetails>> = _gameDetailsState

    fun getGameDetailsById(id: Int) {
        rawgGameDetailsByIdUseCase(id).onEach {
            when (it) {
                is Resource.Success -> {
                    _gameDetailsState.value = UiState(data = it.data)
                }
                is Resource.Error -> {
                    _gameDetailsState.value = UiState(error = it.message ?: "Something went wrong")
                }
                is Resource.Loading -> {
                    _gameDetailsState.value = UiState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
