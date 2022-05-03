package com.co.mercapp.ui.mapper

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.co.mercapp.R
import com.co.mercapp.domain.base.ErrorEntity
import com.co.mercapp.ui.base.error.ErrorEntityToUIModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.NullPointerException

@RunWith(AndroidJUnit4::class)
class ErrorEntityToUIModelTest {

    private lateinit var context: Context
    private lateinit var mapper: ErrorEntityToUIModel

    @Before
    fun startupDependencies() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        mapper = ErrorEntityToUIModel(context)
    }

    @Test
    fun mapFailedRemoteResponseError() {
        val expectedGeneralMessage = context.getString(R.string.warning_something_went_wrong)

        val error = mapper.map(ErrorEntity.RemoteResponseError(code = 200))

        Assert.assertTrue(error.generalPurposeMessage == expectedGeneralMessage)
    }

    @Test
    fun mapFailedOperationError() {
        val expectedGeneralMessage = context.getString(R.string.warning_something_went_wrong)

        val error = mapper.map(ErrorEntity.FailedOperationError(NullPointerException()))

        Assert.assertTrue(error.generalPurposeMessage == expectedGeneralMessage)
    }

    @Test
    fun mapFailedTimedOutOperationError() {

        val expectedGeneralMessage = context.getString(R.string.warning_something_went_wrong)
        val expectedCriticalMessage = context.getString(R.string.warning_poor_internet_connection)

        val error = mapper.map(ErrorEntity.TimedOutOperationError(NullPointerException()))

        Assert.assertTrue(
            error.generalPurposeMessage == expectedGeneralMessage
                    && error.criticalMessage == expectedCriticalMessage
        )
    }

    @Test
    fun mapNoNetworkConnectionError() {

        val expectedGeneralMessage = context.getString(R.string.warning_something_went_wrong)
        val expectedCriticalMessage = context.getString(R.string.warning_not_connected_internet)

        val error = mapper.map(ErrorEntity.NoNetworkConnectionError)

        Assert.assertTrue(
            error.generalPurposeMessage == expectedGeneralMessage
                    && error.criticalMessage == expectedCriticalMessage
        )
    }

}