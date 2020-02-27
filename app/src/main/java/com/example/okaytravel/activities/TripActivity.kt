package com.example.okaytravel.activities

import android.content.Intent
import android.os.Bundle
import com.example.okaytravel.R
import com.example.okaytravel.activities.fragments.BudgetFragment
import com.example.okaytravel.activities.fragments.PlacesFragment
import com.example.okaytravel.activities.fragments.ThingsFragment
import com.example.okaytravel.adapters.TripFragmentViewPagerAdapter
import com.example.okaytravel.database.TripDatabaseHelper
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.views.TripView
import kotlinx.android.synthetic.main.activity_trip.*

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

        val fragmentViewPagerAdapter = TripFragmentViewPagerAdapter(supportFragmentManager)
        fragmentViewPagerAdapter.addFragment(PlacesFragment(), getString(R.string.places))
        fragmentViewPagerAdapter.addFragment(BudgetFragment(), getString(R.string.budget))
        fragmentViewPagerAdapter.addFragment(ThingsFragment(), getString(R.string.things))
        tripViewPager.adapter = fragmentViewPagerAdapter
        tripTabs.setupWithViewPager(tripViewPager)
    }

    override fun onBackPressed() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
        finish()
    }

    private fun getTripFromExtra(): TripModel {
        val tripUuid =  intent.extras?.getString("trip")
        return tripsDBHelper.getTripByUuid(tripUuid!!)!!
    }
}
