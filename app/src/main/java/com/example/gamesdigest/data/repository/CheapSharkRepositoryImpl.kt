package com.example.gamesdigest.data.repository

import com.example.gamesdigest.data.remote.api.CheapSharkApi
import com.example.gamesdigest.domain.repository.CheapSharkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheapSharkRepositoryImpl @Inject constructor(
    private val cheapSharkApi: CheapSharkApi
) : CheapSharkRepository {

    override suspend fun getGameEditions(name: String) =
        withContext(Dispatchers.IO) { cheapSharkApi.getGameEditions(name) }

    override suspend fun getDeals(id: Int) =
        withContext(Dispatchers.IO) { cheapSharkApi.getDeals(id) }

    override suspend fun getStores() =
        withContext(Dispatchers.IO) { cheapSharkApi.getStores().filter { it.isActive != 0 } }

    override suspend fun alertWhenPriceLess(email: String, id: Int, desiredPrice: Float) =
        withContext(Dispatchers.IO) {
            cheapSharkApi.alertWhenPriceLess(email = email, id = id, desiredPrice = desiredPrice)
        }
}
