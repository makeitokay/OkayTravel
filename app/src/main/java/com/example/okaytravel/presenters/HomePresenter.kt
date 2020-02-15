package com.example.okaytravel.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.api.services.OkayTravelApiService
import com.example.okaytravel.database.TripDatabaseHelper
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.isInternetAvailable
import com.example.okaytravel.views.HomeView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class HomePresenter(private val context: Context): MvpPresenter<HomeView>() {

    private val usersDBHelper = UsersDatabaseHelper()
    private val tripsDBHelper = TripDatabaseHelper()
    private val apiService: OkayTravelApiService = OkayTravelApiService.create()
    private val sessionSharedPref = SharedPrefHelper("session", context)

    private val currentUser = usersDBHelper.getUserByLogin(sessionSharedPref.getCurrentUser())

    fun sync(onSuccess: () -> Unit = {}): Boolean {
        if (currentUser == null)
            return false
        if (!isInternetAvailable(context)) {
            viewState.showMessage(R.string.noInternetConnection)
            return false
        }
        val syncBody = usersDBHelper.serializeUser(currentUser.id) ?: return false
        apiService.sync(syncBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                if (it.error == null)
                    usersDBHelper.updateUser(it)
                onSuccess()
                viewState.updateTrips(currentUser.trips())
                viewState.showMessage("Synced!")
            }, { error ->
                println(error)
                viewState.showMessage(R.string.syncError)
            })

        return true
    }

    fun addTrip(ownPlace: String, rawDuration: String, startDate: String) {
        if (currentUser == null)
            return

        val duration: Int? = try { rawDuration.toInt() } catch ( e: NumberFormatException ) { null }
        if (duration == null || duration <= 0 || duration > 100) {
            viewState.showMessage("Некорректная продолжительность")
            return
        }

        sync {
            tripsDBHelper.create(ownPlace, startDate, duration, currentUser)
            sync()
        }
    }

}