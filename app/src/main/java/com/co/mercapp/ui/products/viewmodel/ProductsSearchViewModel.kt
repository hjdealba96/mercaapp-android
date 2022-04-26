package com.co.mercapp.ui.products.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.co.mercapp.domain.products.usecase.SearchProductsByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsSearchViewModel @Inject constructor(private val searchProductsByNameUseCase: SearchProductsByNameUseCase) :
    ViewModel() {

        fun test(){
            viewModelScope.launch {
                searchProductsByNameUseCase.perform(
                    SearchProductsByNameUseCase.SearchProductsParams(
                        "wsf",
                        0,
                        0
                    )
                )
            }
        }


}