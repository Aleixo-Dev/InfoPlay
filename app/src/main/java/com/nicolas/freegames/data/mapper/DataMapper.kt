package com.nicolas.freegames.data.mapper

import com.nicolas.freegames.models.domain.DetailGame
import com.nicolas.freegames.models.domain.ModelGame
import com.nicolas.freegames.models.domain.ScreenshotsGame
import com.nicolas.freegames.models.domain.SystemRequirements
import com.nicolas.freegames.models.remote.GameResponse
import com.nicolas.freegames.models.remote.GameResponseItem
import com.nicolas.freegames.models.remote.GameDetailResponse
import com.nicolas.freegames.models.remote.MinimumSystemRequirements
import com.nicolas.freegames.models.remote.Screenshot

fun GameResponseItem.toDataItem() =
    ModelGame(
        id = id.toString(),
        title = title,
        genre = genre,
        description = shortDescription,
        platform = platform,
        thumbnail = thumbnail,
        publisher = publisher,
        developer = developer
    )

fun GameResponse.toData() = map { it.toDataItem() }

fun Screenshot.toScreenshotGame() = ScreenshotsGame(id = id.toString(), image = image)

fun GameDetailResponse.toGameDetail() =
    DetailGame(
        title = title,
        status = status,
        shortDescription = shortDescription,
        gameUrl = gameUrl,
        genre = genre,
        platform = platform,
        publisher = publisher,
        developer = developer,
        dateGame = releaseDate, description = description,
        systemRequirements = SystemRequirements(
            os = minimumSystemRequirements?.os,
            processor = minimumSystemRequirements?.processor,
            memory = minimumSystemRequirements?.memory,
            graphics = minimumSystemRequirements?.graphics,
            storage = minimumSystemRequirements?.storage
        ),
        screenshotsGame = screenshots?.map { it.toScreenshotGame() }
    )


