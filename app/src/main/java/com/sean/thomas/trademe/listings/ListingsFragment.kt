package com.sean.thomas.trademe.listings

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sean.thomas.trademe.BaseListFragment
import com.sean.thomas.trademe.R
import com.sean.thomas.trademe.network.ServerRepository
import com.sean.thomas.trademe.network.models.Category
import com.sean.thomas.trademe.network.models.Listing
import kotlinx.android.synthetic.main.empty_listings.*
import kotlinx.android.synthetic.main.fragment_listings.*

class ListingsFragment: BaseListFragment(), ListingsContract.View {

    companion object {
        const val TAG = "ListingsFragment"
        const val CATEGORY_KEY = "category_key"

        fun newInstance(category: Category? = null): ListingsFragment {
            val frag = ListingsFragment()
            val bundle = Bundle()
            if (category != null) {
                bundle.putSerializable(CATEGORY_KEY, category)
            }
            frag.arguments = bundle
            return frag
        }
    }

    private lateinit var presenter: ListingsContract.Presenter

    private val listingsAdapter: ListingsAdapter = ListingsAdapter({
        presenter.onListingClicked(it.listingId)
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_listings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = listingsAdapter

        presenter = ListingsPresenter(this, ServerRepository())

        val category = arguments?.get(CATEGORY_KEY)
        if (category != null) {
            presenter.setUp(category as Category)
        } else {
            presenter.setUp(null)
        }
    }

    override fun showEmptyScreen(categoryTitle: String) {
        Log.i(TAG, "showEmptyScreen")
        empty_view.visibility = View.VISIBLE

        empty_message.text = activity?.getString(R.string.empty_listings_message, categoryTitle)
    }

    override fun hideEmptyScreen() {
        Log.i(TAG, "hideEmptyScreen")
        empty_view.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
    }

    override fun setListings(listings: List<Listing>) {
        listingsAdapter.setListings(listings)
    }

    override fun onDestroy() {
        presenter.tearDown()
        super.onDestroy()
    }

    override fun getChildTag(): String {
        return TAG
    }
}