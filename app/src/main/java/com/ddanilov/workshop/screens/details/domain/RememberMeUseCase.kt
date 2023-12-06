package com.ddanilov.workshop.screens.details.domain

import com.ddanilov.workshop.core.UseCase
import com.ddanilov.workshop.screens.details.RememberMeActon
import com.ddanilov.workshop.screens.details.RememberMeResult
import com.ddanilov.workshop.storage.UserStorageDataStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RememberMeUseCase @Inject constructor(
    private val statusStorage: UserStorageDataStorage
) : UseCase<RememberMeActon, RememberMeResult> {

    override fun apply(action: RememberMeActon): Flow<RememberMeResult> {
        return flow {
            emit(RememberMeResult.Loading)
            statusStorage.rememberMe()
            delay(3_000)
            emit(RememberMeResult.Success)
        }
    }
}
