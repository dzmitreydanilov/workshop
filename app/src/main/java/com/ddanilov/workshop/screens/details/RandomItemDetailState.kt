package com.ddanilov.workshop.screens.details

import com.ddanilov.workshop.core.State

sealed class RandomItemDetailState(
    open val isUserSaved: Boolean = false
) : State {

    data object Initial : RandomItemDetailState()
    data class Loading(override val isUserSaved: Boolean) : RandomItemDetailState(isUserSaved)
    data class Loaded(override val isUserSaved: Boolean) : RandomItemDetailState(isUserSaved)
    data class RememberMeSuccess(
        override val isUserSaved: Boolean
    ) : RandomItemDetailState(isUserSaved)

    data class ForgotMeSuccess(
        override val isUserSaved: Boolean
    ) : RandomItemDetailState(isUserSaved)

    data class ShowAreYouSureDialog(
        override val isUserSaved: Boolean
    ) : RandomItemDetailState(isUserSaved)

    data class Error(
        val error: String,
        override val isUserSaved: Boolean
    ) : RandomItemDetailState(isUserSaved)
}
