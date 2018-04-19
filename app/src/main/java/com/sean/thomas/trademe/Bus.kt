package com.sean.thomas.trademe

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

/**
 * A singleton RxJava bus for publishing generic events.
 */
object Bus {

    private val subject = PublishSubject.create<Any>()

    fun publish(event: Any) {
        subject.onNext(event)
    }

    fun <T> observe(type: Class<T>): Flowable<T> = subject.ofType(type)
            .toFlowable(BackpressureStrategy.LATEST)
}