package com.co.mercapp.ui.products.viewmodel

import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.co.mercapp.domain.base.Result
import com.co.mercapp.domain.products.usecase.GetProductDetailsUseCase
import com.co.mercapp.domain.products.usecase.SearchProductsByNameUseCase
import com.co.mercapp.ui.base.error.ErrorEntityToUIModel
import com.co.mercapp.ui.pagination.PagingUISource
import com.co.mercapp.ui.products.base.BaseViewModel
import com.co.mercapp.ui.products.viewmodel.mapper.ProductEntityToUIModel
import com.co.mercapp.ui.products.viewmodel.mapper.ProductsDetailsEntityToUIModel
import com.co.mercapp.ui.products.viewmodel.model.ProductUIModel
import com.co.mercapp.ui.products.viewmodel.state.ProductDetailsState
import com.co.mercapp.ui.products.viewmodel.state.ProductsSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * All the logic for searching and getting information about products are placed here. Results for a search
 * by a phrase are paginated, see [PagingUISource]. All the data streams are presented as [LiveData] in order
 * to provide support for unit testing since [State] does not provide a proper way to observe updates out of a
 * composable scope.
 *
 * @param delayStatus Enabled/disabled delays for sending Livedata updates. Delays are applied
 * here in order to provide a better UX experience to the user, some computations are completed really
 * this may prevent the UI to render a proper animation or loading state. Since TestScope.kt skips delay
 * functions, this flag is set to true when instantiating [ProductsViewModel] from a unit test, this seems
 * like a decent workaround for now. (It's hard to inject a boolean using Hilt :sad)
 * See: https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/kotlinx.coroutines.test/-test-scope/index.html#-33808464%2FExtensions%2F-1989735873
 *
 * @author Henry De Alba
 *
 * */

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val searchProductsByNameUseCase: SearchProductsByNameUseCase,
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val productDetailsMapper: ProductsDetailsEntityToUIModel,
    private val productModelMapper: ProductEntityToUIModel,
    private val errorMapper: ErrorEntityToUIModel,
    private val delayStatus: DelayStatus = DelayStatus()
) : BaseViewModel() {

    private var _searchState = MutableLiveData<ProductsSearchState>()
    val searchState: LiveData<ProductsSearchState> = _searchState

    private var _detailsState = MutableLiveData<ProductDetailsState>()
    val detailsState: LiveData<ProductDetailsState> = _detailsState

    private val pagingSource by lazy { PagingUISource<ProductUIModel>(limit = 20) }

    fun initialSearch(text: String) {
        if (text.isNotBlank()) {
            runJobAndCancelPrevious(jobName = "initialSearch", job =
            viewModelScope.launch(Dispatchers.IO) {
                pagingSource.reset()
                _searchState.postValue(ProductsSearchState.Loading)
                when (val result = searchProductsByNameUseCase.perform(
                    SearchProductsByNameUseCase.SearchProductsParams(
                        name = text,
                        offset = pagingSource.offset(),
                        limit = pagingSource.limit()
                    )
                )) {
                    is Result.Success -> {
                        if (result.data.isNotEmpty()) {
                            pagingSource.init(pages = result.data.pages, total = result.data.total)
                            pagingSource.append(productModelMapper.map(result.data.items))
                            runDelayed {
                                _searchState.postValue(
                                    ProductsSearchState.Results(pagingSource.data())
                                )
                            }
                        } else {
                            runDelayed {
                                _searchState.postValue(ProductsSearchState.NoResults)
                            }
                        }
                    }
                    is Result.Failure -> runDelayed {
                        _searchState.postValue(
                            ProductsSearchState.Failure(error = errorMapper.map(result.error))
                        )
                    }
                }
            })
        } else {
            _searchState.value = ProductsSearchState.EmptySearchedText
        }
    }

    fun fetchMoreResults(text: String) {
        runJobAndCancelPrevious(jobName = "fetchMoreResults",
            job = viewModelScope.launch(Dispatchers.IO) {
                if (delayStatus.enabled) {
                    delay(PAGINATION_SEARCH_DELAY)
                }
                if (pagingSource.canFetch()) {
                    _searchState.postValue(
                        ProductsSearchState.Results(pagingSource.data(), isRefreshing = true)
                    )
                    when (val result = searchProductsByNameUseCase.perform(
                        SearchProductsByNameUseCase.SearchProductsParams(
                            name = text,
                            offset = pagingSource.offset(),
                            limit = pagingSource.limit()
                        )
                    )) {
                        is Result.Success -> {
                            if (result.data.isNotEmpty()) {
                                pagingSource.append(productModelMapper.map(result.data.items))
                            }
                            runDelayed {
                                _searchState.postValue(ProductsSearchState.Results(pagingSource.data()))
                            }
                        }
                        is Result.Failure -> runDelayed {
                            _searchState.postValue(
                                ProductsSearchState.Results(
                                    pagingSource.data(),
                                    refreshingError = errorMapper.map(result.error)
                                )
                            )
                        }
                    }
                }
            })
    }

    fun getProductDetails(product: ProductUIModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _detailsState.postValue(ProductDetailsState.Loading(product))
            when (val result = getProductDetailsUseCase.perform(product.id)) {
                is Result.Success -> runDelayed {
                    _detailsState.postValue(
                        ProductDetailsState.DetailsReady(
                            product = product,
                            details = productDetailsMapper.map(result.data)
                        )
                    )
                }
                is Result.Failure -> runDelayed {
                    _detailsState.postValue(
                        ProductDetailsState.Failure(
                            product = product,
                            error = errorMapper.map(result.error)
                        )
                    )
                }
            }
        }
    }

    private suspend fun runDelayed(
        delay: Long = DEFAULT_UPDATE_DELAY,
        function: suspend () -> Unit
    ) {
        if (delayStatus.enabled) {
            delay(delay)
        }
        function()
    }

    companion object {
        private const val DEFAULT_UPDATE_DELAY = 800L
        private const val PAGINATION_SEARCH_DELAY = 300L
    }

}