package com.example.okaytravel.database

import com.activeandroid.query.Select
import com.example.okaytravel.api.models.okaytravelserver.*
import com.example.okaytravel.api.responses.okaytravelserver.SyncResponse
import com.example.okaytravel.models.UserModel
import java.util.*

class UsersDatabaseHelper {

    private val tripDBHelper = TripDatabaseHelper()
    private val budgetElementDBHelper = BudgetElementDatabaseHelper()
    private val placeDBHelper = PlaceDatabaseHelper()
    private val thingDBHelper = ThingDatabaseHelper()

    fun createUser(username: String, email: String, passwordHash: String, accessToken: String): UserModel {
        var user = UserModel(username, email, passwordHash, null, accessToken)
        user.save()
        return user
    }

    fun createAnonymousUser(): UserModel {
        val user = UserModel("Anonymous", null, null, null, null, true)
        user.save()
        return user
    }

    fun replaceAnonymousWithNewUser(username: String, email: String, passwordHash: String, accessToken: String) {
        var anonymousUser = getUserByUsername("Anonymous") ?: return
        anonymousUser.username = username
        anonymousUser.email = email
        anonymousUser.passwordHash = passwordHash
        anonymousUser.accessToken = accessToken
        anonymousUser.anonymous = false
        anonymousUser.save()
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
            val userInfoBody = UserInfo(user.username, user.email, user.passwordHash, user.avatar, user.commits)

            var trips: ArrayList<Trip> = arrayListOf()
            user.trips().forEach { trip ->
                val tripInfoBody = TripInfo(trip.uuid, trip.ownPlace, trip.fullAddress, trip.startDate, trip.duration)
                val budget: ArrayList<BudgetElement> = arrayListOf()
                val places: ArrayList<Place> = arrayListOf()
                val things: ArrayList<Thing> = arrayListOf()
                trip.budget().forEach { budgetElement ->
                    val budgetElementBody = BudgetElement(budgetElement.uuid, budgetElement.amount, budgetElement.category)
                    budget.add(budgetElementBody)
                }
                trip.places().forEach { place ->
                    val placeInfoBody = Place(place.uuid, place.name, place.fullAddress, place.latitude, place.longitude, place.date)
                    places.add(placeInfoBody)
                }
                trip.things().forEach { thing ->
                    val thingBody = Thing(thing.uuid, thing.name, thing.taken)
                    things.add(thingBody)
                }
                trips.add(Trip(tripInfoBody, budget, places, things))
            }
            return SyncBody(User(userInfoBody, trips), user.accessToken!!)
        }
        return null
    }

    fun updateUser(syncResponse: SyncResponse): Boolean {
        val syncUser = syncResponse.user ?: return false
        val userInfo = syncUser.user
        val trips = syncUser.trips
        var user = getUserByUsername(userInfo.username)

        if (user != null) {
            user.username = userInfo.username
            user.email = userInfo.email
            user.passwordHash = userInfo.passwordHash
            user.avatar = userInfo.avatar
            user.commits = userInfo.commits
            user.save()
        } else {
            user = createUser(userInfo.username, userInfo.email, userInfo.passwordHash, userInfo.accessToken)
        }

        trips.forEach { trip ->
            val tripInfo = trip.trip
            var tripModel = tripDBHelper.getTripByUuid(tripInfo.uuid)

            if (tripModel != null) {
                tripModel.ownPlace = tripInfo.ownPlace
                tripModel.fullAddress = tripInfo.fullAddress
                tripModel.startDate = tripInfo.startDate
                tripModel.duration = tripInfo.duration

                trip.budget.forEach { budgetElement ->
                    var budgetElementModel =
                        budgetElementDBHelper.getBudgetElementByUuid(budgetElement.uuid)

                    if (budgetElementModel != null) {
                        budgetElementModel.amount = budgetElement.amount
                        budgetElementModel.category = budgetElement.category
                        budgetElementModel.save()
                    } else {
                        budgetElementDBHelper.create(
                            budgetElement.uuid,
                            budgetElement.amount,
                            budgetElement.category,
                            tripModel
                        )
                    }
                }

                trip.places.forEach { place ->
                    var placeModel = placeDBHelper.getPlaceByUuid(place.uuid)

                    if (placeModel != null) {
                        placeModel.name = place.name
                        placeModel.fullAddress = place.fullAddress
                        placeModel.latitude = place.latitude
                        placeModel.longitude = place.longitude
                        placeModel.date = place.date
                        placeModel.save()
                    } else {
                        placeDBHelper.create(place.uuid, place.name, place.fullAddress, place.latitude, place.longitude, place.date, tripModel)
                    }
                }

                trip.things.forEach { thing ->
                    var thingModel = thingDBHelper.getThingByUuid(thing.uuid)

                    if (thingModel != null) {
                        thingModel.name = thing.name
                        thingModel.taken = thing.taken
                    } else {
                        thingDBHelper.create(thing.uuid, thing.name, thing.taken, tripModel)
                    }
                }
                tripModel.save()

                } else {

                val newTripModel = tripDBHelper.create(
                    tripInfo.uuid, tripInfo.ownPlace, tripInfo.fullAddress, tripInfo.startDate, tripInfo.duration, user)

                trip.budget.forEach { budgetElement ->
                    budgetElementDBHelper.create(
                        budgetElement.uuid,
                        budgetElement.amount,
                        budgetElement.category,
                        newTripModel
                    )
                }
                trip.places.forEach { place ->
                    placeDBHelper.create(place.uuid, place.name, place.fullAddress, place.latitude, place.longitude, place.date, newTripModel)
                }
                trip.things.forEach { thing ->
                    thingDBHelper.create(thing.uuid, thing.name, thing.taken, newTripModel)
                }
            }
        }
        return true
    }
}