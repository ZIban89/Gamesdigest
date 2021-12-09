package com.example.gamesdigest.common

import com.example.gamesdigest.data.remote.dto.firestore.SubscriptionDto
import com.example.gamesdigest.data.remote.dto.rawg.RawgGameDetailsDto
import com.example.gamesdigest.data.remote.dto.rawg.RawgGameDto
import com.example.gamesdigest.data.remote.dto.scheapshark.CheapSharkGameEditionDto
import com.example.gamesdigest.data.remote.dto.scheapshark.StoreDto
import com.example.gamesdigest.domain.model.Game
import com.example.gamesdigest.domain.model.GameDetails
import com.example.gamesdigest.domain.model.GameEdition
import com.example.gamesdigest.domain.model.Genre
import com.example.gamesdigest.domain.model.Platform
import com.example.gamesdigest.domain.model.Publisher
import com.example.gamesdigest.domain.model.Store
import com.example.gamesdigest.domain.model.Subscription
import com.example.gamesdigest.domain.model.Tag

fun RawgGameDto.toGame() = Game(
    id = id,
    name = name ?: "",
    released = released?.toLocalDate() ?: "Unknown",
    backgroundImage = backgroundImage ?: "",
    rating = rating ?: 0.0,
    ratingTop = ratingTop ?: 0,
    ratingsCount = ratingsCount ?: 0,
    metacritic = metacritic ?: 0,
    parentPlatforms = parentPlatforms
)

fun RawgGameDetailsDto.toGameDetails() = GameDetails(
    id = id ?: 0,
    name = name ?: "Unknown",
    released = released?.toLocalDate() ?: "Unknown",
    backgroundImage = backgroundImage ?: "",
    rating = rating ?: 0.0,
    ratingTop = ratingTop ?: 0,
    ratingsCount = ratingsCount ?: 0,
    metacritic = metacritic ?: 0,
    playtime = playtime ?: 0,
    genres = genres.map { genre -> Genre(genre.id, genre.name) },
    parentPlatforms = parentPlatforms.map { platform ->
        Platform(
            platform.platform.id,
            platform.platform.name
        )
    },
    tags = tags.map { tag -> Tag(tag.id, tag.name) },
    website = website ?: "",
    description = description ?: "",
    publishers = publishers.map { publisher -> Publisher(publisher.id, publisher.name) },
    slug = slug ?: "Unknown"
)

fun CheapSharkGameEditionDto.toGameEdition() = GameEdition(
    cheapest = cheapest,
    name = name,
    gameId = gameID,
    thumb = thumb
)

fun StoreDto.toStore() = Store(
    image = "https://www.cheapshark.com${images.logo}",
    storeID = storeID,
    storeName = storeName
)

fun SubscriptionDto.toSubscription() = Subscription(
    id = id,
    gameId = gameId,
    gameEditionName = gameEditionName,
    desiredPrice = price
)
