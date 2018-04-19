package com.sean.thomas.trademe.network

import android.util.Log
import com.sean.thomas.trademe.network.models.Category
import com.sean.thomas.trademe.network.models.Listing
import io.reactivex.Flowable

class ServerRepository: Repository {

    companion object {
        const val TAG = "ServerRepository"

        const val CONSUMER_KEY = "A1AC63F0332A131A78FAC304D007E7D1"
        const val CONSUMER_SECRET = "EC7F18B17A062962C6930A8AE88B16C7"
        const val CONTENT_TYPE = "application/x-www-form-urlencoded"
    }

    private val serverAPI: ServerAPI = ServerAPI.create()

    override fun getCategoryTree(): Flowable<Category> {
        Log.i(TAG, "~~~requesting category tree")

        return serverAPI.getCategoryTree()
    }

    override fun getListings(categoryNum: String): Flowable<List<Listing>> {
        Log.i(TAG, "~~~requesting listings for $categoryNum")

        return serverAPI.getListings(getToken(), CONTENT_TYPE, categoryNum)
                .flatMap({response -> Flowable.fromIterable(response.listings)
                        .toList()
                        .toFlowable()
                })
    }

    private fun getToken(): String =
            "OAuth oauth_consumer_key=\"$CONSUMER_KEY\", " +
                    "oauth_signature_method=\"PLAINTEXT\", " +
                    "oauth_signature=\"$CONSUMER_SECRET&\""




//    OAuth oauth_consumer_key="A1AC63F0332A131A78FAC304D007E7D1",oauth_signature_method="PLAINTEXT",oauth_timestamp="1524018520",oauth_nonce="l6c2Ia7JexH",oauth_version="1.0",oauth_signature="EC7F18B17A062962C6930A8AE88B16C7%26"
}