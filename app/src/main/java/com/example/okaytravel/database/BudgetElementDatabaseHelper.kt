package com.example.okaytravel.database

import com.activeandroid.query.Select
import com.example.okaytravel.models.BudgetElementModel
import com.example.okaytravel.models.TripModel

class BudgetElementDatabaseHelper {

    fun getBudgetElementByUuid(uuid: String): BudgetElementModel? {
        return Select()
            .from(BudgetElementModel::class.java)
            .where("uuid = ?", uuid)
            .executeSingle()
    }

    fun create(uuid: String, amount: Int, category: String, trip: TripModel): BudgetElementModel {
        var budgetElement = BudgetElementModel(uuid, amount, category, trip)
        budgetElement.save()
        return budgetElement
    }

}