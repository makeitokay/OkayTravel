package com.example.okaytravel.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.okaytravel.R
import com.example.okaytravel.adapters.thingitems.*
import com.example.okaytravel.database.ThingDatabaseHelper
import com.example.okaytravel.database.UsersDatabaseHelper
import com.example.okaytravel.helpers.SharedPrefHelper
import com.example.okaytravel.helpers.UsersApiHelper
import com.example.okaytravel.isInternetAvailable
import com.example.okaytravel.models.ThingModel
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.uuid
import com.example.okaytravel.views.ThingsView

@InjectViewState
class ThingsPresenter(private val context: Context, private val trip: TripModel) :
    MvpPresenter<ThingsView>() {

    private val usersDBHelper = UsersDatabaseHelper()
    private val thingsDBHelper = ThingDatabaseHelper()

    private val usersApiHelper = UsersApiHelper()

    private val sessionSharedPref = SharedPrefHelper("session", context)

    private val currentUser = usersDBHelper.getUserByLogin(sessionSharedPref.getCurrentUser())

    fun sync(onSuccess: () -> Unit = {}) {
        if (currentUser == null || currentUser.anonymous)
            return
        if (!isInternetAvailable(context)) {
            updateItems()
            viewState.showThings()
            viewState.showMessage(R.string.noInternetConnection)
            return
        }
        usersApiHelper.sync(currentUser, {
            onSuccess()
        }, {
            viewState.showMessage(R.string.syncError)
            viewState.showThings()
            updateItems()
        })
    }

    fun addThing(name: String) {
        if (currentUser == null || !validateInputData(name)) {
            return
        }
        viewState.dismissAddThingDialog()
        if (!isInternetAvailable(context)) {
            updateItems()
            viewState.showThings()
            viewState.showMessage(R.string.noInternetConnection)
            return
        }

        viewState.showLoadingDialog()
        if (currentUser.anonymous) {
            thingsDBHelper.create(uuid(), name, false, trip)
            currentUser.updateTrigger()
            viewState.dismissLoadingDialog()
            updateItems()
            return
        }
        viewState.showThingsLoading()
        sync {
            thingsDBHelper.create(uuid(), name, false, trip)
            currentUser.updateTrigger()
            sync()
            viewState.dismissLoadingDialog()
            updateItems()
        }
    }

    fun editThings(changeThingsList: Map<ThingModel, Boolean>) {
        if (currentUser == null) return

        viewState.showLoadingDialog()
        if (currentUser.anonymous) {
            changeThingsList.keys.forEach { it.save() }
            currentUser.updateTrigger()
            viewState.dismissLoadingDialog()
            updateItems()
            return
        }
        viewState.showThingsLoading()
        sync {
            changeThingsList.keys.forEach {
                it.taken = changeThingsList[it]
                it.save()
            }
            currentUser.updateTrigger()
            sync()
            viewState.dismissLoadingDialog()
            updateItems()
        }
    }

    private fun validateInputData(name: String): Boolean {
        if (name.isEmpty()) {
            viewState.showMessage(R.string.emptyFieldsError)
            return false
        }
        if (name.length > 50) {
            viewState.showMessage(R.string.invalidThingName)
            return false
        }
        return true
    }

    fun updateAll() {
        if (currentUser == null) return
        if (!currentUser.premium) {
            viewState.hideThingsContent()
            return
        }
        if (currentUser.anonymous) {
            updateItems()
            return
        }
        sync {
            updateItems()
        }
    }

    private fun updateItems() {
        currentUser?.let {
            val notTakenItems: MutableList<ThingListItem> = mutableListOf()
            val takenItems: MutableList<ThingListItem> = mutableListOf()
            val groupedThings = trip.things().groupBy { it.taken }

            notTakenItems.add(ThingAddItem())
            groupedThings[false]?.forEach {
                notTakenItems.add(ThingItem(it))
            }
            groupedThings[true]?.forEach {
                takenItems.add(ThingItem(it))
            }
            viewState.updateThingsItems(notTakenItems, takenItems)
            viewState.showThings()
        }
    }

    fun buyPremium() {
        if (currentUser == null) return
        if (currentUser.anonymous) {
            viewState.openSignUp()
            viewState.showMessage(R.string.needToSignUp)
            return
        }
        if (!isInternetAvailable(context)) {
            viewState.showMessage(R.string.noInternetConnection)
            return
        }
        viewState.showThingsLoading()
        sync {
            currentUser.buyPremium()
            sync()
            updateItems()
            viewState.showMessage(R.string.successPremiumBuy)
        }
    }

}