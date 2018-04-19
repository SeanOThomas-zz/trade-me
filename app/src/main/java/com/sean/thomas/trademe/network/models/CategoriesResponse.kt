package com.sean.thomas.trademe.network.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Category(
        @SerializedName("Name")
        val name: String,
        @SerializedName("Number")
        val categoryId: String,
        @SerializedName("Count")
        val count: Int,
        @SerializedName("Subcategories")
        val subCategories: List<Category>?
): Serializable