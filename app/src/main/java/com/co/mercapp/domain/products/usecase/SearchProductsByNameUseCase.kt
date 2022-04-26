package com.co.mercapp.domain.products.usecase

import com.co.mercapp.domain.base.Result
import com.co.mercapp.domain.base.UseCase
import com.co.mercapp.domain.products.entity.ProductEntity
import com.co.mercapp.domain.products.repository.ProductsRepository
import javax.inject.Inject

class SearchProductsByNameUseCase @Inject constructor(private val productsRepository: ProductsRepository) :
    UseCase<SearchProductsByNameUseCase.SearchProductsParams, List<ProductEntity>> {

    override suspend fun perform(params: SearchProductsParams): Result<List<ProductEntity>> = productsRepository.searchByName(params)

    data class SearchProductsParams(val name: String, val offset: Int, val limit: Int)

}