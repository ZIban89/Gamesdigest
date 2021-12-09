package com.example.gamesdigest.data.remote.api

import com.example.gamesdigest.data.remote.dto.scheapshark.CheapSharkGameEditionDeals
import com.example.gamesdigest.data.remote.dto.scheapshark.CheapSharkGameEditionDto
import com.example.gamesdigest.data.remote.dto.scheapshark.StoreDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CheapSharkApi {

    @GET("games")
    suspend fun getGameEditions(
        @Query("title") title: String
    ): List<CheapSharkGameEditionDto>

    @GET("games")
    suspend fun getDeals(
        @Query("id") id: Int
    ): CheapSharkGameEditionDeals

    @GET("stores")
    suspend fun getStores(): List<StoreDto>

    @GET("alerts")
    suspend fun alertWhenPriceLess(
        @Query("email") email: String,
        @Query("gameID") id: Int,
        @Query("price") desiredPrice: Float,
        @Query("action") action: String = "set"
    ): Boolean
}
