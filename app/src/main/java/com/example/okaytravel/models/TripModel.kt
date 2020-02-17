package com.example.okaytravel.models

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import java.util.*

@Table(name = "Trips")
class TripModel: Model {

    @Column(name = "uuid", unique = true)
    var uuid: String? = null

    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    var user: UserModel? = null

    @Column(name = "ownPlace")
    var ownPlace: String? = null

    @Column(name = "startDate")
    var startDate: String? = null

    @Column(name = "duration")
    var duration: Int? = null

    fun budget(): List<BudgetElementModel> {
        return getMany(BudgetElementModel::class.java, "trip")
    }

    fun places(): List<PlaceModel> {
        return getMany(PlaceModel::class.java, "trip")
    }

    constructor(uuid: String, user: UserModel, ownPlace: String, startDate: String, duration: Int) {
        this.uuid = uuid
        this.user = user
        this.ownPlace = ownPlace
        this.startDate = startDate
        this.duration = duration
    }

    constructor()

}