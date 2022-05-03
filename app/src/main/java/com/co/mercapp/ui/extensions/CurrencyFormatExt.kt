package com.co.mercapp.ui.extensions

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat

fun Double.formatAsCurrency(
    decimalSeparatorCharacter: Char = ',',
    groupingSeparatorCharacter: Char = '.',
    maximumFractionDigits: Int = 0
): String {
    val numberFormat = NumberFormat.getCurrencyInstance()
    if (numberFormat is DecimalFormat) {
        numberFormat.decimalFormatSymbols = DecimalFormatSymbols().apply {
            decimalSeparator = decimalSeparatorCharacter
            groupingSeparator = groupingSeparatorCharacter
        }
    }
    numberFormat.maximumFractionDigits = maximumFractionDigits
    return numberFormat.format(this)
}