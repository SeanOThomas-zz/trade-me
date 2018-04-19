package com.sean.thomas.trademe

/**
 * Base view methods for the MVP pattern.
 */
interface BaseView {
    fun showNetworkErrorMessage()
    fun showProgress()
    fun hideProgress()
}