package com.example.okaytravel.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.fragments.TripAddAllDataFragment
import com.example.okaytravel.presenters.TripsAddOwnPlacePresenter
import com.example.okaytravel.views.TripsAddOwnPlaceView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import kotlinx.android.synthetic.main.activity_add_own_place.*
import kotlinx.android.synthetic.main.fragment_add_own_place.*

class TripAddOwnPlaceActivity: MvpAppCompatActivity(), TripsAddOwnPlaceView, SearchManager.SuggestListener {

    @ProvidePresenter
    fun provideTripsPresenter(): TripsAddOwnPlacePresenter {
        return TripsAddOwnPlacePresenter(this)
    }

    @InjectPresenter
    lateinit var tripsAddOwnPlacePresenter: TripsAddOwnPlacePresenter

        private val CENTER = Point(55.75, 37.62)
    private val BOX_SIZE = 0.2
    private val BOUNDING_BOX = BoundingBox(
        Point(CENTER.latitude - BOX_SIZE, CENTER.longitude - BOX_SIZE),
        Point(CENTER.latitude + BOX_SIZE, CENTER.longitude + BOX_SIZE))
    private val SEARCH_OPTIONS = SearchOptions().setSearchTypes(
        SearchType.GEO.value or SearchType.TRANSIT.value or SearchType.BIZ.value
    )

    lateinit var searchManager: SearchManager
    lateinit var suggestResult: MutableList<String>
    lateinit var suggestItems: MutableList<SuggestItem>
    lateinit var suggestAdapter: ArrayAdapter<String>

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.initialize(this)
        SearchFactory.initialize(this)

        setContentView(R.layout.activity_add_own_place)
        title = "Новая поездка"

        val currentFragment = layoutInflater.inflate(R.layout.fragment_add_own_place, null)
        addTripFragmentContainer.addView(currentFragment)

        searchManager = SearchFactory.getInstance().createSearchManager(
            SearchManagerType.COMBINED)
        suggestResult = ArrayList()
        suggestItems = ArrayList()
        suggestAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_2,
            android.R.id.text1,
            suggestResult)
        suggestResultView.adapter = suggestAdapter

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back_white_24dp)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        ownPlace.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                requestSuggest(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        suggestResultView.setOnItemClickListener { parent, view, position, id ->
            loadFragment(TripAddAllDataFragment(suggestItems[position].title.text))
        }
    }

    override fun onSuggestError(error: Error) {
        var errorMessage = "Unknown error"
        if (error is RemoteError) {
            errorMessage = "Remote error"
        } else if (error is NetworkError) {
            errorMessage = "Network error"
        }

        showMessage(errorMessage)
    }

    override fun onSuggestResponse(suggest: MutableList<SuggestItem>) {
        suggestResult.clear()
        suggestItems.clear()
        for (i in 0 until 10.coerceAtMost(suggest.size)) {
            if (suggest[i].tags[0] == "locality") {
                val displayText = suggest[i].displayText
                if (displayText != null) suggestResult.add(displayText)
                else suggestResult.add(suggest[i].title.text)
                suggestItems.add(suggest[i])
            }
        }
        suggestAdapter.notifyDataSetChanged()
        suggestResultView.visibility = View.VISIBLE
    }

    private fun requestSuggest(query: String) {
        suggestResultView.visibility = View.INVISIBLE
        searchManager.suggest(query, BOUNDING_BOX, SEARCH_OPTIONS, this)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        addTripFragmentContainer.removeAllViews()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        if (fragment != null) {
            transaction.replace(R.id.addTripFragmentContainer, fragment).commit()
            return true
        }
        return false
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(resourceId: Int) {
        showMessage(getString(resourceId))
    }

}