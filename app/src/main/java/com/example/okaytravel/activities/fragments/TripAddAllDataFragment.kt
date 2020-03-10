package com.example.okaytravel.activities.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.HomeActivity
import com.example.okaytravel.getHumanReadableDate
import com.example.okaytravel.hideKeyboard
import com.example.okaytravel.parseDate
import com.example.okaytravel.presenters.TripAddAllDataPresenter
import com.example.okaytravel.views.TripAddAllDataView
import kotlinx.android.synthetic.main.fragment_trip_add_all_data.*
import java.util.*

class TripAddAllDataFragment(private val ownPlace: String, private val fullAddress: String) : BaseFragment(), TripAddAllDataView {

    override val fragmentNameResource: Int
        get() = R.string.newTrip

    @ProvidePresenter
    fun provideTripAddAllDataPresenter(): TripAddAllDataPresenter {
        return TripAddAllDataPresenter(this.requireActivity())
    }

    @InjectPresenter
    lateinit var tripAddAllDataPresenter: TripAddAllDataPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trip_add_all_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ownPlaceView.text = getString(R.string.tripAddAllDataOwnPlace, ownPlace)

        startDateView.setOnClickListener {
            hideKeyboard(this.requireActivity())
            openDatePickerDialog()
        }

        addTripBtn.setOnClickListener {
            tripAddAllDataPresenter.addTrip(ownPlace, fullAddress, duration.text.toString(), startDateView.text.toString())
        }
    }

    override fun showAddTripButton() {
        loading.visibility = View.GONE
        addTripBtn.visibility = View.VISIBLE
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
        addTripBtn.visibility = View.GONE
    }

    private val onDateSetListener = DatePickerDialog.OnDateSetListener { dp, year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        startDateView.setText(parseDate(calendar.time))
    }

    private fun openDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this.requireActivity(), onDateSetListener, year, month, day)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    override fun openTrips() {
        startActivity(Intent(this.requireActivity(), HomeActivity::class.java))
    }

    override fun update() {}

}