package com.example.okaytravel.database

import com.activeandroid.query.Select
import com.example.okaytravel.api.models.okaytravelserver.*
import com.example.okaytravel.api.responses.okaytravelserver.SyncResponse
import com.example.okaytravel.getCurrentDate
import com.example.okaytravel.models.UserModel
import java.util.*

class UsersDatabaseHelper {

    private val tripDBHelper = TripDatabaseHelper()
    private val budgetElementDBHelper = BudgetElementDatabaseHelper()
    private val placeDBHelper = PlaceDatabaseHelper()

    fun createUser(username: String, email: String, passwordHash: String, accessToken: String): UserModel {
        var date = getCurrentDate()
        var user = UserModel(username, email, passwordHash, null, accessToken, date)
        user.save()
        return user
    }

    fun getUserByLogin(login: String): UserModel? {
        getUserByUsername(login)?.let {
            return it
        }
        return getUserByEmail(login)
    }

    fun getUserByUsername(username: String): UserModel? {
        return Select()
            .from(UserModel::class.java)
            .where("username = ?", username)
            .executeSingle()
    }

    fun getUserByEmail(email: String): UserModel? {
        return Select()
            .from(UserModel::class.java)
            .where("email = ?", email)
            .executeSingle()
    }

    fun getUserById(id: Long): UserModel? {
        return Select()
            .from(UserModel::class.java)
            .where("id = ?", id)
            .executeSingle()
    }

    fun serializeUser(id: Long): SyncBody? {
        val user = getUserById(id)
        user?.let {
            val userInfoBody = UserInfo(user.username, user.email, user.passwordHash, user.avatar, user.lastUpdateDatetime)

            var trips: ArrayList<Trip> = arrayListOf()
            user.trips().forEach { trip ->
                val tripInfoBody = TripInfo(trip.id, trip.ownPlace, trip.startDate, trip.duration)
                val budget: ArrayList<BudgetElement> = arrayListOf()
                val places: ArrayList<Place> = arrayListOf()
                trip.budget().forEach { budgetElement ->
                    val budgetElementBody = BudgetElement(budgetElement.id, budgetElement.amount, budgetElement.category)
                    budget.add(budgetElementBody)
                }
                trip.places().forEach { place ->
                    val placeInfoBody = Place(place.id, place.name, place.date)
                    places.add(placeInfoBody)
                }
                trips.add(Trip(tripInfoBody, budget, places))
            }
            return SyncBody(User(userInfoBody, trips), user.accessToken!!)
        }
        return null
    }

    fun updateUser(syncResponse: SyncResponse): Boolean {
        val syncUser = syncResponse.user ?: return false
        val userInfo = syncUser.user ?: return false
        val trips = syncUser.trips ?: return false
        var user = getUserByUsername(userInfo.username!!)

        if (user != null) {
            user.username = userInfo.username
            user.email = userInfo.email
            user.passwordHash = userInfo.passwordHash
            user.avatar = userInfo.avatar
            user.lastUpdateDatetime = getCurrentDate()
            user.save()
        } else {
            user = createUser(userInfo.username, userInfo.email!!, userInfo.passwordHash!!, userInfo.accessToken!!)
        }

        trips.forEach { trip ->
            val tripInfo = trip.trip ?: return false
            var tripModel = tripDBHelper.getTripById(tripInfo.remoteId)

            if (tripModel != null) {
                tripModel.ownPlace = tripInfo.ownPlace
                tripModel.startDate = tripInfo.startDate
                tripModel.duration = tripInfo.duration

                trip.budget?.forEach { budgetElement ->
                    var budgetElementModel =
                        budgetElementDBHelper.getBudgetElementById(budgetElement.remoteId)

                    if (budgetElementModel != null) {
                        budgetElementModel.amount = budgetElement.amount
                        budgetElementModel.category = budgetElement.category
                        budgetElementModel.save()
                    } else {
                        budgetElementDBHelper.create(
                            budgetElement.amount!!,
                            budgetElement.category!!,
                            tripModel
                        )
                    }
                }

                trip.places?.forEach { place ->
                    var placeModel = placeDBHelper.getPlaceById(place.remoteId)

                    if (placeModel != null) {
                        placeModel.name = place.name
                        placeModel.date = place.date
                        placeModel.save()
                    } else {
                        placeDBHelper.create(place.name!!, place.date!!, tripModel)
                    }
                }
                tripModel.save()

                } else {

                var newTripModel = tripDBHelper.create(tripInfo.ownPlace!!, tripInfo.startDate!!, tripInfo.duration!!, user)

                trip.budget?.forEach { budgetElement ->
                    budgetElementDBHelper.create(
                        budgetElement.amount!!,
                        budgetElement.category!!,
                        newTripModel
                    )
                }

                trip.places?.forEach { place ->
                    placeDBHelper.create(place.name!!, place.date!!, newTripModel)
                }
            }
        }
        return true
    }
}