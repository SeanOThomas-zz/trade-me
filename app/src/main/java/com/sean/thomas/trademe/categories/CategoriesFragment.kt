package com.sean.thomas.trademe.categories

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sean.thomas.trademe.BaseFragment
import com.sean.thomas.trademe.R
import com.sean.thomas.trademe.listings.ListingsFragment
import com.sean.thomas.trademe.network.ServerRepository
import com.sean.thomas.trademe.network.models.Category
import com.sean.thomas.trademe.schedulers.SchedulersProviderImpl
import kotlinx.android.synthetic.main.fragment_categories.*

/**
 * The view for the categories portion of the screen.
 */
class CategoriesFragment: BaseFragment(), CategoriesContract.View {

    companion object {
        val TAG = CategoriesFragment::class.java.canonicalName!!

        fun newInstance(): CategoriesFragment = CategoriesFragment()
    }

    private lateinit var presenter: CategoriesContract.Presenter

    private val categoriesAdapter: CategoriesAdapter = CategoriesAdapter({presenter.onCategoryClicked(it) })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = categoriesAdapter

        // only set up presenter if not restored
        if (!this::presenter.isInitialized) {
            presenter = CategoriesPresenter(this, ServerRepository(), SchedulersProviderImpl)
            presenter.setUp()
        }
    }

    override fun setCategories(categories: List<Category>) {
        categoriesAdapter.setCategories(categories)
    }

    override fun onDestroy() {
        presenter.tearDown()
        super.onDestroy()
    }

    override fun finish() {
        activity?.finish()
    }

    fun handleBackPress() {
        presenter.onBackPressed()
    }

    override fun getChildTag(): String = CategoriesFragment.TAG
}