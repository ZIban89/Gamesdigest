package com.example.gamesdigest.fakeRepository

import androidx.paging.Pager
import com.example.gamesdigest.data.remote.dto.rawg.RawgGameDetailsDto
import com.example.gamesdigest.data.remote.dto.rawg.RawgGameDto
import com.example.gamesdigest.data.remote.dto.rawg.GenreDto
import com.example.gamesdigest.domain.repository.RawgRepository

class FakeRawgRepository: RawgRepository {

    val genres = listOf(GenreDto(1, "fakeGenre1"), GenreDto(2, "fakeGenre2"))
    val games = listOf(RawgGameDetailsDto(1,
        "The witcher",
        "2015-01-01",
        "fake",
        5.0, 5
        ,
        666,
        99,
        100,
        emptyList(),
        emptyList(),
        emptyList(),
        "fake",
        "fake",
        emptyList(),
        "the-witcher"
    ))


    override suspend fun getGames(
        genre: String?,
        year: String?,
        metacriticRating: String?
    ): Pager<Int, RawgGameDto> {
        throw Exception()
    }


    override suspend fun getGenres() = genres

    override suspend fun getGameDetails(id: Int) = games.first { it.id == id }
}