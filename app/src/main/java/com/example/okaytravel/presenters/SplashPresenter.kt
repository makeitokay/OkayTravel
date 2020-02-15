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
import com.example.okaytravel.views.SplashView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class SplashPresenter(private val context: Context): MvpPresenter<SplashView>() {

    private val apiService: OkayTravelApiService = OkayTravelApiService.create()
    private val usersDBHelper: UsersDatabaseHelper = UsersDatabaseHelper()
    private val sessionSharedPref: SharedPrefHelper = SharedPrefHelper("session", context)
    private val usageInfoSharedPref: SharedPrefHelper = SharedPrefHelper("usageInfo", context)

    fun checkUserSession() {
        if (!usageInfoSharedPref.getHasVisited()) {
            usageInfoSharedPref.setHasVisited()
            viewState.startIntro()
            return
        }

        val currentUser = usersDBHelper.getUserByLogin(sessionSharedPref.getCurrentUser())
        currentUser?.let {
            if (isInternetAvailable(context)) {
                val body = AuthBody(currentUser.username!!, currentUser.passwordHash!!)
                apiService.auth(body)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe ({ userInfoResponse ->
                        if (!userInfoResponse.error) {
                            viewState.showMessage("Authorized!")
                            viewState.startHome()

                        } else {
                            viewState.showMessage(userInfoResponse.message)
                            viewState.startLogin()
                        }
                    }, {
                        viewState.showMessage(R.string.unknownError)
                        viewState.startLogin()
                    })
            }
            else {
                viewState.showMessage(R.string.noInternetConnection)
                viewState.startHome()
            }
            return
        }
    }
}