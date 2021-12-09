package com.example.gamesdigest.domain.usecase

import com.example.gamesdigest.common.Resource
import com.example.gamesdigest.common.toGameEdition
import com.example.gamesdigest.domain.model.GameEdition
import com.example.gamesdigest.domain.repository.CheapSharkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GameEditionsUseCase @Inject constructor(private val cheapSharkRepository: CheapSharkRepository) {

    operator fun invoke(name: String): Flow<Resource<List<GameEdition>>> = flow {

        try {
            emit(Resource.Loading<List<GameEdition>>())
            emit(
                Resource.Success(
                    cheapSharkRepository.getGameEditions(name).map { it.toGameEdition() })
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error<List<GameEdition>>(
                    e.localizedMessage ?: "An unexpected error occured"
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error<List<GameEdition>>("Couldn't reach server. Check your internet connection."))
        }
    }
}
