package com.sean.thomas.trademe.categories

import com.sean.thomas.trademe.BasePresenter
import com.sean.thomas.trademe.BaseView
import com.sean.thomas.trademe.network.models.Category

interface CategoriesContract {

    interface View: BaseView {
        fun setCategories(categories: List<Category>)
        fun hide()
        fun show()
        fun finish()
    }

    interface Presenter: BasePresenter {
        fun onCategoryClicked(category: Category)
        fun onBackPressed()
    }
}