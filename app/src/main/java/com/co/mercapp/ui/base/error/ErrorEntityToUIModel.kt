package com.co.mercapp.ui.base.error

import android.content.Context
import com.co.mercapp.R
import com.co.mercapp.domain.base.ErrorEntity
import com.co.mercapp.domain.base.Mapper
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class ErrorEntityToUIModel @Inject constructor(@ApplicationContext private val context: Context) :
    Mapper<ErrorEntity, ErrorUIModel> {

    override fun map(input: ErrorEntity): ErrorUIModel {
        val generalPurposeMessage = context.getString(R.string.warning_something_went_wrong)
        var criticalMessage: String? = null
        when (input) {
            is ErrorEntity.FailedOperationError -> {
                Timber.e(input.exception, "FailedOperationError")
            }
            is ErrorEntity.RemoteResponseError -> {
                Timber.e("RemoteResponseError - Code [%d] %s", input.code, input.message)
            }
            is ErrorEntity.TimedOutOperationError -> {
                Timber.e(input.exception, "TimedOutOperationError")
                criticalMessage = context.getString(R.string.warning_poor_internet_connection)
            }
            ErrorEntity.NoNetworkConnectionError -> {
                Timber.e("NoNetworkConnectionError")
                criticalMessage = context.getString(R.string.warning_not_connected_internet)
            }
        }
        return ErrorUIModel(
            generalPurposeMessage,
            criticalMessage,
            suggestedAction = context.getString(R.string.title_retry)
        )
    }

}