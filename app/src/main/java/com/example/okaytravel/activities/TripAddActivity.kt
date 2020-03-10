package com.example.okaytravel.activities

import android.os.Bundle
import com.example.okaytravel.R
import com.example.okaytravel.activities.fragments.TripAddOwnPlaceFragment
import com.example.okaytravel.views.TripsAddOwnPlaceView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.search.*

class TripAddActivity : BaseActivity(), TripsAddOwnPlaceView {

    override val fragmentContainer: Int? = R.id.addTripFragmentContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.initialize(this)
        SearchFactory.initialize(this)
        setContentView(R.layout.activity_add_trip)

        title = "Новая поездка"

        loadFragment(TripAddOwnPlaceFragment())

        setToolbarBackButton()
    }

}