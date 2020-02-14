package com.example.okaytravel.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.parseDate
import com.example.okaytravel.presenters.HomePresenter
import com.example.okaytravel.views.HomeView
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HomeActivity : MvpAppCompatActivity(), HomeView {

    @ProvidePresenter
    fun provideHomePresenter(): HomePresenter {
        return HomePresenter(this)
    }

    @InjectPresenter
    lateinit var homePresenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homePresenter.sync()

        addTripBtn.setOnClickListener {
            homePresenter.addTrip(ownPlace.text.toString(), duration.text.toString(), dateView.text.toString())
        }

        dateView.setOnClickListener {
            openDatePickerDialog()
        }

        // TODO: выход из аккаунта
    }

    private val onDateSetListener = DatePickerDialog.OnDateSetListener { dp, year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        dateView.setText(parseDate(calendar.time))
    }

    fun openDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, onDateSetListener, year, month, day).show()
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(resourceId: Int) {
        showMessage(getString(resourceId))
    }

}
