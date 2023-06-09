package com.nicolas.freegames.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicolas.freegames.data.repository.FreeGameRepository
import com.nicolas.freegames.models.domain.ModelGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FreeGameRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameStateUi())
    val uiState: StateFlow<GameStateUi> = _uiState.asStateFlow()

    fun onEvent(event: GameUiEvent) {
        when (event) {
            GameUiEvent.FetchAllGames -> {
                getAllGames()
            }
            is GameUiEvent.FetchCategory -> {
                getCategory(event.category)
            }
        }
    }

    private fun getCategory(category: String) = viewModelScope.launch {
        repository.getGamesPerCategory(category)
            .onStart { setStateGameUi(isLoading = true) }
            .catch { setStateGameUi(hasError = true) }
            .collect { data -> setStateGameUi(data = data, loaded = true) }
    }

    private fun getAllGames() = viewModelScope.launch {
        repository.getAllGames()
            .onStart { setStateGameUi(isLoading = true ) }
            .catch { setStateGameUi(hasError = true) }
            .collect { data -> setStateGameUi(data = data, loaded = true) }
    }

    private fun setStateGameUi(
        isLoading: Boolean = false,
        data: List<ModelGame> = listOf(),
        hasError: Boolean = false,
        loaded: Boolean = false
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = isLoading,
                data = data,
                hasError = hasError,
                loaded = loaded
            )
        }
    }
}