package com.sean.thomas.trademe.util

import com.sean.thomas.trademe.schedulers.SchedulersProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 *  A synchronous [SchedulersProvider] implementation for testing.
 */
object TestSchedulersProvider: SchedulersProvider {

    override fun background(): Scheduler = Schedulers.trampoline()

    override fun mainThread(): Scheduler = Schedulers.trampoline()
}