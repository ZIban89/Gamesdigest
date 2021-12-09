package com.example.gamesdigest.presentation.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.gamesdigest.common.Resource
import com.example.gamesdigest.data.remote.UNEXPECTED_ERROR_MESSAGE
import com.example.gamesdigest.domain.model.Game
import com.example.gamesdigest.domain.model.Genre
import com.example.gamesdigest.domain.model.UiState
import com.example.gamesdigest.domain.usecase.GetGenresUseCase
import com.example.gamesdigest.domain.usecase.RawgGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val rawgGamesUseCase: RawgGamesUseCase,
    private val rawgGenresUseCase: GetGenresUseCase
) : ViewModel() {

    private val _genresListState = MutableStateFlow(UiState<List<Genre>>(isLoading = true))
    val genresListState: StateFlow<UiState<List<Genre>>> = _genresListState

    private val _gamesListState =
        MutableStateFlow(UiState<PagingData<Game>>(isLoading = true))
    val gamesListState: StateFlow<UiState<PagingData<Game>>> = _gamesListState

    private var _filterParameters = RawgGameFilter()

    init {
        getGenres()
        getGames()
    }

    fun setFilterParameters(gameFilter: RawgGameFilter) {
        _filterParameters = gameFilter
        getGames()
    }

    private fun getGenres() {
        viewModelScope.launch {
            rawgGenresUseCase().collect { result ->

                when (result) {
                    is Resource.Success -> {
                        _genresListState.value =
                            UiState(data = result.data)
                        this.cancel()
                    }
                    is Resource.Error -> {
                        _genresListState.value = UiState(
                            error = result.message ?: UNEXPECTED_ERROR_MESSAGE
                        )
                    }
                    is Resource.Loading -> {
                        _genresListState.value = UiState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    private fun getGames() {
        rawgGamesUseCase(
            filterParameters = _filterParameters,
            scope = viewModelScope
        ).onEach { result ->
            _gamesListState.value = (
                    when (result) {
                        is Resource.Success -> {
                            UiState(data = result.data)
                        }
                        is Resource.Error -> {
                            UiState(
                                error = result.message ?: UNEXPECTED_ERROR_MESSAGE
                            )
                        }
                        is Resource.Loading -> {
                            UiState(isLoading = true)
                        }
                    }
                    )
        }.launchIn(viewModelScope)
    }
}
