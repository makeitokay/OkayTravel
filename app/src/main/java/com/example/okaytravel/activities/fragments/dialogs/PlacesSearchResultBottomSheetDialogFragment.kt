package com.example.okaytravel.activities.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.okaytravel.R
import com.example.okaytravel.activities.PlacesMapActivity
import com.example.okaytravel.adapters.PlacesSearchResultAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yandex.mapkit.GeoObjectCollection
import kotlinx.android.synthetic.main.places_search_result_bottom_sheet.*

class PlacesSearchResultBottomSheetDialogFragment(
    var searchItems: MutableList<GeoObjectCollection.Item>
): BottomSheetDialogFragment() {

    lateinit var searchResultsAdapter: PlacesSearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.places_search_result_bottom_sheet, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchResultsAdapter = PlacesSearchResultAdapter(searchItems)
        placesSearchResult.adapter = searchResultsAdapter

        searchResultsAdapter.onItemClick = { pos, _ ->
            val obj = searchItems[pos].obj
            obj?.let {
                (activity as PlacesMapActivity)
                    .showAddPlaceDialog(obj.name, obj.descriptionText, obj.geometry[0].point!!)
                dismiss()
            }
        }
    }

    fun updateSearchItems() {
        placesSearchItemsLoading.visibility = View.GONE
        searchResultsAdapter.notifyDataSetChanged()
        if (searchItems.isEmpty()) {
            placesSearchResult.visibility = View.GONE
            searchIsEmptyView.visibility = View.VISIBLE
        } else {
            placesSearchResult.visibility = View.VISIBLE
            searchIsEmptyView.visibility = View.GONE
        }
    }
}