package com.ddanilov.workshop

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ddanilov.workshop.core.BaseViewModel
import com.ddanilov.workshop.core.State
import kotlinx.coroutines.launch

@Composable
inline fun <reified T : BaseViewModel<S>, reified S : State> T.subscribeAndCollectStateWithLifecycle(
): androidx.compose.runtime.State<S> {
    OnLifecycleStartAction(::subscribeToEvents)
    return collectState().collectAsStateWithLifecycle()
}

@Composable
fun OnLifecycleStartAction(action: suspend () -> Unit) {
    LifecycleResumeEffect {
        lifecycleScope.launch {
            println("XXX OnLifecycleStartAction launch")
            action()
        }

        onPauseOrDispose {
            println("XXX OnLifecycleStartAction cancel")
        }
    }
}
