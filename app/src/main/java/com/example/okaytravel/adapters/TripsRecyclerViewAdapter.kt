package com.example.okaytravel.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.okaytravel.R
import com.example.okaytravel.activities.HomeActivity
import com.example.okaytravel.activities.TripActivity
import com.example.okaytravel.models.TripModel

class TripsRecyclerViewAdapter(
    private val tripsList: MutableList<TripModel>,
    private val context: Context):
    RecyclerView.Adapter<TripsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.trip_adapter_item, parent, false)
        return ViewHolder(v, context)
    }

    override fun getItemCount(): Int {
        return tripsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tripsList[position])
    }

    class ViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
        private val ownPlace: TextView = itemView.findViewById(R.id.ownPlace)
        private val startDate: TextView = itemView.findViewById(R.id.startDate)

        fun bind(trip: TripModel) {
            ownPlace.text = trip.ownPlace
            startDate.text = trip.startDate
            itemView.setOnClickListener {
                val intent = Intent(context, TripActivity::class.java)
                intent.putExtra("trip", trip.uuid)
                context.startActivity(intent)
            }
        }
    }
}