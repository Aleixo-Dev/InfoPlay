package com.nicolas.freegames.data.repository

import com.nicolas.freegames.models.remote.GameDetailResponse
import com.nicolas.freegames.models.remote.GameResponse

interface FreeGameDataSource {

    suspend fun getAllGames() : GameResponse
    suspend fun getGamesPerCategory(category : String) : GameResponse
    suspend fun getGameDetail(gameId : String) : GameDetailResponse

}