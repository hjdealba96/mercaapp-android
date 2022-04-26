package com.co.mercapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MercaAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        configureTimber()
    }

    private fun configureTimber() = Timber.plant(Timber.DebugTree())

}