package com.nicolas.freegames.data.repository

import com.nicolas.freegames.data.mapper.toData
import com.nicolas.freegames.data.mapper.toGameDetail
import com.nicolas.freegames.data.service.FreeGameService
import com.nicolas.freegames.models.domain.DetailGame
import com.nicolas.freegames.models.domain.ModelGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FreeGameRepositoryImpl @Inject constructor(
    private val dataSource: FreeGameDataSource
) : FreeGameRepository {

    override suspend fun getAllGames(): Flow<List<ModelGame>> = flow {
        emit(dataSource.getAllGames().toData())
    }

    override suspend fun getGamesPerCategory(category: String): Flow<List<ModelGame>> = flow {
        emit(dataSource.getGamesPerCategory(category = category).toData())
    }

    override suspend fun getGameDetail(gameId: String): Flow<DetailGame> = flow {
        emit(dataSource.getGameDetail(gameId).toGameDetail())
    }
}