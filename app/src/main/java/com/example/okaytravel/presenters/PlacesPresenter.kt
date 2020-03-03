package com.example.okaytravel.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.adapters.placeitems.DateItem
import com.example.okaytravel.adapters.placeitems.PlaceItem
import com.example.okaytravel.adapters.placeitems.PlaceListItem
import com.example.okaytravel.database.PlaceDatabaseHelper
import com.example.okaytravel.database.TripDatabaseHelper
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.helpers.UsersApiHelper
import com.example.okaytravel.isInternetAvailable
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.views.PlacesView

@InjectViewState
class PlacesPresenter(private val context: Context, private val trip: TripModel): MvpPresenter<PlacesView>() {

    private val placeDBHelper = PlaceDatabaseHelper()

    private val usersDBHelper = UsersDatabaseHelper()

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
            onSuccess()
        }, {
            viewState.showMessage(R.string.syncError)
        })
    }

    fun updateAll() {
        currentUser?.let {
            val places = trip.places()
            places.sortBy { it.date }
            val groupedPlaces = places.groupBy { it.date }
            val placeItems: MutableList<PlaceListItem> = mutableListOf()
            groupedPlaces.keys.forEach { date ->
                placeItems.add(DateItem(date!!))
                groupedPlaces[date]?.forEach { place ->
                    placeItems.add(PlaceItem(place))
                }
            }
            viewState.updatePlaces(placeItems)
        }
    }
}