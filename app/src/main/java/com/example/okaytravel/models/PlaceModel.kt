package com.example.okaytravel.models

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import java.util.*

@Table(name = "Places")
class PlaceModel: Model {

    @Column(name = "uuid", unique = true)
    var uuid: String? = null

    @Column(name = "name")
    var name: String? = null

    @Column(name = "date")
    var date: String? = null

    @Column(name = "trip", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    var trip: TripModel? = null

    constructor(uuid: String, name: String, date: String, trip: TripModel) {
        this.uuid = uuid
        this.name = name
        this.date = date
        this.trip = trip
    }

    constructor()
}