package com.example.gamesdigest.presentation.gameeditions

import androidx.lifecycle.ViewModel
import com.example.gamesdigest.common.Resource
import com.example.gamesdigest.common.getViewModelScope
import com.example.gamesdigest.domain.model.GameEdition
import com.example.gamesdigest.domain.model.UiState
import com.example.gamesdigest.domain.usecase.GameEditionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GameEditionsViewModel @Inject constructor(
    private val gameEditionsUseCase: GameEditionsUseCase,
    coroutineScope: CoroutineScope? = null
) : ViewModel() {

    private val scope = getViewModelScope(coroutineScope)
    private val _gamesEditionState = MutableStateFlow(UiState<List<GameEdition>>())
    val gamesEditionState: StateFlow<UiState<List<GameEdition>>> = _gamesEditionState

    fun getEditions(name: String) {
        gameEditionsUseCase(name).onEach {
            when (it) {
                is Resource.Success -> {
                    _gamesEditionState.value = UiState(data = it.data ?: emptyList())
                }
                is Resource.Error -> {
                    _gamesEditionState.value = UiState(error = it.message ?: "Something went wrong")
                }
                is Resource.Loading -> {
                    _gamesEditionState.value = UiState(isLoading = true)
                }
            }
        }.launchIn(scope)
    }
}
