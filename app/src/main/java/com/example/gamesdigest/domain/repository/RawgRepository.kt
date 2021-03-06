package com.example.gamesdigest.domain.repository

import androidx.paging.Pager
import com.example.gamesdigest.data.remote.dto.rawg.RawgGameDetailsDto
import com.example.gamesdigest.data.remote.dto.rawg.RawgGameDto
import com.example.gamesdigest.data.remote.dto.rawg.GenreDto

interface RawgRepository {

    suspend fun getGames(
        genre: String?,
        year: String?,
        metacriticRating: String?
    ): Pager<Int, RawgGameDto>

    suspend fun getGenres(): List<GenreDto>

    suspend fun getGameDetails(id: Int): RawgGameDetailsDto
}
