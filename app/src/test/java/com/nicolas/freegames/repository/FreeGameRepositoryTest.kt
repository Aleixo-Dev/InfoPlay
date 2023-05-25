package com.nicolas.freegames.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nicolas.freegames.data.mapper.toGameDetail
import com.nicolas.freegames.data.repository.FreeGameDataSource
import com.nicolas.freegames.data.repository.FreeGameRepositoryImpl
import com.nicolas.freegames.models.remote.*
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FreeGameRepositoryTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val freeGameDataSource = mockk<FreeGameDataSource>(relaxed = true)
    lateinit var repository: FreeGameRepositoryImpl

    private val gameDetail = GameDetailResponse(
        description = "all of Duty: Warzone is both a standalone free-to-play battle royale and modes accessible via Call of Duty: Modern Warfare.",
        developer = "Infinity Ward",
        freetogameProfileUrl = "https://www.freetogame.com/call-of-duty-warzone",
        gameUrl = "https://www.freetogame.com/open/call-of-duty-warzone",
        genre = "Shooter",
        id = 1,
        minimumSystemRequirements = MinimumSystemRequirements(
            graphics = "NVIDIA GeForce GTX 670 / GeForce GTX 1650 or Radeon HD 7950",
            memory = "8GB RAM",
            os = "Windows 7 64-Bit (SP1) or Windows 10 64-Bit",
            processor = "Intel Core i3-4340 or AMD FX-6300",
            storage = "175GB HD space"
        ),
        platform = "",
        publisher = "",
        releaseDate = "",
        screenshots = listOf(
            Screenshot(
                id = 1,
                image = "https://www.freetogame.com/g/452/Call-of-Duty-Warzone-1.jpg"
            )
        ),
        shortDescription = "A standalone free-to-play battle royale and modes accessible via Call of Duty: Modern Warfare.",
        status = "Live", thumbnail = "https://www.freetogame.com/g/452/thumbnail.jpg",
        title = "Call Of Duty: Warzone"
    )

    @Before
    fun signUp() {
        MockKAnnotations.init(this)
        repository = FreeGameRepositoryImpl(freeGameDataSource)
    }

    @Test
    fun `when getting getAllGames and remote data source has no error then should return empty list`() =
        runBlocking {
            val gameReturn = repository.getAllGames().first()

            coVerify { freeGameDataSource.getAllGames() }

            gameReturn shouldBe listOf()
        }

    @Test
    fun `when getting getGamePerCategory and remote data source has no error then should emit the correct data `() =
        runBlocking {
            val first = repository.getGamesPerCategory(GAME_CATEGORY).first()
            coVerify { freeGameDataSource.getGamesPerCategory(GAME_CATEGORY) }
            first shouldBe listOf()
        }

    @Test
    fun `when getting getGameDetail and remote data source has no error then should emit the correct game detail`() =
        runBlocking {
            addGameDetailStub(gameDetail)
            val first = repository.getGameDetail(GAME_ID).first()

            coVerify { freeGameDataSource.getGameDetail(GAME_ID) }

            first shouldBe gameDetail.toGameDetail()
        }

    @Test
    fun `when getting getAllGames and remote data source has error then should the exception`() {
        val exception = Exception("unexpected server errors")
        coEvery { freeGameDataSource.getAllGames() } throws exception

        val thrownException = assertThrows(Exception::class.java) {
            runBlocking { repository.getAllGames().collect { } }
        }

        exception.message shouldBe thrownException.message
    }

    private fun addGameDetailStub(data: GameDetailResponse) {
        coEvery { freeGameDataSource.getGameDetail(any()) } returns data
    }

    companion object {
        const val GAME_ID = "11"
        const val GAME_CATEGORY = "tower-defense"
    }
}