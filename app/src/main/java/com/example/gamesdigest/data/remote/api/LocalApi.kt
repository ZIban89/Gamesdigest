package com.example.gamesdigest.data.remote.api

import com.example.gamesdigest.data.remote.dto.rawg.RawgGameDetailsDto
import com.example.gamesdigest.data.remote.dto.rawg.RawgGameDto
import com.example.gamesdigest.data.remote.dto.rawg.GenreDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocalApi {

    @GET("genres")
    suspend fun getGenresFromRawg(): Response<List<GenreDto>>

    @GET("games")
    suspend fun getGamesFromRawg(
        @Query("_page") page: Int,
        @Query("_limit") pageSize: Int,
        @Query("genres") genres: String? = null,
        @Query("dates") released: String? = null,
        @Query("metacritic") metacritic: String? = null
    ): Response<List<RawgGameDto>>

    @GET("games/{gameId}")
    suspend fun getGameDetails(
        @Path("gameId") id: Int
    ): RawgGameDetailsDto
}
