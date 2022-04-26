package com.co.mercapp.domain.base

sealed class Result<out T> {
    class Success<T>(val data: T) : Result<T>()
    class Failure(val error: Error) : Result<Nothing>()
}
