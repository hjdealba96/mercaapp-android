package com.co.mercapp.domain.base

data class PaginatedDataEntity<T>(
    val total: Int,
    val pages: Int,
    val items: List<T>
) {
    fun isNotEmpty(): Boolean = items.isNotEmpty()
    fun isEmpty(): Boolean = items.isEmpty()
}