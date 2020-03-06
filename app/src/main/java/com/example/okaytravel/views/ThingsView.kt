package com.example.okaytravel.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.okaytravel.adapters.thingitems.ThingListItem

@StateStrategyType(value = AddToEndStrategy::class)
interface ThingsView: MvpView {
    fun showMessage(message: String)
    fun showMessage(resourceId: Int)

    fun showThingsLoading()
    fun hideThingsLoading()

    fun openAddThingDialog()
    fun showLoadingDialog()
    fun dismissLoadingDialog()

    fun showThings()
    fun hideTakenThings()
    fun showTakenThings()
    fun hideNotTakenThings()
    fun showNotTakenThings()

    fun updateThingsItems(notTakenItems: MutableList<ThingListItem>, takenItems: MutableList<ThingListItem>)
}