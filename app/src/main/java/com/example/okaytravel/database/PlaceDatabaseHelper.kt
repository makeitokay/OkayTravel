package com.example.okaytravel.database

import com.activeandroid.query.Select
import com.example.okaytravel.models.PlaceModel
import com.example.okaytravel.models.TripModel
import java.util.*

class PlaceDatabaseHelper {

    fun getPlaceByUuid(uuid: String): PlaceModel? {
        return Select()
            .from(PlaceModel::class.java)
            .where("uuid = ?", uuid)
            .executeSingle()
    }

    fun create(uuid: String, name: String, date: String, trip: TripModel): PlaceModel {
        var place = PlaceModel(uuid, name, date, trip)
        place.save()
        return place
    }

}