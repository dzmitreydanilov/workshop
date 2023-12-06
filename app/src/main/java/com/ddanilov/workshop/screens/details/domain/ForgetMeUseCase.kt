package com.ddanilov.workshop.screens.details.domain

import com.ddanilov.workshop.core.UseCase
import com.ddanilov.workshop.screens.details.ForgetMeAction
import com.ddanilov.workshop.screens.details.ForgetMeResult
import com.ddanilov.workshop.storage.UserStorageDataStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ForgetMeUseCase @Inject constructor(
    private val statusStorage: UserStorageDataStorage
) : UseCase<ForgetMeAction, ForgetMeResult> {

    override fun apply(action: ForgetMeAction): Flow<ForgetMeResult> {
        return flow {
            emit(ForgetMeResult.Loading)
            statusStorage.forgetMe()
            delay(3_000)
            emit(ForgetMeResult.Success)
        }
    }
}
