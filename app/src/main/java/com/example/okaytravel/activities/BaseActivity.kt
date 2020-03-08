package com.example.okaytravel.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.arellomobile.mvp.MvpAppCompatActivity
import com.example.okaytravel.R
import com.example.okaytravel.activities.fragments.BaseFragment
import kotlinx.android.synthetic.main.dialog_loading.view.*
import kotlinx.android.synthetic.main.toolbar.*

@SuppressLint("Registered")
abstract class BaseActivity: MvpAppCompatActivity() {

    protected abstract val fragmentContainer: Int?

    private lateinit var loadingDialog: Dialog

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showMessage(resourceId: Int) {
        showMessage(getString(resourceId))
    }

    fun loadFragment(fragment: BaseFragment?): Boolean {
        val currentFragmentContainer = fragmentContainer
        if (currentFragmentContainer != null && fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.replace(currentFragmentContainer, fragment).commit()
            return true
        }
        return false
    }

    fun setToolbarBackButton() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back_white_24dp)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun setToolbarHamburgerButton() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.navigationIcon = getDrawable(R.drawable.ic_menu_black_24dp)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun setToolbarTitle(title: String) {
        toolbar.title = title
    }

    fun showLoadingDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
        builder.setView(dialogView)
        dialogView.loading.visibility = View.VISIBLE
        loadingDialog = builder.create()
        loadingDialog.show()
    }

    fun dismissLoadingDialog() {
        loadingDialog.dismiss()
    }

}