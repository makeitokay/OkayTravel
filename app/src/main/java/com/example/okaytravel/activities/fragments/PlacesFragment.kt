package com.example.okaytravel.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.PlacesMapActivity
import com.example.okaytravel.activities.TripActivity
import com.example.okaytravel.adapters.PlaceDatesRecyclerViewAdapter
import com.example.okaytravel.adapters.PlacesRecyclerViewAdapter
import com.example.okaytravel.adapters.placeitems.PlaceListItem
import com.example.okaytravel.models.PlaceModel
import com.example.okaytravel.presenters.PlacesPresenter
import com.example.okaytravel.views.PlacesView
import kotlinx.android.synthetic.main.fragment_places.*
import kotlinx.android.synthetic.main.fragment_places.placesRecyclerView
import kotlinx.android.synthetic.main.place_date_adapter_item.*

class PlacesFragment: BaseFragment(false), PlacesView,
    PlaceDatesRecyclerViewAdapter.OnDateItemClickedListener {

    override val fragmentNameResource: Int
        get() = R.string.places

    @ProvidePresenter
    fun providePlacesPresenter(): PlacesPresenter {
        return PlacesPresenter(this.requireActivity(), (activity as TripActivity).trip)
    }

    @InjectPresenter
    lateinit var placesPresenter: PlacesPresenter

    private val placeDates: MutableList<String> = mutableListOf()
    private var placesData: MutableMap<String, MutableList<PlaceModel>> = mutableMapOf()
    private lateinit var placeDatesAdapter: PlaceDatesRecyclerViewAdapter

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

        placeDatesAdapter = PlaceDatesRecyclerViewAdapter(placeDates, placesData, this)
        placesRecyclerView.adapter = placeDatesAdapter
    }

    override fun onDateClick(holder: PlaceDatesRecyclerViewAdapter.PlaceDateViewHolder) {
        when (holder.isExpanded) {
            true -> {
                holder.placesRecyclerView.visibility = View.GONE
                holder.actionClickIcon.setImageResource(R.drawable.ic_expand_more_24dp)
            }
            false -> {
                holder.placesRecyclerView.visibility = View.VISIBLE
                holder.actionClickIcon.setImageResource(R.drawable.ic_expand_less_24dp)
            }
        }
        holder.isExpanded = !holder.isExpanded
    }

    override fun showPlacesLoading() {
        loading.visibility = View.VISIBLE
        placesRecyclerView.visibility = View.GONE
    }

    override fun showPlaces() {
        placesRecyclerView.visibility = View.VISIBLE
        loading.visibility = View.GONE
    }

    override fun openPlacesMap() {
        val trip = (activity as TripActivity).trip
        val intent = Intent(this.requireActivity(), PlacesMapActivity::class.java)
        intent.putExtra("tripUuid", trip.uuid)
        startActivity(intent)
    }

    override fun updatePlaces(dates: MutableList<String>, places: Map<String, MutableList<PlaceModel>>) {
        placeDates.clear()
        placeDates.addAll(dates)

        places.forEach {
            placesData[it.key]?.clear()
            if (placesData[it.key] == null) placesData.plusAssign(Pair(it.key, it.value))
            else placesData[it.key]?.addAll(it.value)
            Unit
        }

        placeDatesAdapter.notifyDataSetChanged()
    }

    override fun update() {
        showPlacesLoading()
        placesPresenter.updateAll()
    }

}