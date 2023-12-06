package com.ddanilov.workshop.screens.details

import androidx.lifecycle.viewModelScope
import com.ddanilov.workshop.core.Action
import com.ddanilov.workshop.core.BaseViewModel
import com.ddanilov.workshop.core.Event
import com.ddanilov.workshop.core.Result
import com.ddanilov.workshop.screens.details.domain.ForgetMeUseCase
import com.ddanilov.workshop.screens.details.domain.GetMyStatusUseCase
import com.ddanilov.workshop.screens.details.domain.RememberMeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomItemDetailsViewModel @Inject constructor(
    private val getMyStatusUseCase: GetMyStatusUseCase,
    private val rememberMeUseCase: RememberMeUseCase,
    private val forgetMeUseCase: ForgetMeUseCase
) : BaseViewModel<RandomItemDetailState>(RandomItemDetailState.Initial) {

    init {
        viewModelScope.launch {
            getMyStatusUseCase.apply(GetMyStatusAction).updateState().collect()
        }
    }

    override fun processEvent(event: Event): Flow<Result> {
        return merge(
            processAction(toAction(event)), flowOf(toResult(event))
        )
    }

    override fun processAction(action: Action): Flow<Result> {
        return when (action) {
            is RememberMeActon -> rememberMeUseCase.apply(action)
            is ForgetMeAction -> forgetMeUseCase.apply(action)
            else -> super.processAction(action)
        }
    }

    override fun produceState(
        previous: RandomItemDetailState, result: Result
    ): RandomItemDetailState {
        return when (result) {
            is RememberMeResult.Success -> {
                RandomItemDetailState.RememberMeSuccess(isUserSaved = true)
            }

            is GetUserStatusResult.Success -> {
                RandomItemDetailState.Loaded(isUserSaved = true)
            }

            is ForgetMeResult.Success -> {
                RandomItemDetailState.ForgotMeSuccess(isUserSaved = false)
            }

            is ForgetMeResult.Loading, RememberMeResult.Loading -> {
                RandomItemDetailState.Loading(isUserSaved = statePublisher.value.isUserSaved)
            }

            is AreYouSureResult -> {
                RandomItemDetailState.ShowAreYouSureDialog(isUserSaved = previous.isUserSaved)
            }

            is DismissDialogClickResult -> {
                RandomItemDetailState.Loaded(isUserSaved = statePublisher.value.isUserSaved)
            }

            else -> super.produceState(previous, result)
        }
    }

    private fun toResult(event: Event): Result {
        return when (event) {
            is ForgetMeEvent -> AreYouSureResult
            is AlertDialogDismissClickEvent -> DismissDialogClickResult
            else -> object : Result {}
        }
    }

    private fun toAction(event: Event): Action {
        return when (event) {
            is AlertDialogConfirmClickEvent -> ForgetMeAction
            is RememberMeEvent -> RememberMeActon
            else -> object : Action {}
        }
    }

    override fun produceErrorState(throwable: Throwable): RandomItemDetailState {
        return RandomItemDetailState.Error(
            error = throwable.message.orEmpty(), isUserSaved = statePublisher.value.isUserSaved
        )
    }
}


data object AreYouSureResult : Result
data object DismissDialogClickResult : Result
