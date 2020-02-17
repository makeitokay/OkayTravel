package com.example.okaytravel.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.database.TripDatabaseHelper
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.helpers.UsersApiHelper
import com.example.okaytravel.isInternetAvailable
import com.example.okaytravel.uuid
import com.example.okaytravel.views.TripsView

@InjectViewState
class TripsPresenter(private val context: Context): MvpPresenter<TripsView>() {

    private val usersDBHelper = UsersDatabaseHelper()
    private val tripsDBHelper = TripDatabaseHelper()

    private val usersApiHelper = UsersApiHelper()

    private val sessionSharedPref = SharedPrefHelper("session", context)

    private val currentUser = usersDBHelper.getUserByLogin(sessionSharedPref.getCurrentUser())

    fun sync(onSuccess: () -> Unit = {}) {
        if (currentUser == null || currentUser.anonymous)
            return
        if (!isInternetAvailable(context)) {
            viewState.showMessage(R.string.noInternetConnection)
            return
        }
        usersApiHelper.sync(currentUser, {
            updateAll()
            viewState.showMessage("Synced!")
            onSuccess()
        }, {
            viewState.showMessage(R.string.syncError)
        })
    }

    fun addTrip(ownPlace: String, rawDuration: String, startDate: String) {
        if (currentUser == null)
            return

        if (ownPlace.isEmpty() || startDate.isEmpty() || rawDuration.isEmpty()) {
            viewState.showMessage(R.string.emptyFieldsError)
            return
        }
        val duration: Int? = try { rawDuration.toInt() } catch ( e: NumberFormatException ) { null }
        if (duration == null || duration <= 0 || duration > 365) {
            viewState.showMessage(R.string.invalidTripDuration)
            return
        }

        if (currentUser.anonymous) {
            tripsDBHelper.create(uuid(), ownPlace, startDate, duration, currentUser)
            currentUser.updateTrigger()
            updateAll()
            return
        }

        sync {
            tripsDBHelper.create(uuid(), ownPlace, startDate, duration, currentUser)
            currentUser.updateTrigger()
            sync()
        }
    }

    fun updateAll() {
        currentUser?.let { viewState.updateTrips(currentUser.trips()) }
    }

}