package com.example.okaytravel.database

import com.activeandroid.query.Select
import com.example.okaytravel.models.PlaceModel
import com.example.okaytravel.models.TripModel
import java.util.*

class PlaceDatabaseHelper {

    fun getPlaceById(id: Long): PlaceModel? {
        return Select()
            .from(PlaceModel::class.java)
            .where("id = ?", id)
            .executeSingle()
    }

    fun create(name: String, date: String, trip: TripModel): PlaceModel {
        var place = PlaceModel(name, date, trip)
        place.save()
        return place
    }

}