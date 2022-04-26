package com.co.mercapp.domain.products.repository

import com.co.mercapp.domain.base.Result
import com.co.mercapp.domain.products.entity.ProductEntity
import com.co.mercapp.domain.products.usecase.SearchProductsByNameUseCase.SearchProductsParams

interface ProductsRepository {
    suspend fun searchByName(params: SearchProductsParams): Result<List<ProductEntity>>
}