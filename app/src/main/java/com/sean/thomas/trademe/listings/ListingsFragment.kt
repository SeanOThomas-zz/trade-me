package com.sean.thomas.trademe.listings

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sean.thomas.trademe.BaseListFragment
import com.sean.thomas.trademe.network.ServerRepository
import com.sean.thomas.trademe.network.models.Listing
import kotlinx.android.synthetic.main.fragment_list.*

class ListingsFragment: BaseListFragment(), ListingsContract.View {

    companion object {
        fun newInstance(): ListingsFragment = ListingsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    private lateinit var presenter: ListingsContract.Presenter

    private val listingsAdapter: ListingsAdapter = ListingsAdapter({
        presenter.onListingClicked(it.listingId)
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = listingsAdapter

        presenter = ListingsPresenter(this, ServerRepository())
        presenter.setUp()
    }

    override fun hide() {
        root.visibility = View.GONE
    }

    override fun show() {
        root.visibility = View.VISIBLE
    }

    override fun setListings(listings: List<Listing>) {
        listingsAdapter.setListings(listings)
    }

    override fun onDestroy() {
        presenter.tearDown()
        super.onDestroy()
    }
}