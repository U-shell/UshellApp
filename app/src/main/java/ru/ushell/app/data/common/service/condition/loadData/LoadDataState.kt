package ru.ushell.app.data.common.service.condition.loadData

sealed interface LoadDataState {

    object Empty : LoadDataState
    object Loading : LoadDataState
    data class Success(val isLogin: Boolean) : LoadDataState
    data class Error(val message: String) : LoadDataState
}