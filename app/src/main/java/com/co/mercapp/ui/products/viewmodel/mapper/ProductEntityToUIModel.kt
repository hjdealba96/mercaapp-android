package com.co.mercapp.ui.products.viewmodel.mapper

import android.content.Context
import com.co.mercapp.R
import com.co.mercapp.domain.base.Mapper
import com.co.mercapp.domain.products.entity.ProductEntity
import com.co.mercapp.ui.extensions.formatAsCurrency
import com.co.mercapp.ui.products.viewmodel.model.ProductUIModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProductEntityToUIModel @Inject constructor(@ApplicationContext private val context: Context) :
    Mapper<List<ProductEntity>, List<ProductUIModel>> {
    override fun map(input: List<ProductEntity>): List<ProductUIModel> =
        input.map { product ->
            ProductUIModel(
                id = product.id,
                name = product.title,
                price = product.price.formatAsCurrency(),
                picture = product.thumbnail,
                freeShipping = product.freeShipping,
                condition = when (product.condition) {
                    ProductEntity.ProductConditionEntity.NEW -> context.getString(R.string.title_new)
                    ProductEntity.ProductConditionEntity.USED -> context.getString(R.string.title_used)
                    ProductEntity.ProductConditionEntity.UNKNOWN -> ""
                },
                address = product.address.run {
                    when {
                        this.city.isNotBlank() && this.state.isNotBlank() -> "$state , $city"
                        this.state.isNotBlank() -> state
                        this.city.isNotBlank() -> city
                        else -> context.getString(R.string.title_no_address_information)
                    }
                },
                shareableDescription = context.getString(R.string.title_take_a_look_product)
                    .plus("\n").plus(product.title).plus("\n").plus(product.permalink),
                storeLink = product.permalink
            )
        }
}