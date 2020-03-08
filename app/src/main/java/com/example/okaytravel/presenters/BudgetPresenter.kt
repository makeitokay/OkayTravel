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
class BudgetPresenter(private val context: Context, private val trip: TripModel) :
    MvpPresenter<BudgetView>() {

    private val budgetDBHelper = BudgetElementDatabaseHelper()

    private val usersDBHelper = UsersDatabaseHelper()

    private val usersApiHelper = UsersApiHelper()

    private val sessionSharedPref = SharedPrefHelper("session", context)

    private val currentUser = usersDBHelper.getUserByLogin(sessionSharedPref.getCurrentUser())

    fun sync(onSuccess: () -> Unit = {}) {
        if (currentUser == null || currentUser.anonymous)
            return
        if (!isInternetAvailable(context)) {
            updateItems()
            viewState.showPieChart()
            viewState.showMessage(R.string.noInternetConnection)
            return
        }
        usersApiHelper.sync(currentUser, {
            viewState.showPieChart()
            onSuccess()
        }, {
            updateItems()
            viewState.showPieChart()
            viewState.showMessage(R.string.syncError)
        })
    }

    fun addBudget(category: String, rawAmount: String) {
        if (currentUser == null || !validateInputData(category, rawAmount)) {
            return
        }
        viewState.dismissAddBudgetDialog()

        viewState.showLoadingDialog()
        val amount = rawAmount.toInt()
        if (currentUser.anonymous) {
            budgetDBHelper.create(uuid(), amount, category, trip)
            currentUser.updateTrigger()
            viewState.dismissLoadingDialog()
            updateItems()
            return
        }
        viewState.showBudgetLoading()
        sync {
            budgetDBHelper.create(uuid(), amount, category, trip)
            currentUser.updateTrigger()
            sync()
            viewState.dismissLoadingDialog()
            updateItems()
        }
    }

    fun updateAll() {
        if (currentUser == null) return
        if (!currentUser.premium) {
            viewState.hideBudgetContent()
            return
        }
        if (currentUser.anonymous) {
            viewState.showPieChart()
            updateItems()
            return
        }
        sync {
            updateItems()
        }
    }

    private fun updateItems() {
        currentUser?.let {
            var totalAmount = 0f
            val items = ArrayList<PieEntry>()
            trip.budget().forEach {
                val amount = it.amount?.toFloat() ?: 0f
                totalAmount += amount
                items.add(PieEntry(amount, it.category))
            }
            viewState.updatePieChart(items)
            viewState.setPieChartCenterText("Всего:\n$totalAmount")
        }
    }

    private fun validateInputData(category: String, rawAmount: String): Boolean {
        // TODO: ограничение на длину категории: не более 30 символов
        if (category.isEmpty() || rawAmount.isEmpty()) {
            viewState.showMessage(R.string.emptyFieldsError)
            return false
        }
        val budgetAmount: Int? = try {
            rawAmount.toInt()
        } catch (e: NumberFormatException) {
            null
        }
        if (budgetAmount == null || budgetAmount <= 0 || budgetAmount > 999999998) {
            viewState.showMessage(R.string.invalidBudgetAmount)
            return false
        }
        return true
    }

    fun buyPremium() {
        if (currentUser == null) return
        if (currentUser.anonymous) {
            viewState.openSignUp()
            viewState.showMessage(R.string.needToSignUp)
            return
        }
        viewState.showBudgetLoading()
        sync {
            currentUser.buyPremium()
            sync()
            updateItems()
            viewState.showMessage(R.string.successPremiumBuy)
        }
    }
}