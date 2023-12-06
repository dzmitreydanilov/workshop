package com.ddanilov.workshop.screens.home

import com.ddanilov.workshop.core.UseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetRandomItemsUseCase @Inject constructor(
    private val repository: RandonmStringItemsRepository
) : UseCase<GenerateRandomItemsAction, GenerateRandomItemsResult> {

    override fun apply(action: GenerateRandomItemsAction): Flow<GenerateRandomItemsResult> {
        return repository.getItems().flatMapConcat { result ->
            result.fold({
                flowOf(GenerateRandomItemsResult.Success(it))
            }, {
                flowOf(GenerateRandomItemsResult.Failed(it))
            })
        }.onEach { delay(2_000) }.onStart { emit(GenerateRandomItemsResult.Loading) }
    }

//    fun apply(action: Flow<GenerateRandomItemsAction>): Flow<GenerateRandomItemsResult> {
//        println("XXXX GetRandomItemsUseCase")
//        return action.flatMapConcat {
//            repository.getItems()
//        }.map { result ->
//            result.fold({
//                GenerateRandomItemsResult.Success(it)
//            }, {
//                GenerateRandomItemsResult.Failed(it)
//            })
//        }.onEach { delay(2_000) }.onStart { emit(GenerateRandomItemsResult.Loading) }
//    }
}
