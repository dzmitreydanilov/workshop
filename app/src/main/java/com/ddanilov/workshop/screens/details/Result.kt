package com.ddanilov.workshop.screens.details

import com.ddanilov.workshop.core.Result

sealed interface RememberMeResult : Result {

    data object Success : RememberMeResult
    data object Loading : RememberMeResult
    data object Failed : RememberMeResult
}

sealed interface ForgetMeResult : Result {

    data object Success : ForgetMeResult
    data object Loading : ForgetMeResult
    data object Failed : ForgetMeResult
}

sealed interface GetUserStatusResult : Result {

    data object Success : GetUserStatusResult
    data object Loading : GetUserStatusResult
    data class Failed(val error: Throwable) : GetUserStatusResult
}
