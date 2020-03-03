package com.example.okaytravel.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.database.BudgetElementDatabaseHelper
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.helpers.UsersApiHelper
import com.example.okaytravel.isInternetAvailable
import com.example.okaytravel.models.BudgetElementModel
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.uuid
import com.example.okaytravel.views.BudgetView
import com.github.mikephil.charting.data.PieEntry

@InjectViewState
class BudgetPresenter(private val context: Context, private val trip: TripModel): MvpPresenter<BudgetView>() {

    private val budgetDBHelper = BudgetElementDatabaseHelper()

    private val usersDBHelper = UsersDatabaseHelper()

    private val usersApiHelper = UsersApiHelper()

    private val sessionSharedPref = SharedPrefHelper("session", context)

    private val currentUser = usersDBHelper.getUserByLogin(sessionSharedPref.getCurrentUser())

    fun sync(onSuccess: () -> Unit = {}) {
        if (currentUser == null || currentUser.anonymous)
            return
        if (!isInternetAvailable(context)) {
            viewState.showMessage(R.string.noInternetConnection)
            return
        }
        usersApiHelper.sync(currentUser, {
            viewState.showPieChart()
            onSuccess()
        }, {
            viewState.showPieChart()
            viewState.showMessage(R.string.syncError)
            updateAll()
        })
    }

    fun addBudget(category: String, rawAmount: String) {
        if (currentUser == null || !validateInputData(category, rawAmount)) {
            return
        }

        viewState.showLoadingDialog()
        val amount = rawAmount.toInt()
        if (currentUser.anonymous) {
            budgetDBHelper.create(uuid(), amount, category, trip)
            currentUser.updateTrigger()
            viewState.dismissLoadingDialog()
            updateAll()
            return
        }
        viewState.showBudgetLoading()
        sync {
            budgetDBHelper.create(uuid(), amount, category, trip)
            currentUser.updateTrigger()
            sync()
            viewState.dismissLoadingDialog()
            viewState.showPieChart()
            updateAll()
        }
    }

    fun updateAll() {
        currentUser?.let {
            val items = ArrayList<PieEntry>()
            trip.budget().forEach {
                items.add(PieEntry(it.amount?.toFloat()!!, it.category))
            }
            viewState.updatePieChart(items)
        }
    }

    private fun validateInputData(category: String, rawAmount: String): Boolean {
        if (category.isEmpty() || rawAmount.isEmpty()) {
            viewState.showMessage(R.string.emptyFieldsError)
        }
        val budgetAmount: Int? = try { rawAmount.toInt() } catch ( e: NumberFormatException ) { null }
        if (budgetAmount == null || budgetAmount <= 0 || budgetAmount > 999999998) {
            viewState.showMessage(R.string.invalidBudgetAmount)
            return false
        }
        return true
    }
}