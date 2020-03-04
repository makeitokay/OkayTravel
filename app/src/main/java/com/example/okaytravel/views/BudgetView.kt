package com.example.okaytravel.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.github.mikephil.charting.data.PieEntry

@StateStrategyType(value = AddToEndStrategy::class)
interface BudgetView: MvpView {
    fun showMessage(message: String)
    fun showMessage(resourceId: Int)

    fun showBudgetLoading()
    fun showPieChart()
    fun updatePieChart(items: ArrayList<PieEntry>)
    fun setPieChartCenterText(text: String)

    fun openAddBudgetDialog()
    fun showLoadingDialog()
    fun dismissLoadingDialog()

}