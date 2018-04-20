package com.sean.thomas.trademe

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_categories.*

/**
 * Responsibilities include: logging lifecycle transitions and implementing [BaseView] methods.
 */
abstract class BaseFragment: Fragment(), BaseView {

    companion object {
        val TAG = BaseFragment::class.java.canonicalName!!
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        Log.i(TAG, "onAttach: ${getChildTag()}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        Log.i(TAG, "onCreate: ${getChildTag()}")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "onCreateView: ${getChildTag()}")

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.i(TAG, "onActivityCreated: ${getChildTag()}")
    }

    override fun onStart() {
        super.onStart()

        Log.i(TAG, "onStart: ${getChildTag()}")
    }

    override fun onResume() {
        super.onResume()

        Log.i(TAG, "onResume: ${getChildTag()}")
    }

    override fun onPause() {
        Log.i(TAG, "onPause: ${getChildTag()}")

        super.onPause()

    }

    override fun onStop() {
        Log.i(TAG, "onStop: ${getChildTag()}")

        super.onStop()
    }

    override fun onDestroyView() {
        Log.i(TAG, "onDestroyView: ${getChildTag()}")

        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy: ${getChildTag()}")

        super.onDestroy()
    }

    override fun onDetach() {
        Log.i(TAG, "onDetach: ${getChildTag()}")

        super.onDetach()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        Log.i(TAG, "onConfigurationChanged: ${getChildTag()}")
    }

    override fun showNetworkErrorMessage() {
        Toast.makeText(activity, R.string.network_error_message, Toast.LENGTH_LONG).show()
    }

    override fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    abstract fun getChildTag() : String;
}