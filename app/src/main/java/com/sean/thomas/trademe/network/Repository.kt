package com.sean.thomas.trademe.network

import com.sean.thomas.trademe.network.models.Category
import com.sean.thomas.trademe.network.models.Listing
import com.sean.thomas.trademe.network.models.ListingDetail
import io.reactivex.Flowable

/**
 * Required methods for a repository.
 */
interface Repository {
    fun getCategoryTree(): Flowable<Category>
    fun getListings(categoryNum: String): Flowable<List<Listing>>
    fun getListingDetail(listingId: String): Flowable<ListingDetail>
}