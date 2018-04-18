package com.sean.thomas.trademe


import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.sean.thomas.trademe.categories.CategoriesFragment
import com.sean.thomas.trademe.listings.ListingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

    companion object {
        const val CATEGORIES_FRAGMENT_TAG = "categories_fragment_tag"
        const val LISTINGS_FRAGMENT_TAG = "listings_fragment_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(supportFragmentManager.findFragmentByTag(CATEGORIES_FRAGMENT_TAG) == null) {
            addFragment(CategoriesFragment.newInstance(), categories_container.id, CATEGORIES_FRAGMENT_TAG)
        }

        if(listings_container != null && supportFragmentManager.findFragmentByTag(LISTINGS_FRAGMENT_TAG) == null) {
            addFragment(ListingsFragment.newInstance(), listings_container.id, LISTINGS_FRAGMENT_TAG)
        }
    }

    /**
     * Delegates back presses to the [CategoriesFragment]
     */
    override fun onBackPressed() {
        val categoriesFrag = supportFragmentManager.findFragmentByTag(CATEGORIES_FRAGMENT_TAG) as CategoriesFragment
        categoriesFrag.handleBackPress()
    }

    private fun addFragment(fragment: Fragment, @IdRes frameId: Int, fragmentTag: String) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(frameId, fragment, fragmentTag)
        transaction.commit()
    }
}