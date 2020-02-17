package com.example.okaytravel.presenters

import android.content.Context
import android.os.Handler
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.api.services.OkayTravelApiService
import com.example.okaytravel.database.TripDatabaseHelper
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.helpers.UsersApiHelper
import com.example.okaytravel.isInternetAvailable
import com.example.okaytravel.views.HomeView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class HomePresenter(private val context: Context): MvpPresenter<HomeView>() {

    private val usersDBHelper = UsersDatabaseHelper()
    private val sessionSharedPref = SharedPrefHelper("session", context)

    private val usersApiHelper = UsersApiHelper()

    private val currentUser = usersDBHelper.getUserByLogin(sessionSharedPref.getCurrentUser())

    fun sync(onSuccess: () -> Unit = {}) {
        if (currentUser == null || currentUser.anonymous)
            return
        if (!isInternetAvailable(context)) {
            viewState.showMessage(R.string.noInternetConnection)
            return
        }
        usersApiHelper.sync(currentUser, {
            viewState.showMessage("Synced!")
            onSuccess()
        }, {
            viewState.showMessage(R.string.syncError)
        })
    }

}