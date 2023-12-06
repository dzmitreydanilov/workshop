package com.ddanilov.workshop.core

/**
 * Probably we need to move it out of core. It's more part of the domain logic.
 */

interface Result

interface ErrorResult : Result {

    val error: Throwable
}
