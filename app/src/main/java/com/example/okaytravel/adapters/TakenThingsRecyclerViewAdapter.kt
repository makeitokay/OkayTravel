package com.example.okaytravel.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.okaytravel.R
import com.example.okaytravel.adapters.thingitems.ThingItem
import com.example.okaytravel.adapters.thingitems.ThingListItem
import com.example.okaytravel.models.ThingModel
import kotlinx.android.synthetic.main.thing_add_adapter_item.view.*

class TakenThingsRecyclerViewAdapter(
    private val thingsItems: MutableList<ThingListItem>,
    private val onThingItemClickListener: ThingViewHolder.OnThingItemClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ThingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.thing_adapter_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return thingsItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val thingHolder = holder as ThingViewHolder
        thingHolder.bind(thingsItems[position] as ThingItem, onThingItemClickListener, position)
    }

    class ThingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thingName = itemView.findViewById<TextView>(R.id.thingNameView)
        private val thingTakenCheckbox = itemView.findViewById<CheckBox>(R.id.isThingTakenCheckbox)
        private val thingItem = itemView.findViewById<CardView>(R.id.thingItem)

        fun bind(item: ThingItem, listener: OnThingItemClickListener, position: Int) {
            thingName.text = item.thing.name
            thingTakenCheckbox.isChecked = true
            thingItem.setOnClickListener {
                listener.onThingItemClicked(item, position)
            }
            thingTakenCheckbox.setOnClickListener {
                listener.onThingItemClicked(item, position)
            }
        }

        interface OnThingItemClickListener {
            fun onThingItemClicked(thingItem: ThingListItem, position: Int)
        }
    }
}