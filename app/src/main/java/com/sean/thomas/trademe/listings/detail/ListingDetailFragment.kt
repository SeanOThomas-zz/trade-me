package com.sean.thomas.trademe.listings.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sean.thomas.trademe.BaseFragment
import com.sean.thomas.trademe.R
import com.sean.thomas.trademe.network.ServerRepository
import com.sean.thomas.trademe.network.models.ListingDetail
import com.sean.thomas.trademe.schedulers.SchedulersProviderImpl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_listing_detail.*

/**
 * The view for the listings details portion of the screen.
 */
class ListingDetailFragment : BaseFragment(), ListingDetailContract.View {

    companion object {
        val TAG = ListingDetailFragment::class.java.canonicalName!!
        const val LISTING_ID_KEY = "listing_id_key"

        fun newInstance(listingId: String): ListingDetailFragment {
            val frag = ListingDetailFragment()
            val bundle = Bundle()
            bundle.putString(LISTING_ID_KEY, listingId)
            frag.arguments = bundle
            return frag
        }
    }

    lateinit var presenter: ListingDetailContract.Presenter
    lateinit var listing: ListingDetail

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_listing_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // only set up presenter if not restored
        if (!this::presenter.isInitialized) {
            presenter = ListingDetailPresenter(this, ServerRepository(), SchedulersProviderImpl)
            // args should never be null, hence the bang operator
            presenter.setUp(arguments!!.getString(LISTING_ID_KEY))
        } else if (this::listing.isInitialized) {
            // restore view data
            setData(listing)
        }
    }

    override fun setData(listing: ListingDetail) {
        this.listing = listing

        title.text = listing.title
        start_price.text = activity!!.getString(R.string.start_price, listing.startPrice)
        buy_now_Price.text = activity!!.getString(R.string.buy_now_price, listing.buyNowPrice)
        view_count.text = activity!!.getString(R.string.view_count, listing.viewCount)

        Picasso.with(activity)
                .load(listing.imageList[0].value.fullSize)
                .into(image);
    }

    override fun onDestroy() {
        presenter.tearDown()
        super.onDestroy()
    }

    override fun getChildTag(): String = TAG
}