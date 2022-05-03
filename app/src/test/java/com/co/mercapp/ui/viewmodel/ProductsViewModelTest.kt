@file:OptIn(ExperimentalCoroutinesApi::class)

package com.co.mercapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.co.mercapp.domain.base.PaginatedDataEntity
import com.co.mercapp.domain.base.Result
import com.co.mercapp.domain.errorEntity
import com.co.mercapp.domain.productDetailsEntity
import com.co.mercapp.domain.products.repository.ProductsRepository
import com.co.mercapp.domain.products.usecase.GetProductDetailsUseCase
import com.co.mercapp.domain.products.usecase.SearchProductsByNameUseCase
import com.co.mercapp.ui.base.error.ErrorEntityToUIModel
import com.co.mercapp.ui.products.base.BaseViewModel
import com.co.mercapp.ui.products.viewmodel.ProductsViewModel
import com.co.mercapp.ui.products.viewmodel.mapper.ProductEntityToUIModel
import com.co.mercapp.ui.products.viewmodel.mapper.ProductsDetailsEntityToUIModel
import com.co.mercapp.ui.products.viewmodel.state.ProductDetailsState
import com.co.mercapp.ui.products.viewmodel.state.ProductsSearchState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var mockedProductsRepository: ProductsRepository

    @MockK
    lateinit var productDetailsMapper: ProductsDetailsEntityToUIModel

    @MockK
    lateinit var mockedProductMapper: ProductEntityToUIModel

    @MockK
    lateinit var mockedErrorMapper: ErrorEntityToUIModel

    @MockK
    lateinit var mockSearchStateObserver: Observer<ProductsSearchState>

    @MockK
    lateinit var mockProductDetailsObserver: Observer<ProductDetailsState>

    private lateinit var viewModel: ProductsViewModel

    @Before
    fun setupDependencies() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = ProductsViewModel(
            SearchProductsByNameUseCase(mockedProductsRepository),
            GetProductDetailsUseCase(mockedProductsRepository),
            productDetailsMapper,
            mockedProductMapper,
            mockedErrorMapper,
            delayStatus = BaseViewModel.DelayStatus().apply { enabled = false }
        )
        every { mockedErrorMapper.map(errorEntity) } returns errorUIModel
    }

    @Test
    fun emptyInitialSearchText() {

        viewModel.initialSearch(text = "")

        Assert.assertTrue(viewModel.searchState.value == ProductsSearchState.EmptySearchedText)

    }


    @Test
    fun emptyInitialSearch() = runTest(context = UnconfinedTestDispatcher()) {

        val searchedText = "PS5 console"

        coEvery {
            mockedProductsRepository.searchByName(
                SearchProductsByNameUseCase.SearchProductsParams(
                    name = searchedText,
                    offset = 0,
                    limit = 20
                )
            )
        } returns Result.Success(PaginatedDataEntity(total = 0, pages = 0, items = emptyList()))

        val state = viewModel.searchState
        state.observeForever(mockSearchStateObserver)

        viewModel.initialSearch(text = searchedText)

        coVerifySequence {
            mockSearchStateObserver.onChanged(ProductsSearchState.Loading)
            mockSearchStateObserver.onChanged(ProductsSearchState.NoResults)
        }

    }

    @Test
    fun failedInitialSearch() = runTest {

        val searchedText = "PS5 console"

        coEvery {
            mockedProductsRepository.searchByName(
                SearchProductsByNameUseCase.SearchProductsParams(
                    name = searchedText,
                    offset = 0,
                    limit = 20
                )
            )
        } returns Result.Failure(errorEntity)

        viewModel.searchState.observeForever(mockSearchStateObserver)

        viewModel.initialSearch(text = searchedText)

        coVerifySequence {
            mockSearchStateObserver.onChanged(ProductsSearchState.Loading)
            mockSearchStateObserver.onChanged(ProductsSearchState.Failure(errorUIModel))
        }

    }

    @Test
    fun successfulDetailsFetch() {
        val productId = "ID20"
        coEvery {
            mockedProductsRepository.getDetails(productId)
        } returns Result.Success(productDetailsEntity)

        every { productDetailsMapper.map(productDetailsEntity) } returns productDetailsModel

        viewModel.detailsState.observeForever(mockProductDetailsObserver)

        viewModel.getProductDetails(productModel)

        coVerifySequence {
            mockProductDetailsObserver.onChanged(ProductDetailsState.Loading(productModel))
            mockProductDetailsObserver.onChanged(ProductDetailsState.DetailsReady(productModel, productDetailsModel)
            )
        }
    }

    @Test
    fun unsuccessfulDetailsFetch() {
        val productId = "ID20"
        coEvery {
            mockedProductsRepository.getDetails(productId)
        } returns Result.Failure(errorEntity)

        viewModel.detailsState.observeForever(mockProductDetailsObserver)

        viewModel.getProductDetails(productModel)

        coVerifySequence {
            mockProductDetailsObserver.onChanged(ProductDetailsState.Loading(productModel))
            mockProductDetailsObserver.onChanged(ProductDetailsState.Failure(productModel, errorUIModel)
            )
        }
    }

}