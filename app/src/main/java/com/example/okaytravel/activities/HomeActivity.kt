package com.example.okaytravel.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.fragments.BaseFragment
import com.example.okaytravel.activities.fragments.ProfileFragment
import com.example.okaytravel.activities.fragments.TripsFragment
import com.example.okaytravel.activities.fragments.TripsMapFragment
import com.example.okaytravel.presenters.HomePresenter
import com.example.okaytravel.views.HomeView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.MapKitFactory
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*

class HomeActivity : BaseActivity(), HomeView, BottomNavigationView.OnNavigationItemSelectedListener {

    @ProvidePresenter
    fun provideHomePresenter(): HomePresenter {
        return HomePresenter(this)
    }

    @InjectPresenter
    lateinit var homePresenter: HomePresenter

    override val fragmentContainer = R.id.fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        MapKitFactory.setApiKey(getString(R.string.mapkitAccessToken))
        MapKitFactory.initialize(this)

        homePresenter.sync()

        loadFragment(TripsFragment() as BaseFragment)

        bottom_navigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == bottom_navigation.selectedItemId) return false
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.trips -> { fragment = TripsFragment() }
            R.id.tripsMap -> { fragment = TripsMapFragment() }
            R.id.profile -> { fragment = ProfileFragment() }
        }
        return loadFragment(fragment as BaseFragment)
    }

}
