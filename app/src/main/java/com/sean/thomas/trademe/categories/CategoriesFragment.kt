package com.sean.thomas.trademe.categories

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sean.thomas.trademe.BaseListFragment
import com.sean.thomas.trademe.network.ServerRepository
import com.sean.thomas.trademe.network.models.Category
import kotlinx.android.synthetic.main.fragment_list.*

class CategoriesFragment: BaseListFragment(), CategoriesContract.View {

    companion object {
        fun newInstance(): CategoriesFragment = CategoriesFragment()
    }

    private lateinit var presenter: CategoriesContract.Presenter

    private val categoriesAdapter: CategoriesAdapter = CategoriesAdapter({
        presenter.onCategoryClicked(it)
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = categoriesAdapter

        presenter = CategoriesPresenter(this, ServerRepository())
        presenter.setUp()
    }

    override fun setCategories(categories: List<Category>) {
        categoriesAdapter.setCategories(categories)
    }

    override fun hide() {
        root.visibility = View.GONE
    }

    override fun show() {
        root.visibility = View.VISIBLE
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
}