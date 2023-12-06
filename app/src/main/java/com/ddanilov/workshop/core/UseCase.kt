package com.ddanilov.workshop.core

import kotlinx.coroutines.flow.Flow

interface UseCase<A : Action, R : Result> {

    fun apply(action: A): Flow<R>
}
