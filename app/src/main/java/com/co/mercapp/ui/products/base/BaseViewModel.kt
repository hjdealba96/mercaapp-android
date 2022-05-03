package com.co.mercapp.ui.products.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    private val jobs: MutableMap<String, Job?> by lazy { mutableMapOf() }

    protected fun runJobAndCancelPrevious(jobName: String, job: Job) {
        runCatching { jobs.remove(jobName)?.cancel() }
        jobs[jobName] = job
    }

    override fun onCleared() {
        super.onCleared()
        runCatching { jobs.clear() }
    }

    /**
     * Workaround for injecting a simple boolean as an argument using Hilt/Dagger :|
     * **/

    class DelayStatus @Inject constructor() {
        var enabled: Boolean = true
    }

}