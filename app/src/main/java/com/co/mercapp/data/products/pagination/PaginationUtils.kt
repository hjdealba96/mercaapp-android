package com.co.mercapp.data.products.pagination

import kotlin.math.ceil

object PaginationUtils {

    /**
     * Calculates the number of pages of a dataset based on the [total] and current [limit]
     *
     * @param total Count of results
     * @param limit Limit per page
     *
     * @return Number of pages
     *
     * */

    fun calculatePages(total: Int, limit: Int): Int =
        if (limit > 0) {
            ceil(total.toDouble() / limit.toDouble()).toInt()
        } else {
            0
        }

}