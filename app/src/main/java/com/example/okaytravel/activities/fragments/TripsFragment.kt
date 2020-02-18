package com.example.okaytravel.activities.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.adapters.TripsRecyclerViewAdapter
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.parseDate
import com.example.okaytravel.presenters.TripsPresenter
import com.example.okaytravel.views.TripsView
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
        addTripBtn.setOnClickListener {
            tripsPresenter.addTrip(ownPlace.text.toString(), duration.text.toString(), dateView.text.toString())
        }

        dateView.setOnClickListener {
            openDatePickerDialog()
        }

        tripsRecyclerView.adapter = tripsAdapter

        tripsPresenter.updateAll()
        tripsPresenter.sync()

        super.onViewCreated(view, savedInstanceState)
    }

    private val onDateSetListener = DatePickerDialog.OnDateSetListener { dp, year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        dateView.setText(parseDate(calendar.time))
    }

    private fun openDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this.requireActivity(), onDateSetListener, year, month, day).show()
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

    override fun showMessage(resourceId: Int) {
        showMessage(getString(resourceId))
    }

    override fun showMessage(message: String) {
        Toast.makeText(this.requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}