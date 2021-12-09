package com.example.gamesdigest.domain.usecase

import com.example.gamesdigest.common.Resource
import com.example.gamesdigest.common.toGameDetails
import com.example.gamesdigest.domain.model.GameDetails
import com.example.gamesdigest.domain.repository.RawgRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RawgGameDetailsByIdUseCase @Inject constructor(private val rawgRepository: RawgRepository) {

    operator fun invoke(id: Int): Flow<Resource<GameDetails>> = flow {

        try {
            emit(Resource.Loading())
            emit(Resource.Success(rawgRepository.getGameDetails(id).toGameDetails()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}
