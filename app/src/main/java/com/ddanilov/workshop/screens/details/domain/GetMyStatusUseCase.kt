package com.ddanilov.workshop.screens.details.domain

import com.ddanilov.workshop.core.UseCase
import com.ddanilov.workshop.screens.details.GetMyStatusAction
import com.ddanilov.workshop.screens.details.GetUserStatusResult
import com.ddanilov.workshop.storage.UserStorageDataStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetMyStatusUseCase @Inject constructor(
    private val statusStorage: UserStorageDataStorage
) : UseCase<GetMyStatusAction, GetUserStatusResult> {

    override fun apply(action: GetMyStatusAction): Flow<GetUserStatusResult> {
        return statusStorage.getStatus().flatMapConcat {
            delay(2_000)
            it.fold(
                onSuccess = {
                    flowOf(GetUserStatusResult.Success)
                },
                onFailure = {
                    flowOf(GetUserStatusResult.Failed(it))
                }
            )
        }
            .onStart { emit(GetUserStatusResult.Loading) }
    }
}
