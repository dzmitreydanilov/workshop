package com.ddanilov.workshop.screens.home

import androidx.lifecycle.viewModelScope
import com.ddanilov.workshop.core.Action
import com.ddanilov.workshop.core.BaseViewModel
import com.ddanilov.workshop.core.Event
import com.ddanilov.workshop.core.Navigation
import com.ddanilov.workshop.core.Result
import com.ddanilov.workshop.screens.details.GetMyStatusAction
import com.ddanilov.workshop.screens.details.GetUserStatusResult
import com.ddanilov.workshop.screens.details.domain.GetMyStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val generateRandomItemsUseCase: GetRandomItemsUseCase,
    private val getMyStatusUseCase: GetMyStatusUseCase
) : BaseViewModel<HomeState>(HomeState.Initial) {

    init {
        viewModelScope.launch {
            generateRandomItemsUseCase.apply(GenerateRandomItemsAction).updateState().collect()
        }
        viewModelScope.launch {
            getMyStatusUseCase.apply(GetMyStatusAction).updateState().collect()
        }
    }

    override fun processEvent(event: Event): Flow<Result> {
        return processAction(toAction(event))
    }

    private fun toResult(event: Event): Result {
        return object : Result {
        }
    }

    override fun produceState(previous: HomeState, result: Result): HomeState {
        println("XXX produceState result ${result::class.java}")
        return when (result) {
            is GetUserStatusResult.Success -> {
                HomeState.Loaded(items = statePublisher.value.items)
            }

            is GetUserStatusResult.Failed -> {
                HomeState.Loaded(items = statePublisher.value.items)
            }

            is GenerateRandomItemsResult.Success -> {
                HomeState.Loaded(
                    items = result.items.toPersistentList()
                )
            }

            is GenerateRandomItemsResult.Loading, GetUserStatusResult.Loading -> {
                HomeState.Loading(items = statePublisher.value.items)
            }

            is GenerateRandomItemsResult.Failed -> {
                HomeState.Error(
                    error = result.error.message.orEmpty(),
                    items = previous.items
                )
            }

            else -> super.produceState(previous, result)
        }
    }

    override fun processAction(action: Action): Flow<Result> {
        return when (action) {
            is GenerateRandomItemsAction -> generateRandomItemsUseCase.apply(action)
            else -> super.processAction(action)
        }
    }

    private fun toAction(event: Event): Action {
        return when (event) {
            is GenerateRandomItemsList -> GenerateRandomItemsAction
            else -> object : Action {}
        }
    }

    override fun getNavigationByResult(result: Result): Navigation? {
        return when (result) {
            is GetUserStatusResult.Success -> NavigateDetails
            else -> null
        }
    }

    override fun getNavigationResults(): Set<Class<out Result>> {
        return emptySet()
    }

    override fun produceErrorState(throwable: Throwable): HomeState {
        return HomeState.Error(throwable.message.orEmpty(), statePublisher.value.items)
    }
}

data object NavigateDetails : Navigation
