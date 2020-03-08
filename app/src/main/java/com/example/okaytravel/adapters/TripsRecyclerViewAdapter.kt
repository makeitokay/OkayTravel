package com.example.okaytravel.adapters

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.okaytravel.AVAILABLE_CITY_IMAGES
import com.example.okaytravel.R
import com.example.okaytravel.activities.HomeActivity
import com.example.okaytravel.activities.TripActivity
import com.example.okaytravel.models.TripModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.lang.Exception

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
        private val cityImage: ImageView = itemView.findViewById(R.id.cityImageView)

        private lateinit var picassoRequest: RequestCreator

        fun bind(trip: TripModel) {
            ownPlace.text = trip.ownPlace
            startDate.text = trip.startDate

            picassoRequest = when (trip.fullAddress) {
                in AVAILABLE_CITY_IMAGES ->
                    Picasso.get().load(
                        "http://okaytravel.pythonanywhere.com/image?city=${trip.fullAddress}"
                    )
                else ->
                    Picasso.get().load(R.drawable.standard_city_image)
            }

            picassoRequest
                .error(R.drawable.standard_city_image)
                .placeholder(ColorDrawable(context.getColor(android.R.color.darker_gray)))
                .fit()
                .into(cityImage)

            itemView.setOnClickListener {
                val intent = Intent(context, TripActivity::class.java)
                intent.putExtra("trip", trip.uuid)
                context.startActivity(intent)
            }
        }
    }
}