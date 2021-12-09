package com.example.gamesdigest.data.repository.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gamesdigest.data.remote.ITEMS_PER_PAGE
import com.example.gamesdigest.data.remote.START_PAGE
import com.example.gamesdigest.data.remote.api.LocalApi
import com.example.gamesdigest.data.remote.dto.rawg.RawgGameDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class LocalRawgPagingSource constructor(
    private val genre: String?,
    private val year: String?,
    private val metacriticRating: String?,
    private val api: LocalApi
) : PagingSource<Int, RawgGameDto>() {

    override fun getRefreshKey(state: PagingState<Int, RawgGameDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RawgGameDto> {
        val position = params.key ?: START_PAGE

        return try {
            val rawgGamesResponce = withContext(Dispatchers.IO) {
                api.getGamesFromRawg(
                    page = position,
                    pageSize = ITEMS_PER_PAGE,
                    genres = genre,
                    released = if (!year.isNullOrEmpty()) "$year-01-01,2099-12-31" else null,
                    metacritic = "$metacriticRating,100"
                )
            }
            if (!rawgGamesResponce.isSuccessful) {
                throw HttpException(rawgGamesResponce)
            }
            val rawgGamesList: List<RawgGameDto> = rawgGamesResponce.body() ?: emptyList()
            val nextKey = if (rawgGamesList.isEmpty() || rawgGamesList.size < ITEMS_PER_PAGE) {
                null
            } else {
                position + (rawgGamesList.size / ITEMS_PER_PAGE)
            }
            LoadResult.Page(
                data = rawgGamesList,
                prevKey = if (position == START_PAGE) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}
