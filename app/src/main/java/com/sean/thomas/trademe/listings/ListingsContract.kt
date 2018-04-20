package com.sean.thomas.trademe.listings

import com.sean.thomas.trademe.BasePresenter
import com.sean.thomas.trademe.BaseView
import com.sean.thomas.trademe.network.models.Category
import com.sean.thomas.trademe.network.models.Listing

/**
 * Defines the contract between the listings view and presenter.
 */
interface ListingsContract {

    interface View : BaseView {
        fun setListings(listings: List<Listing>)
        fun showEmptyScreen(categoryTitle: String)
        fun hideEmptyScreen()
    }

    interface Presenter: BasePresenter {
        fun setUp(category: Category)
        fun onListingClicked(listing: Listing)
        fun onNewCategory(category: Category)
    }
}