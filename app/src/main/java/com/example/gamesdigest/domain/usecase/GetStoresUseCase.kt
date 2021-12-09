package com.example.gamesdigest.domain.usecase

import com.example.gamesdigest.common.Resource
import com.example.gamesdigest.common.toStore
import com.example.gamesdigest.domain.model.Store
import com.example.gamesdigest.domain.repository.CheapSharkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetStoresUseCase @Inject constructor(private val cheapSharkRepository: CheapSharkRepository) {
    operator fun invoke(): Flow<Resource<List<Store>>> = flow {
        try {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    cheapSharkRepository.getStores().map { it.toStore() })
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "An unexpected error occured"
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}
