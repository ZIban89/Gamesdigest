package com.example.gamesdigest.vm

import com.example.gamesdigest.domain.model.GameEdition
import com.example.gamesdigest.domain.model.UiState
import com.example.gamesdigest.domain.usecase.GameEditionsUseCase
import com.example.gamesdigest.fakeRepository.FakeCheapSharkRepository
import com.example.gamesdigest.presentation.gameeditions.GameEditionsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test


class GameEditionsViewModelTest {

    private val scope = TestCoroutineScope()
    private val fakeCheapSharkRepository = FakeCheapSharkRepository()
    private val gameEditionsUseCase = GameEditionsUseCase(fakeCheapSharkRepository)

    lateinit var gameEditionsViewModel: GameEditionsViewModel

    @Before
    fun setUp(){
        gameEditionsViewModel = GameEditionsViewModel(gameEditionsUseCase, scope)
    }

    @Test
    fun getEditionsTest() {

        val list = mutableListOf<UiState<List<GameEdition>>>()
        scope.launch { gameEditionsViewModel.gamesEditionState.collectLatest {
            list.add(it)
        }}
        gameEditionsViewModel.getEditions("fake1")
        scope.launch { delay(500)}
        assert(list.size == 3)
        assert(list[2].data?.size == 2)

    }
}