package com.example.okaytravel.database

import com.activeandroid.query.Select
import com.example.okaytravel.models.ThingModel
import com.example.okaytravel.models.TripModel

class ThingDatabaseHelper {

    fun getThingByUuid(uuid: String): ThingModel? {
        return Select()
            .from(ThingModel::class.java)
            .where("uuid = ?", uuid)
            .executeSingle()
    }

    fun create(uuid: String, name: String, taken: Boolean, trip: TripModel): ThingModel {
        var thing = ThingModel(uuid, name, taken, trip)
        thing.save()
        return thing
    }

}