package com.co.mercapp.data.products.datasource.remote

import com.co.mercapp.data.products.datasource.remote.mapper.JsonToProductList
import com.co.mercapp.data.products.networking.networkRequest
import com.co.mercapp.domain.base.Result
import com.co.mercapp.domain.products.entity.ProductEntity
import com.co.mercapp.domain.products.usecase.SearchProductsByNameUseCase.SearchProductsParams
import javax.inject.Inject

class DefaultProductsRemoteDataSource @Inject constructor(private val productsService: ProductsApiService) :
    RemoteProductsDataSource {

    override suspend fun searchByName(params: SearchProductsParams): Result<List<ProductEntity>> =
        networkRequest {
            JsonToProductList.map(
                productsService.searchByName(
                    sideId = DEFAULT_SIDE_ID,
                    query = hashMapOf(
                        "q" to params.name,
                        "offset" to params.offset,
                        "limit" to params.limit
                    )
                )
            )
        }

    companion object {
        private const val DEFAULT_SIDE_ID = "MLA"
    }

}