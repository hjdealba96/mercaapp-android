package com.co.mercapp.ui.base.error

data class ErrorUIModel(
    val generalPurposeMessage: String,
    val criticalMessage: String? = null,
    val suggestedAction: String
) {
    fun getWarningMessage(): String = criticalMessage ?: generalPurposeMessage
}