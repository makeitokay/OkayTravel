package com.example.okaytravel.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.fragments.ProfileFragment
import com.example.okaytravel.activities.fragments.TripsFragment
import com.example.okaytravel.activities.fragments.TripsMapFragment
import com.example.okaytravel.presenters.HomePresenter
import com.example.okaytravel.views.HomeView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : MvpAppCompatActivity(), HomeView, BottomNavigationView.OnNavigationItemSelectedListener {

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

        loadFragment(TripsFragment())

        bottom_navigation.setOnNavigationItemSelectedListener(this)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            return true
        }
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.trips -> { fragment = TripsFragment() }
            R.id.tripsMap -> { fragment = TripsMapFragment() }
            R.id.profile -> { fragment = ProfileFragment() }
        }
        return loadFragment(fragment)
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(resourceId: Int) {
        showMessage(getString(resourceId))
    }
}
