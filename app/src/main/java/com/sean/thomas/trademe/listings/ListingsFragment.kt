package com.sean.thomas.trademe.listings

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sean.thomas.trademe.BaseListFragment
import com.sean.thomas.trademe.R
import com.sean.thomas.trademe.network.ServerRepository
import com.sean.thomas.trademe.network.models.Category
import com.sean.thomas.trademe.network.models.Listing
import com.sean.thomas.trademe.schedulers.SchedulersProviderImpl
import kotlinx.android.synthetic.main.empty_listings.*
import kotlinx.android.synthetic.main.fragment_listings.*

/**
 * The view for the listings portion of the screen.
 */
class ListingsFragment: BaseListFragment(), ListingsContract.View {

    companion object {
        val TAG = ListingsFragment::class.java.canonicalName!!
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
    /**
     * Retained so that the empty listings message can be displayed after config changes on the
     * empty screen.
     */
    private var emptyCategoryTitle: String = ""

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

        // if there was a config change on the empty screen, restore it
        if (!emptyCategoryTitle.isEmpty()) showEmptyScreen(emptyCategoryTitle)

        // don't set up the presenter after a restore
        if(!this::presenter.isInitialized) {
            presenter = ListingsPresenter(this, ServerRepository(), SchedulersProviderImpl)

            val category = arguments?.get(CATEGORY_KEY) ?: return presenter.setUp()
            presenter.setUp(category as Category)
        }
    }

    override fun showEmptyScreen(categoryTitle: String) {
        emptyCategoryTitle = categoryTitle

        empty_view.visibility = View.VISIBLE
        empty_message.visibility = if (categoryTitle.isEmpty()) View.GONE else View.VISIBLE
        empty_message.text = activity?.getString(R.string.empty_listings_message, categoryTitle)

    }

    override fun hideEmptyScreen() {
        emptyCategoryTitle = ""

        empty_view.visibility = View.GONE
    }

    override fun showProgress() {
        showEmptyScreen("")
        super.showProgress()
    }

    override fun hideProgress() {
        hideEmptyScreen()
        super.hideProgress()
    }

    fun onNewCategory(category: Category) {
        presenter.onNewCategory(category)
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