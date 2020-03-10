package com.example.okaytravel.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.database.PlaceDatabaseHelper
import com.example.okaytravel.database.TripDatabaseHelper
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.helpers.UsersApiHelper
import com.example.okaytravel.isInternetAvailable
import com.example.okaytravel.models.PlaceModel
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.views.PlacesView

@InjectViewState
class PlacesPresenter(private val context: Context, private val trip: TripModel) :
    MvpPresenter<PlacesView>() {

    private val placeDBHelper = PlaceDatabaseHelper()

    private val usersDBHelper = UsersDatabaseHelper()

    private val usersApiHelper = UsersApiHelper()

    private val sessionSharedPref = SharedPrefHelper("session", context)

    private val currentUser = usersDBHelper.getUserByLogin(sessionSharedPref.getCurrentUser())

    fun sync(onSuccess: () -> Unit = {}) {
        if (currentUser == null || currentUser.anonymous)
            return
        if (!isInternetAvailable(context)) {
            updateItems()
            viewState.showPlaces()
            viewState.showMessage(R.string.noInternetConnection)
            return
        }
        usersApiHelper.sync(currentUser, {
            viewState.showPlaces()
            onSuccess()
        }, {
            updateItems()
            viewState.showPlaces()
            viewState.showMessage(R.string.syncError)
        })
    }

    fun updateAll() {
        if (currentUser == null) return
        if (currentUser.anonymous) {
            viewState.showPlaces()
            updateItems()
            return
        }
        sync {
            updateItems()
        }
    }

    fun updateItems() {
        val places = trip.places()

        places.sortBy { it.date }
        val groupedPlaces = places.groupBy { it.date }

        val placesData: MutableMap<String, MutableList<PlaceModel>> = mutableMapOf()
        val placeDates: MutableList<String> = mutableListOf()

        groupedPlaces.keys.forEach { date ->
            date?.let {
                placeDates.add(date)
                placesData += Pair(date, mutableListOf())
                groupedPlaces[date]?.forEach { place ->
                    placesData[date]?.add(place)
                }
            }
        }
        viewState.updatePlaces(placeDates, placesData)
    }
}