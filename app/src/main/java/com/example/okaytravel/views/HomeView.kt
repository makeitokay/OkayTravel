package com.example.okaytravel.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.okaytravel.models.TripModel

@StateStrategyType(value = AddToEndStrategy::class)
interface HomeView: MvpView {
    fun showMessage(message: String)
    fun showMessage(resourceId: Int)

    fun updateTrips(trips: List<TripModel>)
}