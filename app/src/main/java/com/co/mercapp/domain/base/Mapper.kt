package com.co.mercapp.domain.base

interface Mapper<I, O> {
    fun map(input: I): O
}