package com.co.mercapp.data.products.datasource.remote

import com.co.mercapp.domain.base.Result
import com.co.mercapp.domain.products.entity.ProductEntity
import com.co.mercapp.domain.products.usecase.SearchProductsByNameUseCase.SearchProductsParams

interface RemoteProductsDataSource {
    suspend fun searchByName(params: SearchProductsParams): Result<List<ProductEntity>>
}