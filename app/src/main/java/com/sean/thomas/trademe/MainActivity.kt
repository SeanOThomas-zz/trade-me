package com.sean.thomas.trademe


import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.sean.thomas.trademe.categories.CategoriesFragment
import com.sean.thomas.trademe.listings.ListingsFragment
import com.sean.thomas.trademe.listings.detail.ListingDetailFragment
import com.sean.thomas.trademe.network.models.Category
import com.sean.thomas.trademe.network.models.Listing
import com.sean.thomas.trademe.schedulers.SchedulersProviderImpl
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Contains one fragment container for phones, and two fragments for tablets. Responsibilities
 * include: fragment switching according to [isTablet] and delegating back presses to the
 * [CategoriesFragment] when necessary. Yes, not my favorite logic.
 */
class MainActivity: AppCompatActivity() {

    companion object {
        const val CATEGORIES_FRAGMENT_TAG = "categories_fragment_tag"
        const val LISTINGS_FRAGMENT_TAG = "listings_fragment_tag"
        const val LISTINGS_DETAIL_FRAGMENT_TAG = "listings_detail_fragment_tag"
    }

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isTablet()) {
            // tablet
            if(supportFragmentManager.findFragmentByTag(CATEGORIES_FRAGMENT_TAG) == null) {
                replaceFragment(CategoriesFragment.newInstance(), categories_container.id, CATEGORIES_FRAGMENT_TAG)
            }

            if(isTablet() && supportFragmentManager.findFragmentByTag(LISTINGS_FRAGMENT_TAG) == null) {
                replaceFragment(ListingsFragment.newInstance(), listings_container.id, LISTINGS_FRAGMENT_TAG)
            }
        } else {
            // phone
            if(supportFragmentManager.findFragmentByTag(CATEGORIES_FRAGMENT_TAG) == null) {
                replaceFragment(CategoriesFragment.newInstance(), fragment_container.id, CATEGORIES_FRAGMENT_TAG)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // listen for new category events
        disposables.add(Bus.observe(Category::class.java)
                .subscribeOn(SchedulersProviderImpl.background())
                .observeOn(SchedulersProviderImpl.mainThread())
                .subscribe({handleCategory(it)})
        )

        // listen for listing detail events
        disposables.add(Bus.observe(Listing::class.java)
                .subscribeOn(SchedulersProviderImpl.background())
                .observeOn(SchedulersProviderImpl.mainThread())
                .subscribe({handleListingDetail(it.listingId)})
        )
    }

    override fun onPause() {
        disposables.clear()
        super.onPause()
    }

    /**
     * For phones, pops the backstack if the [CategoriesFragment] is not visible; for tablets, pops
     * the backstack if the [ListingDetailFragment] is visible; otherwise, delegates back
     * presses to the [CategoriesFragment]
     */
    override fun onBackPressed() {
        val categoriesFrag = supportFragmentManager.findFragmentByTag(CATEGORIES_FRAGMENT_TAG) as CategoriesFragment
        if (!isTablet()) {
            // phone
            if (!categoriesFrag.isVisible) {
                return supportFragmentManager.popBackStack()
            }
        } else {
            // tablet
            val listingDetailFragment = supportFragmentManager.findFragmentByTag(LISTINGS_DETAIL_FRAGMENT_TAG)
            if (isTablet() && listingDetailFragment != null && listingDetailFragment.isVisible) {
                return supportFragmentManager.popBackStack()
            }
        }
        categoriesFrag.handleBackPress()
    }

    /**
     * For phones, swaps in the [ListingsFragment] if the category is a leaf; or, if the
     * category is a branch and the listings fragment is currently visible, swap in the
     * [CategoriesFragment]; for tablets, swaps in the [ListingsFragment] if the detail is visible,
     * otherwise pass on the category to the listings fragment.
     */
    private fun handleCategory(category: Category) {
        if(!isTablet()) {
            // phone
            if (category.subCategories == null || category.subCategories.isEmpty()) {
                replaceFragment(ListingsFragment.newInstance(category), fragment_container.id, LISTINGS_FRAGMENT_TAG)
            } else if(supportFragmentManager.findFragmentByTag(LISTINGS_FRAGMENT_TAG)?.isVisible == true) {
                replaceFragment(CategoriesFragment.newInstance(), fragment_container.id, CATEGORIES_FRAGMENT_TAG)
            }
        } else {
            // tablet
            val listingsFragment = supportFragmentManager.findFragmentByTag(LISTINGS_FRAGMENT_TAG)
            if (listingsFragment != null && listingsFragment.isVisible) {
                return (listingsFragment as ListingsFragment).onNewCategory(category)
            }
            replaceFragment(ListingsFragment.newInstance(category), listings_container.id, LISTINGS_FRAGMENT_TAG)
        }
    }

    /**
     * Swaps in the [ListingDetailFragment]
     */
    private fun handleListingDetail(listingId: String) {
        val containerId =  if(isTablet()) listings_container else fragment_container
        replaceFragment(ListingDetailFragment.newInstance(listingId), containerId.id, LISTINGS_DETAIL_FRAGMENT_TAG)
    }

    private fun replaceFragment(fragment: Fragment, @IdRes frameId: Int, fragmentTag: String) {
        supportFragmentManager
                .beginTransaction()
                .replace(frameId, fragment, fragmentTag)
                .addToBackStack(null)
                .commit()
    }

    private fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }
}