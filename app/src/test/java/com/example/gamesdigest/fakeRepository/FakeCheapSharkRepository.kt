package com.example.gamesdigest.fakeRepository

import android.util.Log
import com.example.gamesdigest.data.remote.dto.scheapshark.CheapSharkDeal
import com.example.gamesdigest.data.remote.dto.scheapshark.CheapSharkGameEditionDeals
import com.example.gamesdigest.data.remote.dto.scheapshark.CheapSharkGameEditionDto
import com.example.gamesdigest.data.remote.dto.scheapshark.Images
import com.example.gamesdigest.data.remote.dto.scheapshark.Info
import com.example.gamesdigest.data.remote.dto.scheapshark.StoreDto
import com.example.gamesdigest.domain.repository.CheapSharkRepository
import java.lang.Exception

class FakeCheapSharkRepository : CheapSharkRepository {

    lateinit var gameEditions: List<CheapSharkGameEditionDto>

    lateinit var deals: HashMap<Int, CheapSharkGameEditionDeals>

    lateinit var stores: List<StoreDto>

    init {
        fillDeals()
        fillGames()
        fillStores()
    }


    override suspend fun getGameEditions(name: String) = gameEditions.filter { it.name == name }

    override suspend fun getDeals(id: Int) = deals[id] ?: throw  Exception()

    override suspend fun getStores() = stores

    override suspend fun alertWhenPriceLess(email: String, id: Int, desiredPrice: Float): Boolean {
        return true
    }

    fun fillGames() {
        gameEditions = List(5) {
            CheapSharkGameEditionDto(
                cheapest = "$it.0",
                name = "fake${it%2}",
                gameID = "$it",
                thumb = "fake$it"
            )
        }
        println(gameEditions.toString())
    }

    fun fillDeals() {
        val _deals = List(5) {
            CheapSharkDeal(
                dealID = "$it",
                price = it.toFloat(),
                retailPrice = 5.0f,
                savings = (5.0f - it) / 5.0f,
                storeID = "$it"
            )
        }
        deals = HashMap()

        val witcherDeals = CheapSharkGameEditionDeals(
            _deals,
            Info("fake", "fake")
        )
        deals+=1 to witcherDeals
    }

    fun fillStores(){
        stores = List(5){
            StoreDto(
                Images("fake$it", "fake$it","fake$it"),
                1,
                "$it",
                "name$it"
            )
        }
    }

}
