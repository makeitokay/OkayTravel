package com.example.okaytravel.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.api.models.okaytravelserver.AuthBody
import com.example.okaytravel.api.services.OkayTravelApiService
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.sha256
import com.example.okaytravel.views.LoginView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class LoginPresenter: MvpPresenter<LoginView>() {

    private val apiService: OkayTravelApiService = OkayTravelApiService.create()
    private val usersDBHelper: UsersDatabaseHelper = UsersDatabaseHelper()

    fun doLogin(login: String, password: String) {
        viewState.startSigningIn()

        val body = AuthBody(login, password.sha256())
        apiService.auth(body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                if (!it.error) {
                    usersDBHelper.setUserAccessToken(login, it.accessToken!!)
                    viewState.endSigningIn()
                    viewState.showMessage("Authorized!")
                    viewState.openMainActivity()

                } else {
                    viewState.showMessage(it.message!!)
                    viewState.endSigningIn()
                }
            }, { error ->
                println(error)
                viewState.showMessage(R.string.unknownError)
                viewState.endSigningIn()
            })
    }
}