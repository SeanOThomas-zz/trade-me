package com.sean.thomas.trademe.network

import com.sean.thomas.trademe.network.models.Category
import com.sean.thomas.trademe.network.models.Listing
import io.reactivex.Flowable

class ServerRepository: Repository {

    companion object {
        const val CONSUMER_KEY = "A1AC63F0332A131A78FAC304D007E7D1"
        const val CONSUMER_SECRET = "EC7F18B17A062962C6930A8AE88B16C7"
        const val CONTENT_TYPE = "application/x-www-form-urlencoded"
    }

    private val serverAPI: ServerAPI = ServerAPI.create()

    override fun getCategoryTree(): Flowable<Category> {
        return serverAPI.getCategoryTree()
    }

    override fun getListings(categoryNum: String): Flowable<List<Listing>> {
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
}