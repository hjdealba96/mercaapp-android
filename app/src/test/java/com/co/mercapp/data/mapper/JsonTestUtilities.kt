package com.co.mercapp.data.mapper

import com.co.mercapp.domain.base.ErrorEntity

fun checkFailureResponseCode(errorEntity: ErrorEntity, originCode: Int): Boolean {
    return if (errorEntity is ErrorEntity.RemoteResponseError) {
        errorEntity.code == originCode
    } else {
        false
    }
}