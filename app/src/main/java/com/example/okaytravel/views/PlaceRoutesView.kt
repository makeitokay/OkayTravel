package com.example.okaytravel.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.yandex.mapkit.geometry.Point

@StateStrategyType(value = AddToEndStrategy::class)
interface PlaceRoutesView: MvpView {

    fun showPlace(point: Point)
    fun showRoute(points: MutableList<Point>)
    fun moveMap(point: Point)

}