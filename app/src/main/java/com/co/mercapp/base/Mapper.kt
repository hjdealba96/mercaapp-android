package com.co.mercapp.base

interface Mapper<I, O> {
    fun map(input: I): O
}