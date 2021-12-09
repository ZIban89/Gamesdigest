package com.example.gamesdigest.domain.model

data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String = ""
)
