package com.example.okaytravel.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.PlacesMapActivity
import com.example.okaytravel.activities.TripActivity
import com.example.okaytravel.adapters.PlacesRecyclerViewAdapter
import com.example.okaytravel.adapters.placeitems.PlaceListItem
import com.example.okaytravel.models.PlaceModel
import com.example.okaytravel.presenters.PlacesPresenter
import com.example.okaytravel.views.PlacesView
import kotlinx.android.synthetic.main.fragment_places.*

class PlacesFragment: BaseFragment(false), PlacesView {

    override val fragmentNameResource: Int
        get() = R.string.places

    @ProvidePresenter
    fun providePlacesPresenter(): PlacesPresenter {
        return PlacesPresenter(this.requireActivity(), (activity as TripActivity).trip)
    }

    @InjectPresenter
    lateinit var placesPresenter: PlacesPresenter

    private val placesData: MutableList<PlaceListItem> = mutableListOf()
    private lateinit var placesAdapter: PlacesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_places, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addPlaceBtn.setOnClickListener {
            openPlacesMap()
        }

        placesAdapter = PlacesRecyclerViewAdapter(placesData)
        placesRecyclerView.adapter = placesAdapter
    }

    override fun openPlacesMap() {
        val trip = (activity as TripActivity).trip
        val intent = Intent(this.requireActivity(), PlacesMapActivity::class.java)
        intent.putExtra("tripUuid", trip.uuid)
        startActivity(intent)
    }

    override fun updatePlaces(places: MutableList<PlaceListItem>) {
        placesData.clear()
        placesData.addAll(places)
        placesAdapter.notifyDataSetChanged()
    }

    override fun update() {
        placesPresenter.updateAll()
        placesPresenter.sync()
    }

}