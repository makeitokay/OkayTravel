package com.example.okaytravel.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.okaytravel.R
import com.example.okaytravel.adapters.placeitems.DateItem
import com.example.okaytravel.adapters.placeitems.PlaceItem
import com.example.okaytravel.adapters.placeitems.PlaceListItem

class PlacesRecyclerViewAdapter(private val placeItems: MutableList<PlaceListItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                PlaceViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.place_adapter_item, parent, false)
                )
            }
            else -> {
                PlaceDateViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.place_date_adapter_item, parent, false)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return placeItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> {
                val placeViewHolder = holder as PlaceViewHolder
                placeViewHolder.bind(placeItems[position] as PlaceItem)
            }
            else -> {
                val placeDateViewHolder = holder as PlaceDateViewHolder
                placeDateViewHolder.bind(placeItems[position] as DateItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return placeItems[position].Type
    }

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fullAddress: TextView = itemView.findViewById(R.id.fullAddressView)
        private val placeNameView: TextView = itemView.findViewById(R.id.placeNameView)

        fun bind(place: PlaceItem) {
            fullAddress.text = place.place.fullAddress
            placeNameView.text = place.place.name
        }
    }

    class PlaceDateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val placeDate: TextView = itemView.findViewById(R.id.placeDateView)

        fun bind(date: DateItem) {
            placeDate.text = date.date
        }
    }
}