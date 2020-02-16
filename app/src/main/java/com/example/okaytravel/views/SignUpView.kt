package com.example.okaytravel.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface SignUpView: MvpView {
    fun showMessage(message: String)
    fun showMessage(resourceId: Int)

    fun startLoginView()
    fun showSyncAnonymUserDialog()

    fun startSigningUp()
    fun endSigningUp()

    fun focusUsername()
    fun focusEmail()
    fun focusPassword()
    fun focusPasswordAgain()
}