package com.example.okaytravel.activities.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.TripAddActivity
import com.example.okaytravel.presenters.TripsAddOwnPlacePresenter
import com.example.okaytravel.views.TripsAddOwnPlaceView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import kotlinx.android.synthetic.main.fragment_add_own_place.*

class TripAddOwnPlaceFragment: BaseFragment(), TripsAddOwnPlaceView, SearchManager.SuggestListener {

    override val fragmentNameResource: Int
        get() = R.string.newTrip

    @ProvidePresenter
    fun provideTripsPresenter(): TripsAddOwnPlacePresenter {
        return TripsAddOwnPlacePresenter(this.requireActivity())
    }

    @InjectPresenter
    lateinit var tripsAddOwnPlacePresenter: TripsAddOwnPlacePresenter

    private val center = Point(55.75, 37.62)
    private val boxSize = 0.2
    private val boundingBox = BoundingBox(
        Point(center.latitude - boxSize, center.longitude - boxSize),
        Point(center.latitude + boxSize, center.longitude + boxSize)
    )
    private val searchOptions = SearchOptions().setSearchTypes(
        SearchType.GEO.value or SearchType.TRANSIT.value or SearchType.BIZ.value
    )

    private lateinit var searchManager: SearchManager
    private lateinit var suggestResult: MutableList<String>
    private lateinit var suggestItems: MutableList<SuggestItem>
    private lateinit var suggestAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_own_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchManager = SearchFactory.getInstance().createSearchManager(
            SearchManagerType.COMBINED)
        suggestResult = ArrayList()
        suggestItems = ArrayList()
        suggestAdapter = ArrayAdapter(this.requireActivity(),
            android.R.layout.simple_list_item_2,
            android.R.id.text1,
            suggestResult)
        suggestResultView.adapter = suggestAdapter

        ownPlace.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                requestSuggest(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        suggestResultView.setOnItemClickListener { _, _, position, _ ->
            (activity as TripAddActivity)
                .loadFragment(TripAddAllDataFragment(
                    suggestItems[position].title.text,
                    suggestItems[position].searchText))
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
        searchManager.suggest(query, boundingBox, searchOptions, this)
    }


    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
    }

}