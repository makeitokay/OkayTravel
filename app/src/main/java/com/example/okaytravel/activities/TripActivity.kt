package com.example.okaytravel.activities

import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.okaytravel.R
import com.example.okaytravel.activities.fragments.BaseFragment
import com.example.okaytravel.activities.fragments.BudgetFragment
import com.example.okaytravel.activities.fragments.PlacesFragment
import com.example.okaytravel.activities.fragments.ThingsFragment
import com.example.okaytravel.adapters.TripFragmentViewPagerAdapter
import com.example.okaytravel.database.TripDatabaseHelper
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.views.TripView
import kotlinx.android.synthetic.main.activity_trip.*

class TripActivity : BaseActivity(), TripView, ViewPager.OnPageChangeListener {

    override val fragmentContainer: Int? = null

    private val tripsDBHelper = TripDatabaseHelper()
    lateinit var trip: TripModel

    private lateinit var fragmentViewPagerAdapter: TripFragmentViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)

        val tripFromExtra = getTripFromExtra()
        if (tripFromExtra == null) {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            return
        }
        trip = tripFromExtra

        setToolbarBackButton()
        title = trip.ownPlace

        fragmentViewPagerAdapter = TripFragmentViewPagerAdapter(supportFragmentManager)
        fragmentViewPagerAdapter.addFragment(PlacesFragment(), getString(R.string.places))
        fragmentViewPagerAdapter.addFragment(BudgetFragment(), getString(R.string.budget))
        fragmentViewPagerAdapter.addFragment(ThingsFragment(), getString(R.string.things))
        tripViewPager.adapter = fragmentViewPagerAdapter
        tripTabs.setupWithViewPager(tripViewPager)
        tripViewPager.addOnPageChangeListener(this)

        tripViewPager.post {
            onPageSelected(0)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {}

    override fun onPageSelected(position: Int) {
        for (i in 0..2) {
            fragmentViewPagerAdapter.getItem(i).onDetach()
        }

        val fragment = fragmentViewPagerAdapter.getItem(position)
        when (position) {
            0 -> (fragment as PlacesFragment).update()
            1 -> (fragment as BudgetFragment).update()
            else -> (fragment as ThingsFragment).update()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun getTripFromExtra(): TripModel? {
        val tripUuid =  intent.extras?.getString("trip") ?: return null
        return tripsDBHelper.getTripByUuid(tripUuid)
    }
}
