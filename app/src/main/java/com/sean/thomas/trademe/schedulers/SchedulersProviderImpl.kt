package com.sean.thomas.trademe.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * The default [SchedulersProvider] implementation to be used at runtime.
 */
object SchedulersProviderImpl : SchedulersProvider {

    override fun background(): Scheduler = Schedulers.io()

    override fun mainThread(): Scheduler = AndroidSchedulers.mainThread()
}