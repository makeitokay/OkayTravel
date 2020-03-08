package com.example.okaytravel.activities.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.activeandroid.query.Update
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.SignUpActivity
import com.example.okaytravel.activities.TripActivity
import com.example.okaytravel.presenters.BudgetPresenter
import com.example.okaytravel.views.BudgetView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.dialog_budget_add.view.*
import kotlinx.android.synthetic.main.fragment_budget.*

class BudgetFragment: BaseFragment(false), BudgetView {

    override val fragmentNameResource: Int
        get() = R.string.budget

    @ProvidePresenter
    fun provideBudgetPresenter(): BudgetPresenter {
        return BudgetPresenter(this.requireActivity(), (activity as TripActivity).trip)
    }

    @InjectPresenter
    lateinit var budgetPresenter: BudgetPresenter

    private var addBudgetDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_budget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addBudgetBtn.setOnClickListener {
            openAddBudgetDialog()
        }

        budgetPieChart.description.isEnabled = false
        budgetPieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        budgetPieChart.dragDecelerationFrictionCoef = 0.15f

        budgetPieChart.isDrawHoleEnabled = true
        budgetPieChart.setHoleColor(Color.WHITE)
        budgetPieChart.transparentCircleRadius = 60f
        budgetPieChart.setDrawEntryLabels(false)

        budgetPieChart.legend.isWordWrapEnabled = true
        budgetPieChart.legend.textSize = 20f

        buyPremiumBtn.setOnClickListener {
            budgetPresenter.buyPremium()
        }
    }

    override fun openSignUp() {
        startActivity(Intent(this.requireActivity(), SignUpActivity::class.java))
    }

    override fun hideBudgetContent() {
        budgetContainer.visibility = View.GONE
        buyPremiumContainer.visibility = View.VISIBLE
        loading.visibility = View.GONE
    }

    override fun setPieChartCenterText(text: String) {
        budgetPieChart.centerText = text
    }
    
    // TODO: убрать двойной прелоадер при добавлении (возможно в Things аналогичная ситуация)
    override fun showBudgetLoading() {
        loading.visibility = View.VISIBLE
        budgetPieChart.visibility = View.GONE
        buyPremiumContainer.visibility = View.GONE
    }

    override fun showPieChart() {
        loading.visibility = View.GONE
        buyPremiumContainer.visibility = View.GONE
        budgetContainer.visibility = View.VISIBLE
        budgetPieChart.visibility = View.VISIBLE
    }

    private fun onAddButtonClicked(category: String, rawAmount: String) {
        budgetPresenter.addBudget(category, rawAmount)
    }

    private fun onCancelButtonClicked() {}

    override fun openAddBudgetDialog() {
        activity?.let {
            val view = layoutInflater.inflate(R.layout.dialog_budget_add, null)
            val dialog = AlertDialog.Builder(context!!)
                .setMessage(R.string.budget)
                .setView(view)
                .setPositiveButton(R.string.addButton, null)
                .setNegativeButton(R.string.cancelButton) { _, _ -> this.onCancelButtonClicked() }
                .create()
            addBudgetDialog = dialog
            dialog.setOnShowListener {
                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setOnClickListener {
                    this.onAddButtonClicked(
                        view.placeBudgetCategory.text.toString(), view.placeBudgetAmount.text.toString()
                    )
                }
            }
            dialog.show()
        }
    }

    override fun dismissAddBudgetDialog() {
        addBudgetDialog?.dismiss()
    }

    private fun getColors(): MutableList<Int> {
        val colors: MutableList<Int> = mutableListOf()
        ColorTemplate.MATERIAL_COLORS.forEach { colors.add(it) }
        ColorTemplate.LIBERTY_COLORS.forEach { colors.add(it) }
        ColorTemplate.PASTEL_COLORS.forEach { colors.add(it) }
        return colors
    }

    override fun updatePieChart(items: ArrayList<PieEntry>) {
        val dataSet = PieDataSet(items, "")
        dataSet.sliceSpace = 4f
        dataSet.selectionShift = 15f
        dataSet.colors = getColors()

        val data = PieData(dataSet)

        data.setValueTextSize(15f)
        data.setValueTextColor(Color.WHITE)

        budgetPieChart.data = data
        budgetPieChart.animateY(1000, Easing.EaseInOutCubic)
    }

    override fun update() {
        showBudgetLoading()
        budgetPresenter.updateAll()
    }
}