package com.sean.thomas.trademe.listings

import android.nfc.Tag
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sean.thomas.trademe.BaseListFragment
import com.sean.thomas.trademe.network.ServerRepository
import com.sean.thomas.trademe.network.models.Category
import com.sean.thomas.trademe.network.models.Listing
import kotlinx.android.synthetic.main.fragment_list.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = listingsAdapter

        presenter = ListingsPresenter(this, ServerRepository())

        val categoryArg = arguments?.get(CATEGORY_KEY)
        if (categoryArg != null) {
            presenter.setUp(categoryArg as Category)
        } else {
            presenter.setUp(null)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
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

    override fun getChildTag(): String {
        return TAG
    }
}