package com.example.okaytravel.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.database.PlaceDatabaseHelper
import com.example.okaytravel.models.PlaceModel
import com.example.okaytravel.views.PlaceRoutesView
import com.yandex.mapkit.geometry.Point

@InjectViewState
class PlaceRoutesPresenter: MvpPresenter<PlaceRoutesView>() {

    private val placeDBHelper = PlaceDatabaseHelper()

    fun showInputPlaces(placesUuids: ArrayList<String>) {
        val places = mutableListOf<PlaceModel>()
        placesUuids.forEach { places.add(placeDBHelper.getPlaceByUuid(it)!!) }
        when (places.size) {
            1 -> viewState.showPlace(places[0].point())
            else -> {
                val points = mutableListOf<Point>()
                places.forEach { points.add(it.point()) }
                viewState.showRoute(points)
            }
        }
        if (places.isNotEmpty()) viewState.moveMap(places[0].point())
    }

}