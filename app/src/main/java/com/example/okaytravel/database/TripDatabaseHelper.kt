package com.example.okaytravel.database

import com.activeandroid.query.Select
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.models.UserModel
import java.util.*

class TripDatabaseHelper {

    fun create(ownPlace: String, startDate: String, duration: Int, user: UserModel): TripModel {
        val trip = TripModel(user, ownPlace, startDate, duration)
        trip.save()
        user.updateTrigger()
        return trip
    }

    fun getTripById(id: Long): TripModel? {
        return Select()
            .from(TripModel::class.java)
            .where("id = ?", id)
            .executeSingle()
    }

}