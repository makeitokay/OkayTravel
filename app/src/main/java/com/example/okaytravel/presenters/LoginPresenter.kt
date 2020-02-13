package com.example.okaytravel.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.api.models.okaytravelserver.AuthBody
import com.example.okaytravel.api.services.OkayTravelApiService
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.isInternetAvailable
import com.example.okaytravel.sha256
import com.example.okaytravel.views.LoginView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class LoginPresenter(private val context: Context): MvpPresenter<LoginView>() {

    private val apiService: OkayTravelApiService = OkayTravelApiService.create()
    private val sessionSharedPref: SharedPrefHelper = SharedPrefHelper("session", context)
    private val usersDBHelper = UsersDatabaseHelper()

    fun doLogin(login: String, password: String) {
        viewState.startSigningIn()

        val body = AuthBody(login, password.sha256())
        apiService.auth(body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({ userInfoResponse ->
                if (!userInfoResponse.error) {
                    val userInfo = userInfoResponse.user
                    userInfo?.let {
                        usersDBHelper.createUser(it.username!!, it.email!!, it.passwordHash!!, it.accessToken!!)
                    }

                    viewState.endSigningIn()
//                    viewState.showMessage("Authorized!")
                    viewState.openMainActivity()
                    sessionSharedPref.setCurrentUser(login)

                } else {
                    viewState.showMessage(userInfoResponse.message)
                    viewState.endSigningIn()
                }
            }, { error ->
                println(error)
                viewState.showMessage(R.string.unknownError)
                viewState.endSigningIn()
            })
    }
}