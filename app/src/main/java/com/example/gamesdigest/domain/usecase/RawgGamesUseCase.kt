package com.example.gamesdigest.domain.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.gamesdigest.common.Resource
import com.example.gamesdigest.common.toGame
import com.example.gamesdigest.domain.model.Game
import com.example.gamesdigest.domain.repository.RawgRepository
import com.example.gamesdigest.presentation.games.RawgGameFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RawgGamesUseCase @Inject constructor(private val rawgRepository: RawgRepository) {

    operator fun invoke(
        filterParameters: RawgGameFilter,
        scope: CoroutineScope
    ): Flow<Resource<PagingData<Game>>> = flow {
        emit(Resource.Loading())
        try {
            rawgRepository.getGames(
                filterParameters.genre,
                filterParameters.releaseYearSince,
                filterParameters.minMetacriticRating
            )
                .flow
                .cachedIn(scope)
                .map { pagingData ->
                    pagingData.map { gameDto ->
                        gameDto.toGame()
                    }
                }
                .collect {
                    emit(Resource.Success(data = it))
                }
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "An unexpected error occured"
                )
            )
        }
    }
}
