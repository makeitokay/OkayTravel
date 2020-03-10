package com.example.okaytravel.database

import com.activeandroid.query.Select
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.models.UserModel

class TripDatabaseHelper {

    fun create(
        uuid: String,
        ownPlace: String,
        fullAddress: String,
        startDate: String,
        duration: Int,
        user: UserModel
    ): TripModel {
        val trip = TripModel(uuid, user, ownPlace, fullAddress, startDate, duration)
        trip.save()
        return trip
    }

    fun getTripByUuid(uuid: String): TripModel? {
        return Select()
            .from(TripModel::class.java)
            .where("uuid = ?", uuid)
            .executeSingle()
    }

}