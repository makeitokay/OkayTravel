package com.example.okaytravel.models

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

@Table(name = "Things")
class ThingModel: Model {

    @Column(name = "uuid", unique = true)
    var uuid: String? = null

    @Column(name = "name")
    var name: String? = null

    @Column(name = "taken")
    var taken: Boolean? = null

    @Column(name = "trip", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    var trip: TripModel? = null

    constructor(uuid: String, name: String, taken: Boolean, trip: TripModel) {
        this.uuid = uuid
        this.name = name
        this.taken = taken
        this.trip = trip
    }

    constructor()
}