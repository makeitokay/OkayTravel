package com.example.okaytravel.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.helpers.UsersApiHelper
import com.example.okaytravel.isInternetAvailable
import com.example.okaytravel.views.SplashView

@InjectViewState
class SplashPresenter(private val context: Context) : MvpPresenter<SplashView>() {

    private val usersApiHelper = UsersApiHelper()
    private val usersDBHelper: UsersDatabaseHelper = UsersDatabaseHelper()
    private val sessionSharedPref: SharedPrefHelper = SharedPrefHelper("session", context)
    private val usageInfoSharedPref: SharedPrefHelper = SharedPrefHelper("usageInfo", context)

    fun checkUserSession() {
        if (!usageInfoSharedPref.getHasVisited()) {
            usageInfoSharedPref.setHasVisited()
            sessionSharedPref.setCurrentUser(usersDBHelper.createAnonymousUser().username!!)
            viewState.startIntro()
            return
        }

        val currentUser = usersDBHelper.getUserByLogin(sessionSharedPref.getCurrentUser())
        currentUser?.let {
            when {
                currentUser.anonymous -> viewState.startHome()
                isInternetAvailable(context) -> usersApiHelper.auth(
                    currentUser.username!!,
                    currentUser.passwordHash!!,
                    {
                        if (!it.error) {
                            viewState.startHome()

                        } else {
                            viewState.showMessage(it.message)
                            viewState.startLogin()
                        }
                    },
                    {
                        viewState.showMessage(R.string.unknownError)
                        viewState.startLogin()
                    })
                else -> {
                    viewState.startHome()
                }
            }
            return
        }
        viewState.startLogin()
    }
}