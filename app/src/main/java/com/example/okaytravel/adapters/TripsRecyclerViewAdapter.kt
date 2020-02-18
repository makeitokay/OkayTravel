package com.example.okaytravel.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.okaytravel.R
import com.example.okaytravel.models.TripModel

class TripsRecyclerViewAdapter(val tripsList: MutableList<TripModel>): RecyclerView.Adapter<TripsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.trip_adapter_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return tripsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ownPlace.text = tripsList[position].ownPlace
        holder.startDate.text = tripsList[position].startDate
        holder.duration.text = tripsList[position].duration.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ownPlace: TextView = itemView.findViewById(R.id.ownPlace)
        val startDate: TextView = itemView.findViewById(R.id.startDate)
        val duration: TextView = itemView.findViewById(R.id.duration)
    }
}