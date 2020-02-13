package com.example.okaytravel.presenters

import android.content.Context
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.api.services.OkayTravelApiService
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.views.HomeView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomePresenter(private val context: Context): MvpPresenter<HomeView>() {

    private val usersDBHelper = UsersDatabaseHelper()
    private val apiService: OkayTravelApiService = OkayTravelApiService.create()
    private val sessionSharedPref = SharedPrefHelper("session", context)

    fun sync(): Boolean {
        val currentUserLogin = sessionSharedPref.getCurrentUser()
        currentUserLogin?.let {
            val user = usersDBHelper.getUserByLogin(currentUserLogin) ?: return false
            val syncBody = usersDBHelper.serializeUser(user.id) ?: return false
            apiService.sync(syncBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    if (it.error == null)
                        usersDBHelper.updateUser(it)
                }, { error ->
                    println(error)
                    viewState.showMessage(R.string.syncError)
                })
        }
        return true
    }

}