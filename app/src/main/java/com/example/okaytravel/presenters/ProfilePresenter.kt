package com.example.okaytravel.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.views.ProfileView

@InjectViewState
class ProfilePresenter(private val context: Context): MvpPresenter<ProfileView>() {

    private val usersDBHelper = UsersDatabaseHelper()
    private val sessionSharedPref = SharedPrefHelper("session", context)
    private val currentUser = usersDBHelper.getUserByLogin(sessionSharedPref.getCurrentUser())

    fun update() {
        currentUser?.let {
            viewState.setUsername(it.username!!)
            if (it.anonymous) viewState.hideLogoutBtn()
            else viewState.hideSignInButtons()
        }

    }

    fun logout() {
        sessionSharedPref.removeCurrentUser()
        viewState.openLogin()
    }
}