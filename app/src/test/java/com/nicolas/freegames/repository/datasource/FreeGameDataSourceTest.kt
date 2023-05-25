package com.nicolas.freegames.repository.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nicolas.freegames.data.repository.FreeGameDataSourceImpl
import com.nicolas.freegames.data.service.FreeGameService
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FreeGameDataSourceTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val service = mockk<FreeGameService>(relaxed = true)
    private lateinit var dataSource: FreeGameDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = FreeGameDataSourceImpl(service)
    }

    @Test
    fun `when getting all games then should call service properly`() {
        runBlocking {
            dataSource.getAllGames()
            coVerify { service.getAllGames() }
        }
    }

    @Test
    fun `when getting game detail then should call service properly`() {
        runBlocking {
            dataSource.getGameDetail(GAME_ID)
            coVerify { service.getGameDetail(GAME_ID) }
        }
    }

    @Test
    fun `when getting game per category then should call service properly`() {
        runBlocking {
            dataSource.getGamesPerCategory(GAME_CATEGORY)
            coVerify { service.getGamePerCategory(GAME_CATEGORY) }
        }
    }

    @After
    fun unMockk() {
        unmockkAll()
    }

    companion object {
        private const val GAME_ID = "403"
        private const val GAME_CATEGORY = "tower-defense"
    }
}