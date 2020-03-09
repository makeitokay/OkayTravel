package com.example.okaytravel.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.database.BudgetElementDatabaseHelper
import com.example.okaytravel.database.PlaceDatabaseHelper
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.helpers.UsersApiHelper
import com.example.okaytravel.isInternetAvailable
import com.example.okaytravel.models.BudgetElementModel
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.uuid
import com.example.okaytravel.views.PlacesMapView

@InjectViewState
class PlacesMapPresenter(private val context: Context): MvpPresenter<PlacesMapView>() {

    private val placeDBHelper = PlaceDatabaseHelper()
    private val usersDBHelper = UsersDatabaseHelper()

    private val usersApiHelper = UsersApiHelper()

    private val sessionSharedPref = SharedPrefHelper("session", context)

    private val currentUser = usersDBHelper.getUserByLogin(sessionSharedPref.getCurrentUser())

    private fun sync(onSuccess: () -> Unit = {}) {
        if (currentUser == null || currentUser.anonymous)
            return
        if (!isInternetAvailable(context)) {
            viewState.showMessage(R.string.noInternetConnection)
            return
        }
        usersApiHelper.sync(currentUser, {
            onSuccess()
        }, {
            viewState.showMessage(R.string.syncError)
        })
    }

    fun addPlace(trip: TripModel, name: String?, fullAddress: String?, latitude: String, longitude: String, date: String, budgetCategory: String?, rawBudgetAmount: String?) {
        if (currentUser == null || !validateInputData(date, budgetCategory, rawBudgetAmount)) {
            return
        }

        viewState.showLoadingDialog()
        var budgetElement: BudgetElementModel? = null
        if (budgetCategory != null && rawBudgetAmount != null) {
            val budgetAmount = rawBudgetAmount.toInt()
            budgetElement = BudgetElementModel(uuid(), budgetAmount, budgetCategory, trip)
        }

        if (currentUser.anonymous) {
            placeDBHelper.create(uuid(), name, fullAddress, latitude, longitude, date, trip)
            budgetElement?.run { save(); currentUser.updateTrigger() }
            currentUser.updateTrigger()
            viewState.dismissLoadingDialog()
            viewState.openPlaces()
            return
        }

        sync {
            placeDBHelper.create(uuid(), name, fullAddress, latitude, longitude, date, trip)
            budgetElement?.run { save(); currentUser.updateTrigger() }
            currentUser.updateTrigger()
            sync()
            viewState.dismissLoadingDialog()
            viewState.openPlaces()
        }
    }

    private fun validateInputData(date: String, budgetCategory: String?, rawBudgetAmount: String?): Boolean {
        if (date.isEmpty()) {
            viewState.showMessage(R.string.emptyFieldsError)
            return false
        }
        if (budgetCategory != null && rawBudgetAmount != null) {
            if (budgetCategory.isEmpty()) {
                viewState.showMessage(R.string.emptyFieldsError)
                viewState.focusBudgetCategoryInput()
                return false
            }
            if (budgetCategory.length > 30) {
                viewState.showMessage(R.string.tooLongCategory)
                viewState.focusBudgetCategoryInput()
                return false
            }
            if (rawBudgetAmount.isEmpty()) {
                viewState.showMessage(R.string.emptyFieldsError)
                viewState.focusBudgetAmountInput()
                return false
            }
            val budgetAmount: Int? = try { rawBudgetAmount.toInt() } catch ( e: NumberFormatException ) { null }
            if (budgetAmount == null || budgetAmount <= 0 || budgetAmount > 999999998) {
                viewState.showMessage(R.string.invalidBudgetAmount)
                return false
            }
        }
        return true
    }
}