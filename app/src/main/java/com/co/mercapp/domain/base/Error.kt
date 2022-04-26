package com.co.mercapp.domain.base

sealed class Error {
    class RemoteResponseError(val code: Int, val message: String? = null) : Error()
    class TimedOutOperationError(val exception: Exception) : Error()
    class FailedOperationError(val exception: Exception) : Error()
}
