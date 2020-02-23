package com.example.okaytravel.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.okaytravel.R

class PlacesFragment: BaseFragment(false) {
    override val fragmentNameResource: Int
        get() = R.string.places

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_places, container, false)
    }
}