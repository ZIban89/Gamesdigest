package com.example.gamesdigest.vm

import com.example.gamesdigest.domain.model.GameDetails
import com.example.gamesdigest.domain.model.UiState
import com.example.gamesdigest.domain.usecase.RawgGameDetailsByIdUseCase
import com.example.gamesdigest.fakeRepository.FakeRawgRepository
import com.example.gamesdigest.presentation.gamedetails.GameDetailsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Before
import org.junit.Test

class GameDetailsViewModelTest {

    private val scope = TestCoroutineScope()
    private val fakeRawgRepository = FakeRawgRepository()
    private val rawgGameDetailsByIdUseCase = RawgGameDetailsByIdUseCase(fakeRawgRepository)
    lateinit var gameDetailsViewModel: GameDetailsViewModel

    @Before
    fun setUp() {
        gameDetailsViewModel = GameDetailsViewModel(rawgGameDetailsByIdUseCase, scope)
    }

    @Test
    fun getGameDetailsByIdTest() {
        val list = mutableListOf<UiState<GameDetails>>()
        scope.launch {
            gameDetailsViewModel.gameDetailsState.collectLatest {
                list.add(it)
            }
        }
        gameDetailsViewModel.getGameDetailsById(1)
        scope.launch { delay(500) }
        assert(list.size == 3)
        assert(list[2].data?.id == 1)
    }

    @Test
    fun getGameDetailsByWrongIdTest() {
        val list = mutableListOf<UiState<GameDetails>>()
        scope.launch {
            gameDetailsViewModel.gameDetailsState.collectLatest {
                list.add(it)
            }
        }
        gameDetailsViewModel.getGameDetailsById(0)
        scope.launch { delay(500) }
        assert(list.size == 2)
        assert(list[1].isLoading)
    }
}