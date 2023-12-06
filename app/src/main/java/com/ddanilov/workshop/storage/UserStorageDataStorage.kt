package com.ddanilov.workshop.storage

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val REMEMBER_ME = "REMEMBER_ME"

class UserStorageDataStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun getStatus(): Flow<Result<Boolean>> {
        return flow {
            val isRemembered = sharedPreferences.getBoolean(REMEMBER_ME, false)
            val result =
                if (isRemembered) {
                    Result.success(true)
                } else {
                    Result.failure(Throwable("Uknown User"))
                }

            emit(result)
        }
    }

    fun rememberMe() {
        sharedPreferences.edit().putBoolean(REMEMBER_ME, true).apply()
    }

    fun forgetMe() {
        sharedPreferences.edit().clear().apply()
    }
}
