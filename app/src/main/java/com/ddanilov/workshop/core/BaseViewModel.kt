package com.ddanilov.workshop.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions", "UnusedPrivateMember")
abstract class BaseViewModel<S : State>(
    private val initialState: S,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    protected val statePublisher = MutableStateFlow(initialState)
    private val navigationFlow = MutableSharedFlow<Navigation>(extraBufferCapacity = 1)
    private val eventPublisher = MutableSharedFlow<Event>()

    private suspend fun navigate(result: Result) {
        val navigation = getNavigationByResult(result)
        navigation?.let {
            navigationFlow.emit(navigation)
        }
    }

    private fun reduceState(result: Flow<Result>): Flow<S> {
        return result
            .onEach(::navigate)
            .filter { item -> getNavigationResults().none { it.isInstance(item) } }
            .scan(statePublisher.value, ::produceStateWithErrorHandling)
    }

    fun dispatchEvent(event: Event) {
        viewModelScope.launch {
            eventPublisher.emit(event)
        }
    }

    open suspend fun subscribeToEvents() {
        eventPublisher
            .flatMapConcat { reduceState(processEvent(it)) }
            .distinctUntilChanged()
            .collect(statePublisher::emit)
    }

    protected fun Flow<Result>.updateState(): Flow<S> {
        return reduceState(this).onEach { statePublisher.emit(it) }
    }

    fun collectState(): StateFlow<S> = statePublisher

    fun collectNavigation(): Flow<Navigation> = navigationFlow

    protected open fun processEvent(event: Event): Flow<Result> {
        return emptyFlow()
    }

    protected open fun processAction(action: Action): Flow<Result> {
        return emptyFlow()
    }

    protected open fun processActionReactive(action: Flow<Action>): Flow<Result> {
        return emptyFlow()
    }


    protected open fun getNavigationByResult(result: Result): Navigation? = null

    protected open fun getNavigationResults(): Set<Class<out Result>> {
        return emptySet()
    }

    @Suppress("TooGenericExceptionCaught")
    private fun produceStateWithErrorHandling(previous: S, result: Result): S {
        return try {
            produceState(previous, result)
        } catch (throwable: Throwable) {
            produceErrorState(throwable)
        }
    }

    abstract fun produceErrorState(throwable: Throwable): S

    protected open fun produceState(
        previous: S,
        result: Result
    ): S {
        return if (result is ErrorResult) {
            produceErrorState(result.error)
        } else {
            previous
        }
    }
}
