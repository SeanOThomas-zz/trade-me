package com.sean.thomas.trademe.network

import com.sean.thomas.trademe.network.models.Category
import com.sean.thomas.trademe.network.models.Listing
import io.reactivex.Flowable

interface Repository {
    fun getCategoryTree(): Flowable<Category>
    fun getListings(categoryNum: String): Flowable<List<Listing>>
}