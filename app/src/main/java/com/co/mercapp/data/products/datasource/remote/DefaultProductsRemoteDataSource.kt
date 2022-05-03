package com.co.mercapp.data.products.datasource.remote

import com.co.mercapp.data.products.datasource.remote.mapper.JsonToPaginatedProductsData
import com.co.mercapp.data.products.datasource.remote.mapper.JsonToProductDetails
import com.co.mercapp.data.products.networking.NetworkConnectionChecker
import com.co.mercapp.data.products.networking.networkRequest
import com.co.mercapp.domain.base.PaginatedDataEntity
import com.co.mercapp.domain.base.Result
import com.co.mercapp.domain.products.entity.ProductDetailsEntity
import com.co.mercapp.domain.products.entity.ProductEntity
import com.co.mercapp.domain.products.usecase.SearchProductsByNameUseCase.SearchProductsParams
import javax.inject.Inject

class DefaultProductsRemoteDataSource @Inject constructor(
    private val productsService: ProductsApiService,
    private val networkConnectionChecker: NetworkConnectionChecker
) : RemoteProductsDataSource {

    override suspend fun searchByName(params: SearchProductsParams): Result<PaginatedDataEntity<ProductEntity>> =
        networkRequest(networkConnectionChecker) {
            JsonToPaginatedProductsData.map(
                productsService.searchByName(
                    sideId = DEFAULT_SIDE_ID,
                    query = hashMapOf(
                        QUERY_PARAM_KEY to params.name,
                        OFFSET_PARAM_KEY to params.offset,
                        LIMIT_PARAM_KEY to params.limit
                    )
                )
            )
        }

    override suspend fun getDetails(id: String): Result<ProductDetailsEntity> =
        networkRequest(networkConnectionChecker) {
            JsonToProductDetails.map(productsService.getDetails(id))
        }

    companion object {
        private const val QUERY_PARAM_KEY = "q"
        private const val OFFSET_PARAM_KEY = "offset"
        private const val LIMIT_PARAM_KEY = "limit"
        private const val DEFAULT_SIDE_ID = "MLA"
    }

}