package com.example.gamesdigest.domain.usecase

import com.example.gamesdigest.common.Resource
import com.example.gamesdigest.domain.model.Genre
import com.example.gamesdigest.domain.repository.RawgRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(private val rawgRepository: RawgRepository) {

    operator fun invoke(): Flow<Resource<List<Genre>>> = flow {
        try {
            emit(Resource.Loading<List<Genre>>())
            val genres = rawgRepository.getGenres()
                .map { Genre(id = it.id, name = it.name) }
            emit(Resource.Success(genres))
        } catch (e: HttpException) {
            emit(
                Resource.Error<List<Genre>>(
                    e.localizedMessage ?: "An unexpected error occured"
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error<List<Genre>>("Couldn't reach server. Check your internet connection."))
        }
    }
}
