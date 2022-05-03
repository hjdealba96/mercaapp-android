package com.co.mercapp.ui.pagination

import org.junit.Assert
import org.junit.Test

class PagingUISourceTest {

    @Test
    fun canFetchWithOutInit() {
        val pagingSource = PagingUISource<String>(limit = 20)
        Assert.assertFalse(pagingSource.canFetch())
    }

    @Test
    fun dataLongerThanTotal() {
        val pagingSource = PagingUISource<Unit>(limit = 5).apply {
            init(pages = 2, total = 10)
        }
        Assert.assertFalse(pagingSource.append(List(15) {}))
    }

    @Test
    fun dataEqualThanTotal() {
        val pagingSource = PagingUISource<Unit>(limit = 5).apply {
            init(pages = 2, total = 10)
        }
        Assert.assertTrue(pagingSource.append(List(10) {}))
    }

    @Test
    fun dataLowerThanTotal() {
        val pagingSource = PagingUISource<Unit>(limit = 5).apply {
            init(pages = 2, total = 10)
        }
        Assert.assertTrue(pagingSource.append(List(5) {}))
    }

    @Test
    fun canFetchDataFilled() {
        val pagingSource = PagingUISource<Unit>(limit = 5).apply {
            init(pages = 2, total = 10)
            append(List(10) {})
        }
        Assert.assertFalse(pagingSource.canFetch())
    }

    @Test
    fun canFetch() {
        val pagingSource = PagingUISource<Unit>(limit = 5).apply {
            init(pages = 2, total = 10)
            append(List(5) {})
        }
        Assert.assertTrue(pagingSource.canFetch())
    }

}