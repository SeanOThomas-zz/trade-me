package com.sean.thomas.trademe.schedulers

import io.reactivex.Scheduler

/**
 * Provides RxJava schedulers.
 */
interface SchedulersProvider {
    fun background(): Scheduler
    fun mainThread(): Scheduler
}