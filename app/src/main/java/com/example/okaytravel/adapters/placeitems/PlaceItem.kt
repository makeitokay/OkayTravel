package com.example.okaytravel.adapters.placeitems

import com.example.okaytravel.models.PlaceModel

class PlaceItem(val place: PlaceModel): PlaceListItem() {
    override val Type: Int
        get() = TYPE_PLACE
}