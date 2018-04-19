package com.sean.thomas.trademe


import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.sean.thomas.trademe.categories.CategoriesFragment
import com.sean.thomas.trademe.listings.ListingsFragment
import com.sean.thomas.trademe.network.models.Category
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Contains one fragment container for non-tablets, and two fragments for tablets. Responsibilities
 * include: replacing fragments (including any [isTablet]) checking, delegating back presses and
 * category events.
 */
class MainActivity: AppCompatActivity() {

    companion object {
        const val CATEGORIES_FRAGMENT_TAG = "categories_fragment_tag"
        const val LISTINGS_FRAGMENT_TAG = "listings_fragment_tag"
    }

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isTablet()) {
            if(supportFragmentManager.findFragmentByTag(CATEGORIES_FRAGMENT_TAG) == null) {
                replaceFragment(CategoriesFragment.newInstance(), categories_container.id, CATEGORIES_FRAGMENT_TAG)
            }

            if(isTablet() && supportFragmentManager.findFragmentByTag(LISTINGS_FRAGMENT_TAG) == null) {
                replaceFragment(ListingsFragment.newInstance(), listings_container.id, LISTINGS_FRAGMENT_TAG)
            }
        } else {
            if(supportFragmentManager.findFragmentByTag(CATEGORIES_FRAGMENT_TAG) == null) {
                // for non-tablets, always load the categories fragment if not restoring.
                replaceFragment(CategoriesFragment.newInstance(), fragment_container.id, CATEGORIES_FRAGMENT_TAG)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // listen for new category events
        disposables.add(Bus.observe(Category::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({handleCategory(it)})
        )
    }

    override fun onPause() {
        disposables.clear()
        super.onPause()
    }

    /**
     * Pops the backstack if the [CategoriesFragment] is not visible; otherwise, delegates back
     * presses to the [CategoriesFragment]
     */
    override fun onBackPressed() {
        val categoriesFrag = supportFragmentManager.findFragmentByTag(CATEGORIES_FRAGMENT_TAG) as CategoriesFragment
        if (!categoriesFrag.isVisible) {
            return supportFragmentManager.popBackStack()
        }
        categoriesFrag.handleBackPress()
    }

    private fun replaceFragment(fragment: Fragment, @IdRes frameId: Int, fragmentTag: String) {
        supportFragmentManager
                .beginTransaction()
                .replace(frameId, fragment, fragmentTag)
                .addToBackStack(null)
                .commit()
    }

    /**
     * For non-tablets, swap in the [ListingsFragment] if the category is a leaf; or, if the
     * category is a branch and the listings fragment is currently visible, swap in the
     * [CategoriesFragment]; otherwise, pass on the category to the listings fragment if visible.
     */
    private fun handleCategory(category: Category) {
        if(!isTablet()) {
            if (category.subCategories == null || category.subCategories.isEmpty()) {
                return replaceFragment(ListingsFragment.newInstance(category), fragment_container.id, LISTINGS_FRAGMENT_TAG)
            } else if(supportFragmentManager.findFragmentByTag(LISTINGS_FRAGMENT_TAG)?.isVisible == true) {
                return replaceFragment(CategoriesFragment.newInstance(), fragment_container.id, CATEGORIES_FRAGMENT_TAG)
            }
        }

        val listingsFragment = supportFragmentManager.findFragmentByTag(LISTINGS_FRAGMENT_TAG)
        if (listingsFragment != null && listingsFragment.isVisible) {
            (listingsFragment as ListingsFragment).onNewCategory(category)
        }
    }

    private fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }
}