package com.sean.thomas.trademe.categories

import android.util.Log
import com.sean.thomas.trademe.Bus
import com.sean.thomas.trademe.network.Repository
import com.sean.thomas.trademe.network.models.Category
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.apache.commons.lang3.StringUtils

class CategoriesPresenter(
        private val view: CategoriesContract.View,
        private val repository: Repository
): CategoriesContract.Presenter {

    companion object {
        const val TAG = "CategoriesPresenter"
    }

    private val disposables: CompositeDisposable = CompositeDisposable()
    private lateinit var categoryTree: Category
    private var currentCategoryId: String = ""

    override fun setUp() {
        view.showProgress()
        // get the category tree then update the view
        disposables.add(
                repository.getCategoryTree()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ root ->
                            view.hideProgress()

                            categoryTree = root
                            view.setCategories(categoryTree.subCategories?: ArrayList())
                        }, {
                            error ->
                            view.hideProgress()

                            Log.e(CategoriesPresenter.TAG, "Failed to get categories", error)
                            view.showNetworkErrorMessage()
                        })
        )
    }

    override fun tearDown() {
        disposables.clear()
    }

    override fun onCategoryClicked(category: Category) {
        Bus.publish(category)

        if (category.subCategories != null) {
            currentCategoryId = category.categoryId
            view.setCategories(category.subCategories)
        }
    }

    override fun onBackPressed() {
        if (currentCategoryId.isEmpty()) {
            return view.finish()
        }
        // display the last set of categories (i.e., the root category's subcategories)
        val rootCategory = getRootCategory(currentCategoryId)
        currentCategoryId = rootCategory.categoryId
        view.setCategories(rootCategory.subCategories?: ArrayList())

        Bus.publish(rootCategory)
    }

    /**
     * If the current category id is "1234-5678-", gets the root category with id "1234-" from
     * [categoryTree].
     */
    private fun getRootCategory(categoryId: String) : Category {
        val depth = categoryId.count({ it == '-'})

        var tempRoot = categoryTree

        for(i in 1..(depth - 1)) {
            // note, the i'th '-' is the last char of tempId
            val lastIndex = StringUtils.ordinalIndexOf(categoryId, "-", i)

            // the category id for the current depth
            val tempId = categoryId.subSequence(0, lastIndex + 1)

            // filter subcategories for tempId. note, every root should have subcategories, hence
            // the bang operator.
            tempRoot = tempRoot.subCategories!!.filter({
                it.categoryId == tempId
            })[0]
        }

        return tempRoot
    }
}