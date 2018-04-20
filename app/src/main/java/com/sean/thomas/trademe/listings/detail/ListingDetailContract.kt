package com.sean.thomas.trademe.listings.detail

import com.sean.thomas.trademe.BasePresenter
import com.sean.thomas.trademe.BaseView
import com.sean.thomas.trademe.network.models.ListingDetail

interface ListingDetailContract {

    interface View: BaseView {
        fun setData(listing: ListingDetail)
    }

    interface Presenter: BasePresenter {
        fun setUp(listingId: String)
    }
}