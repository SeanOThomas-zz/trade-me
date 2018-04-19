package com.sean.thomas.trademe.network

import com.sean.thomas.trademe.network.models.Category
import com.sean.thomas.trademe.network.models.ListingsResponse
import io.reactivex.Flowable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface ServerAPI {

    companion object {

        private const val BASE_URL = "https://api.tmsandbox.co.nz/v1/"
        private const val DEFAULT_NUM_LISTINGS = 20
        private const val DEFAULT_PHOTO_SIZE = "Thumbnail"

        fun create(): ServerAPI {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()

            return retrofit.create(ServerAPI::class.java)
        }
    }

    @GET("Categories.json")
    fun getCategoryTree(@Query("with_counts") withCounts: Boolean = true): Flowable<Category>

    @GET("Search/General.json")
    fun getListings(
            @Header("Authorization") authorization: String,
            @Header("Content-Type") contentType: String,
            @Query("category") categoryNumber: String,
            @Query("rows") numListings: Int = DEFAULT_NUM_LISTINGS,
            @Query("photo_size") photoSize: String = DEFAULT_PHOTO_SIZE): Flowable<ListingsResponse>
}