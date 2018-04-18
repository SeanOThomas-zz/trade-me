package com.sean.thomas.trademe.network.models

import com.google.gson.annotations.SerializedName

data class Category(
        @SerializedName("Name")
        val name: String,
        @SerializedName("Number")
        val number: String,
        @SerializedName("Count")
        val count: Int,
        @SerializedName("IsLeaf")
        val isLeaf: Boolean,
        @SerializedName("Subcategories")
        val subCategories: List<Category>
)