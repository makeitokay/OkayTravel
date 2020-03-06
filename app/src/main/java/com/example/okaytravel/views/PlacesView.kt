package com.example.okaytravel.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.okaytravel.adapters.placeitems.PlaceListItem

@StateStrategyType(value = AddToEndStrategy::class)
interface PlacesView: MvpView {
    fun showMessage(message: String)
    fun showMessage(resourceId: Int)

    fun openPlacesMap()

    fun showPlacesLoading()
    fun showPlaces()

    fun updatePlaces(places: MutableList<PlaceListItem>)
}