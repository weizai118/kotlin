/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin

fun createTextForHelpers(coroutinesPackage: String): String {
    val isReleaseCoroutines = !coroutinesPackage.contains("experimental")

    val emptyContinuationBody =
        if (isReleaseCoroutines)
            """
                |override fun resumeWith(result: SuccessOrFailure<Any?>) {
                |   result.getOrThrow()
                |}
            """.trimMargin()
        else
            """
                |override fun resume(data: Any?) {}
                |override fun resumeWithException(exception: Throwable) { throw exception }
            """.trimMargin()

    val handleResultContinuationBody =
        if (isReleaseCoroutines)
            """
                |override fun resumeWith(result: SuccessOrFailure<T>) {
                |   x(result.getOrThrow())
                |}
            """.trimMargin()
        else
            """
                |override fun resumeWithException(exception: Throwable) {
                |   throw exception
                |}
                |
                |override fun resume(data: T) = x(data)
            """.trimMargin()

    val handleExceptionContinuationBody =
        if (isReleaseCoroutines)
            """
                |override fun resumeWith(result: SuccessOrFailure<Any?>) {
                |   result.exceptionOrNull()?.let(x)
                |}
            """.trimMargin()
        else
            """
                |override fun resumeWithException(exception: Throwable) {
                |   x(exception)
                |}
                |
                |override fun resume(data: Any?) {}
            """.trimMargin()


    return """
            |package helpers
            |import $coroutinesPackage.*
            |
            |fun <T> handleResultContinuation(x: (T) -> Unit): Continuation<T> = object: Continuation<T> {
            |    override val context = EmptyCoroutineContext
            |    $handleResultContinuationBody
            |}
            |
            |
            |fun handleExceptionContinuation(x: (Throwable) -> Unit): Continuation<Any?> = object: Continuation<Any?> {
            |    override val context = EmptyCoroutineContext
            |    $handleExceptionContinuationBody
            |}
            |
            |open class EmptyContinuation(override val context: CoroutineContext = EmptyCoroutineContext) : Continuation<Any?> {
            |    companion object : EmptyContinuation()
            |    $emptyContinuationBody
            |}
        """.trimMargin()
}
