package com.example.okaytravel.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.api.services.OkayTravelApiService
import com.example.okaytravel.database.TripDatabaseHelper
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.models.UserModel
import com.example.okaytravel.views.HomeView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class HomePresenter(private val context: Context): MvpPresenter<HomeView>() {

    private val usersDBHelper = UsersDatabaseHelper()
    private val tripsDBHelper = TripDatabaseHelper()
    private val apiService: OkayTravelApiService = OkayTravelApiService.create()
    private val sessionSharedPref = SharedPrefHelper("session", context)

    private fun getCurrentUser(): UserModel? {
        return usersDBHelper.getUserByLogin(sessionSharedPref.getCurrentUser() ?: return null)
    }

    fun sync(): Boolean {
        val user = getCurrentUser()
        user?.let {
            val syncBody = usersDBHelper.serializeUser(user.id) ?: return false
            apiService.sync(syncBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    if (it.error == null)
                        usersDBHelper.updateUser(it)
                    viewState.showMessage("Synced!")
                }, { error ->
                    println(error)
                    viewState.showMessage(R.string.syncError)
                })
        }
        return true
    }

    fun addTrip(ownPlace: String, rawDuration: String, startDate: String) {
        val duration: Int? = try { rawDuration.toInt() } catch ( e: NumberFormatException ) { null }
        if (duration == null || duration <= 0 || duration > 100) {
            viewState.showMessage("Некорректная продолжительность")
            return
        }
        tripsDBHelper.create(ownPlace, startDate, duration, getCurrentUser() ?: return)
        sync()
    }

}