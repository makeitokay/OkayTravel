package com.example.okaytravel.database

import com.activeandroid.query.Select
import com.example.okaytravel.models.BudgetElementModel
import com.example.okaytravel.models.TripModel

class BudgetElementDatabaseHelper {

    fun getBudgetElementById(id: Long): BudgetElementModel? {
        return Select()
            .from(BudgetElementModel::class.java)
            .where("id = ?", id)
            .executeSingle()
    }

    fun create(amount: Int, category: String, trip: TripModel): BudgetElementModel {
        var budgetElement = BudgetElementModel(amount, category, trip)
        budgetElement.save()
        return budgetElement
    }

}