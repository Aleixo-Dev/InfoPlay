package com.nicolas.freegames.data.repository

import com.nicolas.freegames.data.service.FreeGameService
import javax.inject.Inject

class FreeGameDataSourceImpl @Inject constructor(
    private val service: FreeGameService
) : FreeGameDataSource {

    override suspend fun getAllGames() = service.getAllGames()

    override suspend fun getGamesPerCategory(category: String) =
        service.getGamePerCategory(category)

    override suspend fun getGameDetail(gameId: String) = service.getGameDetail(gameId)

}