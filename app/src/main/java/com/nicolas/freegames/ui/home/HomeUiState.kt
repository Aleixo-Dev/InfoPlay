package com.nicolas.freegames.ui.home

import com.nicolas.freegames.models.domain.ModelGame

sealed class HomeUiState {
    object Idle : HomeUiState()
    object Loading : HomeUiState()
    data class Success(val data: List<ModelGame> = listOf()) : HomeUiState()
    data class Error(val error: String) : HomeUiState()
}