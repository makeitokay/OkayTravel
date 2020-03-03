package com.example.okaytravel.activities.fragments

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.TripAddActivity
import com.example.okaytravel.adapters.TripsRecyclerViewAdapter
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.presenters.TripsPresenter
import com.example.okaytravel.views.TripsView
import kotlinx.android.synthetic.main.fragment_trips.*

class TripsFragment: BaseFragment(), TripsView {

    override val fragmentNameResource: Int
        get() = R.string.tripsMenuItemText

    @ProvidePresenter
    fun provideTripsPresenter(): TripsPresenter {
        return TripsPresenter(this.requireActivity())
    }

    @InjectPresenter
    lateinit var tripsPresenter: TripsPresenter

    private val tripsData: MutableList<TripModel> = mutableListOf()
    lateinit var tripsAdapter: TripsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addTripFAB.setOnClickListener {
            openNewTrip()
        }

        tripsAdapter = TripsRecyclerViewAdapter(tripsData, this.requireActivity())
        tripsRecyclerView.adapter = tripsAdapter

        update()
    }

    override fun updateTrips(trips: List<TripModel>) {
        tripsData.clear()
        if (trips.isEmpty()) {
            noTripsView.visibility = View.VISIBLE
            tripsRecyclerView.visibility = View.GONE
            return
        }
        tripsRecyclerView.visibility = View.VISIBLE
        noTripsView.visibility = View.GONE
        tripsData.addAll(trips)
        tripsAdapter.notifyDataSetChanged()
    }

    override fun openNewTrip() {
        startActivity(Intent(this.requireActivity(), TripAddActivity::class.java))
    }

    override fun update() {
        tripsPresenter.updateAll()
        tripsPresenter.sync()
    }

}