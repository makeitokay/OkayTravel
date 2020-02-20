package com.example.okaytravel.activities.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.TripAddOwnPlaceActivity
import com.example.okaytravel.adapters.TripsRecyclerViewAdapter
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.parseDate
import com.example.okaytravel.presenters.TripsPresenter
import com.example.okaytravel.views.TripsView
import kotlinx.android.synthetic.main.fragment_trip_add_all_data.*
import kotlinx.android.synthetic.main.fragment_trips.*
import java.util.*

class TripsFragment: MvpAppCompatFragment(), TripsView {

    @ProvidePresenter
    fun provideTripsPresenter(): TripsPresenter {
        return TripsPresenter(this.requireActivity())
    }

    @InjectPresenter
    lateinit var tripsPresenter: TripsPresenter

    private val tripsData: MutableList<TripModel> = mutableListOf()
    private val tripsAdapter = TripsRecyclerViewAdapter(tripsData)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addTripFAB.setOnClickListener {
            openNewTrip()
        }

        tripsRecyclerView.adapter = tripsAdapter

        tripsPresenter.updateAll()
        tripsPresenter.sync()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        val transaction = this.requireActivity().supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        if (fragment != null) {
            transaction.replace(R.id.fragment_container, fragment).commit()
            return true
        }
        return false
    }

    override fun updateTrips(trips: List<TripModel>) {
        tripsData.clear()
        if (trips.isEmpty()) {
            noTripsView.visibility = View.VISIBLE
            return
        }
        noTripsView.visibility = View.GONE
        tripsData.addAll(trips)
        tripsAdapter.notifyDataSetChanged()
    }

    override fun openNewTrip() {
        startActivity(Intent(this.requireActivity(), TripAddOwnPlaceActivity::class.java))
    }

    override fun showMessage(resourceId: Int) {
        showMessage(getString(resourceId))
    }

    override fun showMessage(message: String) {
        Toast.makeText(this.requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}