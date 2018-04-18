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
    private var currentCategoryNum: String = ""

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
                            view.setCategories(categoryTree.subCategories)
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

        if(category.isLeaf || category.subCategories.isEmpty()) {
            //TODO: remove?
            return view.hide()
        }

        currentCategoryNum = category.number
        view.setCategories(category.subCategories)
    }

    override fun onBackPressed() {
        if (currentCategoryNum.isEmpty()) {
            return view.finish()
        }
        // after a back press, categories should always show
        view.show() //TODO: remove?

        // display the last set of categories (i.e., the root category's subcategories)
        val rootCategory = getRootCategory(currentCategoryNum)
        currentCategoryNum = rootCategory.number
        view.setCategories(rootCategory.subCategories)

        Bus.publish(rootCategory)
    }

    private fun getRootCategory(currentNum: String) : Category {
        val rootCategoryNum = getRootNum(currentNum)

        var currentRoot = categoryTree

        val depth = rootCategoryNum.count({ it == '-'})
        for(i in 1..depth) {
            // get index of last hyphen for current depth
            val last = StringUtils.ordinalIndexOf(rootCategoryNum, "-", i)

            // get category num for current depth
            val currCategoryNum = rootCategoryNum.subSequence(0, last + 1)

            // filter the category with same num
            currentRoot = currentRoot.subCategories.filter({
                it.number == currCategoryNum
            })[0]
        }

        return currentRoot
    }

    /**
     * Gets the root (i.e., last) category number. For example, if [currentNum] is "0351-2439-", the
     * result would be "0351-".
     *
     * @param currentNum the current category number
     * @return the root category number
     */
    private fun getRootNum(currentNum: String): String  {
        val depth = currentNum.count({ it == '-'})

        val indexOfSecondToLastHyphen = StringUtils.ordinalIndexOf(currentNum, "-", depth - 1)

        return currentNum.substring(0, indexOfSecondToLastHyphen + 1)
    }
}