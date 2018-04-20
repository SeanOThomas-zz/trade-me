package com.sean.thomas.trademe.listings.detail

import android.util.Log
import com.sean.thomas.trademe.network.Repository
import com.sean.thomas.trademe.schedulers.SchedulersProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * Presenter for the listing detail screen.
 */
class ListingDetailPresenter(
        private val view: ListingDetailContract.View,
        private val repository: Repository,
        private val schedulersProvider: SchedulersProvider
): ListingDetailContract.Presenter {

    companion object {
        val TAG = ListingDetailContract::class.java.canonicalName!!
    }

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun setUp(listingId: String) {
        view.showProgress()
        disposables.add(
                repository.getListingDetail(listingId)
                        .subscribeOn(schedulersProvider.background())
                        .observeOn(schedulersProvider.mainThread())
                        .subscribe({ listing ->
                            view.hideProgress()

                            view.setData(listing)
                        }, {
                            error ->
                            view.hideProgress()

                            Log.e(TAG, "Failed to get listing detail", error)
                            view.showNetworkErrorMessage()
                        })
        )
    }

    override fun setUp() {}

    override fun tearDown() {
        disposables.clear()
    }
}