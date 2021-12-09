package com.example.gamesdigest.domain.usecase

import com.example.gamesdigest.common.Resource
import com.example.gamesdigest.domain.repository.CheapSharkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AlertWhenPriceLessUseCase @Inject constructor(private val cheapSharkRepository: CheapSharkRepository) {

    operator fun invoke(email: String, gameId: Int, desiredPrice: Float): Flow<Resource<Boolean>> =
        flow {
            try {
                emit(Resource.Loading())

                emit(
                    Resource.Success(
                        cheapSharkRepository.alertWhenPriceLess(
                            email,
                            gameId,
                            desiredPrice
                        )
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            }
        }
}
