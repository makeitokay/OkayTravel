package com.example.okaytravel.presenters

import android.content.Context
import android.content.res.Resources
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.api.models.okaytravelserver.AuthBody
import com.example.okaytravel.api.services.OkayTravelApiService
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.helpers.UsersApiHelper
import com.example.okaytravel.isInternetAvailable
import com.example.okaytravel.sha256
import com.example.okaytravel.views.LoginView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class LoginPresenter(private val context: Context): MvpPresenter<LoginView>() {

    private val usersApiHelper = UsersApiHelper()
    private val sessionSharedPref: SharedPrefHelper = SharedPrefHelper("session", context)
    private val usersDBHelper = UsersDatabaseHelper()

    fun Login(login: String, password: String) {
        viewState.startSigningIn()

        if (!validateInputData(login, password)) {
            viewState.endSigningIn()
            return
        }

        usersApiHelper.auth(login, password.sha256(), {
            if (!it.error) {
                val userInfo = it.user
                if (usersDBHelper.getUserByUsername(userInfo?.username!!) == null) {
                    usersDBHelper.createUser(
                        userInfo.username, userInfo.email, userInfo.passwordHash, userInfo.accessToken
                    )
                }

                viewState.endSigningIn()
                viewState.showMessage("Authorized!")
                sessionSharedPref.setCurrentUser(login)
                viewState.openHome()

            } else {
                viewState.showMessage(it.message)
                viewState.endSigningIn()
            }
        }, {
            viewState.showMessage(R.string.unknownError)
            viewState.endSigningIn()
        })
    }

    fun loginAsAnonymous() {
        sessionSharedPref.setCurrentUser("Anonymous")
        viewState.openHome()
    }

    private fun validateInputData(login: String, password: String): Boolean {
        if (login.isEmpty() || password.isEmpty()) {
            viewState.showMessage(R.string.emptyFieldsError)
            return false
        }
        return true
    }
}