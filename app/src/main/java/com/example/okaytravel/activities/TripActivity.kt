package com.example.okaytravel.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.okaytravel.R
import com.example.okaytravel.database.TripDatabaseHelper
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.views.TripView
import kotlinx.android.synthetic.main.activity_trip.*
import kotlinx.android.synthetic.main.toolbar.*

class TripActivity : BaseActivity(), TripView {

    override val fragmentContainer: Int? = null

    private val tripsDBHelper = TripDatabaseHelper()
    lateinit var trip: TripModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)

        trip = getTripFromExtra()

        setToolbarBackButton()
        title = trip.ownPlace
    }

    private fun getTripFromExtra(): TripModel {
        val tripUuid =  intent.extras?.getString("trip")
        return tripsDBHelper.getTripByUuid(tripUuid!!)!!
    }
}
