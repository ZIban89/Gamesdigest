package com.example.gamesdigest.data.remote.api

import com.example.gamesdigest.data.remote.dto.rawg.RawgGameDetailsDto
import com.example.gamesdigest.data.remote.dto.rawg.RawgGamesApiData
import com.example.gamesdigest.data.remote.dto.rawg.Responce
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val API_KEY = "90c0007031374b668f56b8e152c5e62b"

interface RawgApi {

    @GET("genres")
    suspend fun getGenresFromRawg(
        @Query("key") key: String = API_KEY
    ): Responce

    @GET("games")
    suspend fun getGamesFromRawg(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("genres") genres: String? = null,
        @Query("dates") released: String? = null,
        @Query("metacritic") metacritic: String? = null,
        @Query("key") key: String = API_KEY
    ): Response<RawgGamesApiData>

    @GET("games/{gameId}")
    suspend fun getGameDetails(
        @Path("gameId") id: Int,
        @Query("key") key: String = API_KEY
    ): RawgGameDetailsDto
}
