package com.sean.thomas.trademe.listings

import android.util.Log
import com.sean.thomas.trademe.Bus
import com.sean.thomas.trademe.network.Repository
import com.sean.thomas.trademe.network.models.Category
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ListingsPresenter(
        private val view: ListingsContract.View,
        private val repository: Repository
): ListingsContract.Presenter {

    companion object {
        const val TAG = "ListingsPresenter"
        const val DEFAULT_ROOT_CAT_ID = ""
    }

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun setUp(category: Category) {
        getListings(category.categoryId, category.name)
    }

    override fun setUp() {
        getListings(DEFAULT_ROOT_CAT_ID, "")
    }

    override fun onNewCategory(category: Category) {
        getListings(category.categoryId, category.name)
    }

    /**
     * Requests listings for the the provided [Category]. If there's no listings, have the view
     * show an empty screen.
     */
    private fun getListings(categoryId: String, categoryName: String) {
        view.showProgress()
        disposables.add(
                repository.getListings(categoryId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ listings ->
                            view.hideProgress()

                            if(listings.isEmpty()) {
                                view.showEmptyScreen(categoryName)
                            } else {
                                view.hideEmptyScreen()
                                view.setListings(listings)
                            }
                        }, {
                            error ->
                            view.hideProgress()

                            Log.e(ListingsPresenter.TAG, "Failed to get listings", error)
                            view.showNetworkErrorMessage()
                        })
        )
    }

    override fun onListingClicked(listingId: String) {
        //TODO: implement
    }

    override fun tearDown() {
        disposables.clear()
    }
}