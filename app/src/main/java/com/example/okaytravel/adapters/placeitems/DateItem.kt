package com.example.okaytravel.adapters.placeitems

class DateItem(val date: String): PlaceListItem() {
    override val Type: Int
        get() = TYPE_DATE
}