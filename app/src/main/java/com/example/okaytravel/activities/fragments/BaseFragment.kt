package com.example.okaytravel.activities.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.arellomobile.mvp.MvpAppCompatFragment
import com.example.okaytravel.R
import com.example.okaytravel.activities.BaseActivity
import kotlinx.android.synthetic.main.dialog_loading.view.*

abstract class BaseFragment(private val changeToolbarTitle: Boolean = true): MvpAppCompatFragment() {

    abstract val fragmentNameResource: Int

    abstract fun update()

    private lateinit var loadingDialog: Dialog

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (changeToolbarTitle) (activity as BaseActivity).setToolbarTitle(getString(fragmentNameResource))
    }

    fun showMessage(message: String) {
        Toast.makeText(this.requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    fun showLoadingDialog() {
        val builder = AlertDialog.Builder(this.requireActivity())
        val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
        builder.setView(dialogView)
        dialogView.loading.visibility = View.VISIBLE
        loadingDialog = builder.create()
        loadingDialog.show()
    }

    fun dismissLoadingDialog() {
        loadingDialog.dismiss()
    }

    fun showMessage(resourceId: Int) {
        showMessage(getString(resourceId))
    }

}