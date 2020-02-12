package com.example.okaytravel.models

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import java.util.*

@Table(name = "Places")
class PlaceModel: Model {

    @Column(name = "name")
    var name: String? = null

    @Column(name = "date")
    var date: Date? = null

    @Column(name = "trip", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    var trip: TripModel? = null

    constructor(name: String, date: Date, trip: TripModel) {
        this.name = name
        this.date = date
        this.trip = trip
    }

    constructor()
}