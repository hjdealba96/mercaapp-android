package com.co.mercapp.domain.base

interface UseCase<P, R> {
    suspend fun perform(params: P): Result<R>
}