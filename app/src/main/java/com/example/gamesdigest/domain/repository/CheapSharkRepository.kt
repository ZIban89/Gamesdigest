package com.example.gamesdigest.domain.repository

import com.example.gamesdigest.data.remote.dto.scheapshark.CheapSharkGameEditionDeals
import com.example.gamesdigest.data.remote.dto.scheapshark.CheapSharkGameEditionDto
import com.example.gamesdigest.data.remote.dto.scheapshark.StoreDto

interface CheapSharkRepository {

    suspend fun getGameEditions(name: String): List<CheapSharkGameEditionDto>

    suspend fun getDeals(id: Int): CheapSharkGameEditionDeals

    suspend fun getStores(): List<StoreDto>

    suspend fun alertWhenPriceLess(email: String, id: Int, desiredPrice: Float): Boolean
}
