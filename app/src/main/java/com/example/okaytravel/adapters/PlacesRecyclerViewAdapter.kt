package com.example.okaytravel.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.okaytravel.R
import com.example.okaytravel.models.PlaceModel

class PlacesRecyclerViewAdapter(
    private val places: MutableList<PlaceModel>,
    private val listener: OnPlaceItemClickedListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return PlaceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.place_adapter_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val placeViewHolder = holder as PlaceViewHolder
        placeViewHolder.bind(places[position])
    }

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fullAddress: TextView = itemView.findViewById(R.id.fullAddressView)
        private val placeNameView: TextView = itemView.findViewById(R.id.placeNameView)
        private val placeCardView: CardView = itemView.findViewById(R.id.placeCardView)
        val selectedCheckbox: CheckBox = itemView.findViewById(R.id.selectedCheckbox)

        fun bind(place: PlaceModel) {
            fullAddress.text = place.fullAddress
            placeNameView.text = place.name

            placeCardView.setOnClickListener {
                listener.onPlaceItemClicked(this, place)
            }
            selectedCheckbox.setOnClickListener {
                selectedCheckbox.isChecked = true
                listener.onPlaceItemClicked(this, place)
            }
        }
    }

    interface OnPlaceItemClickedListener {
        fun onPlaceItemClicked(holder: PlaceViewHolder, place: PlaceModel)
    }
}