package com.sean.thomas.trademe.network.models

import com.google.gson.annotations.SerializedName


data class ListingsResponse(
        @SerializedName("List")
        val listings: List<Listing>
)

data class Listing(
        @SerializedName("ListingId")
        val listingId: String,
        @SerializedName("Title")
        val title: String,
        @SerializedName("PictureHref")
        val thumbUrl: String
)