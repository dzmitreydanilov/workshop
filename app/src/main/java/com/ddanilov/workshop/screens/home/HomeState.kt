package com.ddanilov.workshop.screens.home

import com.ddanilov.workshop.core.State
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed class HomeState(open val items: ImmutableList<String>) : State {

    data object Initial : HomeState(persistentListOf())

    data class Loading(override val items: ImmutableList<String>) : HomeState(items)

    data class Loaded(override val items: ImmutableList<String>) : HomeState(items)

    data class Error(
        val error: String,
        override val items: ImmutableList<String>
    ) : HomeState(items)
}
