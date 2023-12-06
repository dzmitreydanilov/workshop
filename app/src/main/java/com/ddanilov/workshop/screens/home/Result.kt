package com.ddanilov.workshop.screens.home

import com.ddanilov.workshop.core.Result

sealed interface GenerateRandomItemsResult : Result {
    data class Success(val items: List<String>) : GenerateRandomItemsResult
    data object Loading : GenerateRandomItemsResult
    data class Failed(val error: Throwable) : GenerateRandomItemsResult
}
