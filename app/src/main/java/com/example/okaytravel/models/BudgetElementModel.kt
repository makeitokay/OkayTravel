package com.example.okaytravel.models

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

@Table(name = "Budget")
class BudgetElementModel: Model {

    @Column(name = "amount")
    var amount: Int? = null

    @Column(name = "category")
    var category: String? = null

    @Column(name = "trip", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    var trip: TripModel? = null

    constructor(amount: Int, category: String, trip: TripModel) {
        this.amount = amount
        this.category = category
        this.trip = trip
    }

    constructor()
}