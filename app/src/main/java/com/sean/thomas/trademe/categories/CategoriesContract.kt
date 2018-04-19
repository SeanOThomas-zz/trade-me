package com.sean.thomas.trademe.categories

import com.sean.thomas.trademe.BasePresenter
import com.sean.thomas.trademe.BaseView
import com.sean.thomas.trademe.network.models.Category

/**
 * Defines the contract between the categories view and presenter.
 */
interface CategoriesContract {

    interface View: BaseView {
        fun setCategories(categories: List<Category>)
        fun finish()
    }

    interface Presenter: BasePresenter {
        fun onCategoryClicked(category: Category)
        fun onBackPressed()
    }
}