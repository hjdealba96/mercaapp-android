package com.co.mercapp.ui.products.viewmodel.mapper

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.co.mercapp.R
import com.co.mercapp.domain.base.Mapper
import com.co.mercapp.domain.products.entity.ProductDetailsEntity
import com.co.mercapp.ui.products.viewmodel.model.ProductDetailsUIModel
import com.co.mercapp.ui.theme.CornflowerBlue
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProductsDetailsEntityToUIModel @Inject constructor(@ApplicationContext private val context: Context) :
    Mapper<ProductDetailsEntity, ProductDetailsUIModel> {
    override fun map(input: ProductDetailsEntity): ProductDetailsUIModel {
        val picturesCount = input.pictures.count()
        return ProductDetailsUIModel(warranty = validateValidUserFaceValue(
            input.warranty,
            R.string.title_warranty_not_specified
        ),
            pictures = input.pictures.mapIndexed { index, picture ->
                ProductDetailsUIModel.ProductPicture(
                    index = "${index.plus(1)}/$picturesCount",
                    url = picture
                )
            }, attributes = input.attributes.map { attribute ->
                ProductDetailsUIModel.ProductAttribute(
                    name = validateValidUserFaceValue(attribute.name),
                    value = validateValidUserFaceValue(attribute.value)
                )
            }, soldQuantity = context.resources.getQuantityString(
                R.plurals.soldQuantity,
                input.soldQuantity,
                input.soldQuantity
            ), availableQuantity = context.resources.getQuantityString(
                R.plurals.availableQuantity,
                input.availableQuantity,
                input.availableQuantity
            ),
            availableQuantityColor = if (input.availableQuantity > 0) {
                CornflowerBlue
            } else {
                Color.Red
            }
        )
    }

    private fun validateValidUserFaceValue(
        value: String,
        @StringRes defaultValue: Int = R.string.title_not_specified
    ): String =
        value.run { this.ifBlank { context.getString(defaultValue) } }

}