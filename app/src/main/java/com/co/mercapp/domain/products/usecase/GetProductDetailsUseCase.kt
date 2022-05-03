package com.co.mercapp.domain.products.usecase

import com.co.mercapp.domain.base.Result
import com.co.mercapp.domain.base.UseCase
import com.co.mercapp.domain.products.entity.ProductDetailsEntity
import com.co.mercapp.domain.products.repository.ProductsRepository
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(private val productsRepository: ProductsRepository) :
    UseCase<String, ProductDetailsEntity> {
    override suspend fun perform(params: String): Result<ProductDetailsEntity> =
        productsRepository.getDetails(params)
}