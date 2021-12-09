package com.example.gamesdigest.domain.usecase

import com.example.gamesdigest.common.Resource
import com.example.gamesdigest.common.toStore
import com.example.gamesdigest.domain.model.Deal
import com.example.gamesdigest.domain.model.Deals
import com.example.gamesdigest.domain.model.Store
import com.example.gamesdigest.domain.repository.CheapSharkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetDealsUseCase @Inject constructor(private val cheapSharkRepository: CheapSharkRepository) {

    operator fun invoke(gameId: Int): Flow<Resource<Deals>> = flow {
        try {
            emit(Resource.Loading())
            val stores = HashMap<String, Store>()
            cheapSharkRepository.getStores().forEach { stores.put(it.storeID, it.toStore()) }
            val dealsDto = cheapSharkRepository.getDeals(gameId)
            val deals = Deals(
                gameId,
                dealsDto.info.title,
                dealsDto.info.thumb,
                dealsDto.deals.map {
                    Deal(
                        String.format("%.2f", it.price),
                        String.format("%.0f", it.savings),
                        stores[it.storeID] ?: Store()
                    )
                }
            )
            emit(Resource.Success(data = deals))
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
