package com.co.mercapp.data.products.networking

import com.co.mercapp.domain.base.Error
import com.co.mercapp.domain.base.Result
import timber.log.Timber
import java.net.SocketTimeoutException

inline fun <T> networkRequest(body: () -> Result<T>): Result<T> =
    try {
        body()
    } catch (exception: Exception) {
        Timber.e(exception, message = "Something went wrong while making a network request!")
        if (exception is SocketTimeoutException) {
            Result.Failure(Error.TimedOutOperationError(exception))
        } else {
            Result.Failure(Error.FailedOperationError(exception))
        }
    }