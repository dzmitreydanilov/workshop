package com.ddanilov.workshop.screens.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import kotlin.random.Random

class RandonmStringItemsRepository @Inject constructor() {

    fun getItems(): Flow<Result<List<String>>> {
        return flowOf(Result.success(generateListOfRandomStrings(70, 70)))
    }
}

private fun generateRandomString(length: Int): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}

private fun generateListOfRandomStrings(count: Int, length: Int): List<String> {
    return List(count) { generateRandomString(length) }
}

