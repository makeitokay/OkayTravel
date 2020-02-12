package com.example.okaytravel.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface SplashView: MvpView {
    fun startLogin()
    fun startIntro()
    fun startHome()

    fun showMessage(message: String)
    fun showMessage(resourceId: Int)
}