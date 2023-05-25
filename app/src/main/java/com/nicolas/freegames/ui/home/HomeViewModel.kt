package com.nicolas.freegames.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicolas.freegames.data.repository.FreeGameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FreeGameRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

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
            .onStart { setState(HomeUiState.Loading) }
            .catch { setState(HomeUiState.Error(it.message.toString())) }
            .collect { data -> setState(newState = HomeUiState.Success(data = data)) }
    }

    private fun getAllGames() = viewModelScope.launch {
        repository.getAllGames()
            .onStart { setState(HomeUiState.Loading) }
            .catch { setState(HomeUiState.Error(it.message.toString())) }
            .collect { data -> setState(newState = HomeUiState.Success(data = data)) }
    }


    private fun setState(newState: HomeUiState) {
        _uiState.value = newState
    }
}