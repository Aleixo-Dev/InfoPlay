package com.nicolas.freegames.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nicolas.freegames.data.FakeData
import com.nicolas.freegames.data.repository.FreeGameRepository
import com.nicolas.freegames.ui.home.GameUiEvent
import com.nicolas.freegames.ui.home.HomeUiState
import com.nicolas.freegames.ui.home.HomeViewModel
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val myRepository = mockk<FreeGameRepository>(relaxed = true)
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = HomeViewModel(repository = myRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `when getting fetchAllGames should return loading `() {
        viewModel.onEvent(event = GameUiEvent.FetchAllGames)
        coVerify { myRepository.getAllGames() }
        viewModel.uiState.value shouldBe HomeUiState.Loading
    }

    @Test
    fun `when getting fetchAllGames state should return success`() {
        coEvery { myRepository.getAllGames() } returns flowOf(FakeData.provideAllGamesData())
        viewModel.onEvent(event = GameUiEvent.FetchAllGames)
        coVerify { myRepository.getAllGames() }
        viewModel.uiState.value shouldBe HomeUiState.Success(FakeData.provideAllGamesData())
    }

    @Test
    fun `when getting fetchAllGames state should return catch `() = runTest {
        coEvery { myRepository.getAllGames() } returns flow { throw Exception("unexpected server errors") }
        viewModel.onEvent(GameUiEvent.FetchAllGames)
        coVerify { myRepository.getAllGames() }
        viewModel.uiState.value shouldBe HomeUiState.Error("unexpected server errors")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }
}