package com.example.okaytravel.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.okaytravel.R
import com.example.okaytravel.getHumanReadableDate
import com.example.okaytravel.models.PlaceModel

class PlaceDatesRecyclerViewAdapter(
    private val dates: MutableList<String>,
    private val placesData: Map<String, MutableList<PlaceModel>>,
    private val onDateItemClickedListener: OnDateItemClickedListener,
    private val onPlaceItemClickedListener: PlacesRecyclerViewAdapter.OnPlaceItemClickedListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return PlaceDateViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.place_date_adapter_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dates.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val placeDateViewHolder = holder as PlaceDateViewHolder
        placeDateViewHolder.bind(dates[position], placesData.getValue(dates[position]))
    }

    inner class PlaceDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeDate: TextView = itemView.findViewById(R.id.placeDateView)
        private val placeDateCardView: CardView = itemView.findViewById(R.id.placeDateCardView)

        val placesRecyclerView: RecyclerView = itemView.findViewById(R.id.placesRecyclerView)
        val actionClickIcon: ImageView = itemView.findViewById(R.id.actionIcon)

        var isExpanded = false

        fun bind(date: String, places: MutableList<PlaceModel>) {
            placeDate.text = getHumanReadableDate(date)

            val placesAdapter = PlacesRecyclerViewAdapter(places, onPlaceItemClickedListener)
            placesRecyclerView.adapter = placesAdapter

            placeDateCardView.setOnClickListener {
                onDateItemClickedListener.onDateClick(this)
            }
        }
    }

    interface OnDateItemClickedListener {
        fun onDateClick(holder: PlaceDateViewHolder)
    }
}