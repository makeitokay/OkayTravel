package com.example.okaytravel.models

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import java.util.*

@Table(name = "Budget")
class BudgetElementModel : Model {

    @Column(name = "uuid", unique = true)
    var uuid: String? = null

    @Column(name = "amount")
    var amount: Int? = null

    @Column(name = "category")
    var category: String? = null

    @Column(
        name = "trip",
        onUpdate = Column.ForeignKeyAction.CASCADE,
        onDelete = Column.ForeignKeyAction.CASCADE
    )
    var trip: TripModel? = null

    constructor(uuid: String, amount: Int, category: String, trip: TripModel) {
        this.uuid = uuid
        this.amount = amount
        this.category = category
        this.trip = trip
    }

    constructor()
}