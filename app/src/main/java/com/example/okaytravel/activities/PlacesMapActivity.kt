package com.example.okaytravel.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.fragments.dialogs.PlacesSearchResultBottomSheetDialogFragment
import com.example.okaytravel.database.TripDatabaseHelper
import com.example.okaytravel.hideKeyboard
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.parseDate
import com.example.okaytravel.parseDateString
import com.example.okaytravel.presenters.PlacesMapPresenter
import com.example.okaytravel.views.PlacesMapView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yandex.mapkit.Animation
import com.yandex.mapkit.GeoObjectCollection
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import kotlinx.android.synthetic.main.activity_places_map.*
import kotlinx.android.synthetic.main.dialog_add_place.view.*
import kotlinx.android.synthetic.main.dialog_loading.view.*
import java.util.*
import kotlin.math.max

class PlacesMapActivity : BaseActivity(), PlacesMapView, InputListener {

    override val fragmentContainer: Int? = null

    private val tripsDBHelper = TripDatabaseHelper()
    private lateinit var trip: TripModel
    private lateinit var searchManager: SearchManager

    private var tappedPoint: Point? = null
    private var searchItems: MutableList<GeoObjectCollection.Item> = mutableListOf()

    private lateinit var addPlaceDialogView: View

    private var searchResultsDialog: PlacesSearchResultBottomSheetDialogFragment? = null

    @ProvidePresenter
    fun providePlacesMapPresenter(): PlacesMapPresenter {
        return PlacesMapPresenter(this)
    }

    // TODO : перенести основной функционал в презентер
    @InjectPresenter
    lateinit var placesMapPresenter: PlacesMapPresenter

    private fun getTripUuidFromExtra(): String {
        return intent.getStringExtra("tripUuid")!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_map)

        SearchFactory.initialize(this)
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

        trip = tripsDBHelper.getTripByUuid(getTripUuidFromExtra()) ?: return

        placesMapView.map.addInputListener(this)

        searchEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                dismissSearchResultsDialog()
                openSearchResultsDialog()
                submitSearch(searchEdit.text.toString())
                hideKeyboard(this)
            }
            false
        }

        submitCitySearch(trip.fullAddress!!)
    }

    private fun openDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val tripStartDate = parseDateString(trip.startDate!!)
        calendar.time = tripStartDate!!
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, onDateSetListener, year, month, day)
        datePickerDialog.datePicker.minDate = max(tripStartDate.time, Date().time)
        calendar.add(Calendar.DAY_OF_MONTH, trip.duration!!)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private val onDateSetListener = DatePickerDialog.OnDateSetListener { dp, year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        addPlaceDialogView.placeDate.setText(parseDate(calendar.time))
    }

    override fun focusBudgetAmountInput() {
        addPlaceDialogView.placeBudgetAmount.requestFocus()
    }

    override fun focusBudgetCategoryInput() {
        addPlaceDialogView.placeBudgetCategory.requestFocus()
    }

    override fun enableBudgetInput() {
        addPlaceDialogView.placeBudgetCategory.isEnabled = true
        addPlaceDialogView.placeBudgetAmount.isEnabled = true
    }

    override fun disableBudgetInput() {
        addPlaceDialogView.placeBudgetCategory.setText("")
        addPlaceDialogView.placeBudgetAmount.setText("")
        addPlaceDialogView.placeBudgetCategory.isEnabled = false
        addPlaceDialogView.placeBudgetAmount.isEnabled = false
    }

    override fun onMapLongTap(p0: Map, p1: Point) {}

    override fun onMapTap(map: Map, point: Point) {
        tappedPoint = point
        dismissSearchResultsDialog()
        openSearchResultsDialog()
        clearAndAddPlacemark(point)
        submitBizSearch(point)
    }

    private fun clearAndAddPlacemark(point: Point) {
        val mapObjects = placesMapView.map.mapObjects
        mapObjects.clear()
        mapObjects.addPlacemark(point, ImageProvider.fromResource(this, R.drawable.placemark))
    }

    private fun showFirstSearchResult(response: Response) {
        if (response.collection.children.isEmpty()) return

        val firstObject = response.collection.children[0].obj ?: return
        val point = firstObject.geometry[0].point ?: return
        clearAndAddPlacemark(point)
        moveMap(point)
    }

    fun onSearchError(error: Error) {
        var errorMessage = getString(R.string.unknownError)
        if (error is RemoteError) {
            errorMessage = getString(R.string.remoteError)
        } else if (error is NetworkError) {
            errorMessage = getString(R.string.networkError)
        }
        showMessage(errorMessage)
        dismissSearchResultsDialog()
    }

    fun showAddPlaceDialog(placeName: String?, placeFullAddress: String?, point: Point) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.addPlaceButton)

        addPlaceDialogView = layoutInflater.inflate(R.layout.dialog_add_place, null)
        builder.setView(addPlaceDialogView)
        builder.setPositiveButton("Добавить", null)
        builder.setNegativeButton(R.string.cancelButton) { _, _ -> }

        addPlaceDialogView.placeDate.setOnClickListener {
            openDatePickerDialog()
        }
        addPlaceDialogView.budgetRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (group.findViewById<RadioButton>(checkedId).text == getString(R.string.yesButton)) {
                enableBudgetInput()
            }
            else {
                disableBudgetInput()
            }
        }
        addPlaceDialogView.placeNameView.text = placeName
        addPlaceDialogView.placeFullAddressView.text = placeFullAddress

        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                var budgetCategory: String? = null
                var budgetAmount: String? = null
                if (addPlaceDialogView.yesBudgetButton.isChecked) {
                    budgetCategory = addPlaceDialogView.placeBudgetCategory.text.toString()
                    budgetAmount = addPlaceDialogView.placeBudgetAmount.text.toString()
                }
                placesMapPresenter.addPlace(
                    trip,
                    placeName,
                    placeFullAddress,
                    point.latitude.toString(),
                    point.longitude.toString(),
                    addPlaceDialogView.placeDate.text.toString(),
                    budgetCategory,
                    budgetAmount
                )
            }
        }
        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        placesMapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
        placesMapView.onStop()
    }

    override fun moveMap(point: Point) {
        placesMapView.map.move(
            CameraPosition(point, placesMapView.map.cameraPosition.zoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )
    }

    override fun openPlaces() {
        val intent = Intent(this, TripActivity::class.java)
        intent.putExtra("trip", trip.uuid)
        startActivity(intent)
    }

    private fun submitSearch(query: String) {
        searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(placesMapView.map.visibleRegion),
            SearchOptions(),
            querySearchListener
        )
    }

    private fun dismissSearchResultsDialog() {
        searchResultsDialog?.dismiss()
        searchResultsDialog = null
    }

    private fun openSearchResultsDialog() {
        searchResultsDialog = PlacesSearchResultBottomSheetDialogFragment(searchItems)
        searchResultsDialog?.isCancelable = false
        searchResultsDialog?.show(supportFragmentManager, "PlacesSearchResultTag")
    }

    private val querySearchListener = object: Session.SearchListener {
        override fun onSearchError(error: Error) { this@PlacesMapActivity.onSearchError(error) }

        override fun onSearchResponse(response: Response) {
            showFirstSearchResult(response)
            searchItems.clear()
            searchItems.addAll(response.collection.children)
            searchResultsDialog?.updateSearchItems()
        }

    }

    private fun submitGeoSearch(point: Point) {
        searchManager.submit(point, null, SearchOptions().setSearchTypes(SearchType.GEO.value), geoSearchListener)
    }

    private val geoSearchListener = object: Session.SearchListener {
        override fun onSearchError(error: Error) { this@PlacesMapActivity.onSearchError(error) }

        override fun onSearchResponse(response: Response) {
            searchItems.addAll(response.collection.children)
            searchResultsDialog?.updateSearchItems()
        }

    }

    private fun submitBizSearch(point: Point) {
        searchManager.submit(point, null, SearchOptions().setSearchTypes(SearchType.BIZ.value), bizSearchListener)
    }

    private val bizSearchListener = object: Session.SearchListener {
        override fun onSearchError(error: Error) { this@PlacesMapActivity.onSearchError(error) }

        override fun onSearchResponse(response: Response) {
            searchItems.clear()
            searchItems.addAll(response.collection.children)
            tappedPoint?.let { submitGeoSearch(it) }
        }

    }

    private fun submitCitySearch(query: String) {
        searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(placesMapView.map.visibleRegion),
            SearchOptions().setSearchTypes(SearchType.GEO.value),
            citySearchListener
        )
    }

    private val citySearchListener = object: Session.SearchListener {
        override fun onSearchError(error: Error) { this@PlacesMapActivity.onSearchError(error) }

        override fun onSearchResponse(response: Response) {
            if (response.collection.children.isEmpty()) return
            val found = response.collection.children[0].obj ?: return
            val mapObjects = placesMapView.map.mapObjects
            val point = found.geometry[0].point!!
            mapObjects.addPlacemark(point)
            placesMapView.map.move(
                CameraPosition(point, 14f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 1f),
                null
            )
        }

    }
}
