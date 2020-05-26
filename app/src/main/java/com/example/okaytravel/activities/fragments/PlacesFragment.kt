package com.example.okaytravel.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.PlaceRoutesActivity
import com.example.okaytravel.activities.PlacesMapActivity
import com.example.okaytravel.activities.TripActivity
import com.example.okaytravel.adapters.PlaceDatesRecyclerViewAdapter
import com.example.okaytravel.adapters.PlacesRecyclerViewAdapter
import com.example.okaytravel.models.PlaceModel
import com.example.okaytravel.parseDateString
import com.example.okaytravel.presenters.PlacesPresenter
import com.example.okaytravel.views.PlacesView
import kotlinx.android.synthetic.main.fragment_places.*
import kotlinx.android.synthetic.main.fragment_places.placesRecyclerView
import kotlinx.android.synthetic.main.place_adapter_item.*
import java.util.*

class PlacesFragment : BaseFragment(false), PlacesView,
    PlaceDatesRecyclerViewAdapter.OnDateItemClickedListener,
    PlacesRecyclerViewAdapter.OnPlaceItemClickedListener {

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

    private val selectedPlaces: MutableList<PlaceModel> = mutableListOf()

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

        showPlaceBtn.setOnClickListener {
            openPlace()
        }

        showRouteBtn.setOnClickListener {
            openRoute()
        }

        placeDatesAdapter = PlaceDatesRecyclerViewAdapter(
            placeDates,
            placesData,
            this,
            this
        )
        placesRecyclerView.adapter = placeDatesAdapter
    }

    override fun onDateClick(holder: PlaceDatesRecyclerViewAdapter.PlaceDateViewHolder) {
        when (holder.isExpanded) {
            true -> {
                holder.placesRecyclerView.visibility = View.GONE
                holder.actionClickIcon.setImageResource(R.drawable.ic_expand_more_24dp)
                updateActionButtons()
            }
            false -> {
                holder.placesRecyclerView.visibility = View.VISIBLE
                holder.actionClickIcon.setImageResource(R.drawable.ic_expand_less_24dp)
            }
        }
        holder.isExpanded = !holder.isExpanded
    }

    override fun onPlaceItemClicked(
        holder: PlacesRecyclerViewAdapter.PlaceViewHolder,
        place: PlaceModel
    ) {
        if (selectedPlaces.contains(place)) {
            selectedPlaces.remove(place)
            holder.selectedCheckbox.visibility = View.GONE
        } else {
            selectedPlaces.add(place)
            holder.selectedCheckbox.visibility = View.VISIBLE
        }
        updateActionButtons()
    }

    override fun updateActionButtons() {
        when (selectedPlaces.size) {
            1 -> {
                showPlaceBtn.visibility = View.VISIBLE
                showRouteBtn.visibility = View.GONE
                addPlaceBtn.visibility = View.GONE
            }
            2 -> {
                showRouteBtn.visibility = View.VISIBLE
                showPlaceBtn.visibility = View.GONE
                addPlaceBtn.visibility = View.GONE
            }
            else -> {
                showRouteBtn.visibility = View.GONE
                showPlaceBtn.visibility = View.GONE
                addPlaceBtn.visibility = View.VISIBLE
            }
        }
    }

    override fun showPlacesLoading() {
        loading.visibility = View.VISIBLE
        placesRecyclerView.visibility = View.GONE
    }

    override fun showPlaces() {
        placesRecyclerView.visibility = View.VISIBLE
        loading.visibility = View.GONE
    }

    override fun openPlace() {
        val intent = Intent(this.requireActivity(), PlaceRoutesActivity::class.java)
        intent.putStringArrayListExtra("places", arrayListOf<String>(selectedPlaces[0].uuid!!))
        startActivity(intent)
    }

    override fun openRoute() {
        val intent = Intent(this.requireActivity(), PlaceRoutesActivity::class.java)
        val places = arrayListOf<String>()
        selectedPlaces.forEach { places.add(it.uuid!!) }
        intent.putStringArrayListExtra("places", places)
        startActivity(intent)
    }

    override fun openPlacesMap() {
        val trip = (activity as TripActivity).trip

        val currentDate = Calendar.getInstance()
        val finishDate = Calendar.getInstance()
        finishDate.time = parseDateString(trip.startDate!!)!!
        finishDate.add(Calendar.DAY_OF_MONTH, trip.duration!! + 1)
        if (currentDate.time > finishDate.time) {
            showMessage(R.string.tripFinished)
            return
        }

        val intent = Intent(this.requireActivity(), PlacesMapActivity::class.java)
        intent.putExtra("tripUuid", trip.uuid)
        startActivity(intent)
    }

    override fun updatePlaces(
        dates: MutableList<String>,
        places: Map<String, MutableList<PlaceModel>>
    ) {
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