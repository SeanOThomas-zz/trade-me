package com.sean.thomas.trademe.network.models

import com.google.gson.annotations.SerializedName

data class ListingDetail(
        @SerializedName("ListingId")
        val listingId: String,
        @SerializedName("Title")
        val title: String,
        @SerializedName("Category")
        val category: String,
        @SerializedName("StartPrice")
        val startPrice: Double,
        @SerializedName("BuyNowPrice")
        val buyNowPrice: Double,
        @SerializedName("ViewCount")
        val viewCount: Int,
        @SerializedName("Photos")
        val imageList: List<Photo>
)

data class Photo(
        @SerializedName("Value")
        val value: Value
)

data class Value(
        @SerializedName("FullSize")
        val fullSize: String
)