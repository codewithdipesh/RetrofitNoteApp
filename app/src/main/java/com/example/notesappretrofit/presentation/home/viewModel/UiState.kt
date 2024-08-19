package com.example.notesappretrofit.presentation.home.viewModel

sealed class UiState<out T> {
    object Initial : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    object NoInternet : UiState<Nothing>()
    object ServerError : UiState<Nothing>()
    object Unauthorized : UiState<Nothing>()
    data class Error(val error: String) : UiState<Nothing>()
}