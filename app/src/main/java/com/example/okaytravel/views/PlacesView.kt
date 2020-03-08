package com.example.okaytravel.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.okaytravel.models.PlaceModel

@StateStrategyType(value = AddToEndStrategy::class)
interface PlacesView: MvpView {
    fun showMessage(message: String)
    fun showMessage(resourceId: Int)

    fun openPlacesMap()
    fun openPlace()
    fun openRoute()

    fun showPlacesLoading()
    fun showPlaces()

    fun updatePlaces(dates: MutableList<String>, places: Map<String, MutableList<PlaceModel>>)
    fun updateActionButtons()
}