package com.example.okaytravel.adapters.thingitems

import com.example.okaytravel.models.ThingModel

class ThingItem(val thing: ThingModel) : ThingListItem() {
    override val type: Int
        get() = TYPE_THING
}