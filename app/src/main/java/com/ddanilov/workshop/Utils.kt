package com.ddanilov.workshop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ddanilov.workshop.core.BaseViewModel
import com.ddanilov.workshop.core.State
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@Composable
inline fun <reified T : BaseViewModel<S>, reified S : State> T.subscribeAndCollectStateWithLifecycle(): androidx.compose.runtime.State<S> {
    OnLifecycleStartAction(::subscribeToEvents)
    return collectState().collectAsStateWithLifecycle()
}

@Composable
fun OnLifecycleStartAction(action: suspend () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    LifecycleStartEffect(key1 = Unit) {
        coroutineScope.launch {
            println("XXXXX OnLifecycleStartAction")
            action()
        }

        onStopOrDispose {
            coroutineScope.cancel()
        }
    }
}
