package com.example.gamesdigest.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.gamesdigest.data.remote.ITEMS_PER_PAGE
import com.example.gamesdigest.data.remote.api.RawgApi
import com.example.gamesdigest.data.remote.dto.rawg.RawgGameDto
import com.example.gamesdigest.data.remote.dto.rawg.GenreDto
import com.example.gamesdigest.domain.repository.RawgRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RawgRepositoryImpl @Inject constructor(
    private val rawgApi: RawgApi
) : RawgRepository {

    override suspend fun getGames(
        genre: String?,
        year: String?,
        metacriticRating: String?
    ): Pager<Int, RawgGameDto> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
            pagingSourceFactory = { RawgPagingSource(genre, year, metacriticRating, rawgApi) }
        )
    }

    override suspend fun getGenres(): List<GenreDto> =
        withContext(Dispatchers.IO) { rawgApi.getGenresFromRawg().results }

    override suspend fun getGameDetails(id: Int) =
        withContext(Dispatchers.IO) { rawgApi.getGameDetails(id) }
}
