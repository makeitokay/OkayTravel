package com.example.okaytravel.activities.fragments

import android.os.Bundle
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.example.okaytravel.activities.BaseActivity

abstract class BaseFragment: MvpAppCompatFragment() {

    abstract val fragmentNameResource: Int

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).setToolbarTitle(getString(fragmentNameResource))
    }

    fun showMessage(message: String) {
        Toast.makeText(this.requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    fun showMessage(resourceId: Int) {
        showMessage(getString(resourceId))
    }
}