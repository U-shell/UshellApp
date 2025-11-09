package ru.ushell.app.domain.service.loadData

sealed interface LoadDataState {

    object Empty : LoadDataState
    object Loading : LoadDataState
    data class Success(val status: Boolean) : LoadDataState
    data class Error(val message: String) : LoadDataState
}