package com.example.okaytravel.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.yandex.mapkit.geometry.Point

@StateStrategyType(value = AddToEndStrategy::class)
interface PlacesMapView: MvpView {
    fun showMessage(message: String)
    fun showMessage(resourceId: Int)

    fun enableBudgetInput()
    fun disableBudgetInput()

    fun focusBudgetCategoryInput()
    fun focusBudgetAmountInput()

    fun showLoadingDialog()
    fun dismissLoadingDialog()

    fun moveMap(point: Point)

    fun openPlaces()
}