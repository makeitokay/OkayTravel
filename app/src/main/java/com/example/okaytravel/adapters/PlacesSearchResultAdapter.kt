package com.example.okaytravel.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.okaytravel.R
import com.example.okaytravel.activities.PlacesMapActivity
import com.example.okaytravel.activities.TripActivity
import com.example.okaytravel.models.TripModel
import com.yandex.mapkit.GeoObjectCollection
import kotlinx.android.synthetic.main.places_search_adapter_item.view.*

class PlacesSearchResultAdapter(
    private val searchItems: MutableList<GeoObjectCollection.Item>):
    RecyclerView.Adapter<PlacesSearchResultAdapter.ViewHolder>() {

    var onItemClick: ((pos: Int, view: View?) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.places_search_adapter_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return searchItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchItems[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            onItemClick?.invoke(adapterPosition, v)
        }

        private val fullAddress: TextView = itemView.findViewById(R.id.fullAddressSearchItem)
        private val name: TextView = itemView.findViewById(R.id.nameSearchItem)

        fun bind(item: GeoObjectCollection.Item) {
            fullAddress.text = item.obj?.descriptionText
            name.text = item.obj?.name
            itemView.placeSearchItem.setOnClickListener(this)
        }
    }
}