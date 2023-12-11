package com.ddanilov.workshop.screens.home

import com.ddanilov.workshop.core.UseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetRandomItemsUseCase @Inject constructor(
    private val repository: RandonmStringItemsRepository
) : UseCase<GenerateRandomItemsAction, GenerateRandomItemsResult> {

    override fun apply(action: GenerateRandomItemsAction): Flow<GenerateRandomItemsResult> {
        return repository.getItems().flatMapConcat { result ->
            delay(2_000)
            result.fold({
                flowOf(GenerateRandomItemsResult.Success(it))
            }, {
                flowOf(GenerateRandomItemsResult.Failed(it))
            }
            )
        }.onStart { emit(GenerateRandomItemsResult.Loading) }
    }
}
