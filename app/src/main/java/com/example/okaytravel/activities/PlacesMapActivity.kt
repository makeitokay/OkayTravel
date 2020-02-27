package com.example.okaytravel.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.RadioButton
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.database.TripDatabaseHelper
import com.example.okaytravel.hideKeyboard
import com.example.okaytravel.models.TripModel
import com.example.okaytravel.parseDate
import com.example.okaytravel.parseDateString
import com.example.okaytravel.presenters.PlacesMapPresenter
import com.example.okaytravel.views.PlacesMapView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yandex.mapkit.Animation
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
import kotlinx.android.synthetic.main.sheet_add_place.*
import java.util.*

class PlacesMapActivity : BaseActivity(), PlacesMapView, InputListener, Session.SearchListener {

    override val fragmentContainer: Int? = null

    private val tripsDBHelper = TripDatabaseHelper()
    private lateinit var trip: TripModel
    private lateinit var searchManager: SearchManager
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

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

        placesMapView.map.move(CameraPosition(Point(0.0, 0.0), 14.0f, 0.0f, 0.0f))
        submitSearch(trip.fullAddress!!)

        placesMapView.map.addInputListener(this)

        searchEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                submitSearch(searchEdit.text.toString())
                hideKeyboard(this)
            }
            false
        }

        placeDate.setOnClickListener {
            openDatePickerDialog()
        }

        budgetRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (group.findViewById<RadioButton>(checkedId).text == getString(R.string.yesButton))
                enableBudgetInput()
            else
                disableBudgetInput()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(bottomAddPlaceSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private val onDateSetListener = DatePickerDialog.OnDateSetListener { dp, year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        placeDate.setText(parseDate(calendar.time))
    }

    private fun openDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val tripStartDate = parseDateString(trip.startDate!!)
        calendar.time = tripStartDate!!
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, onDateSetListener, year, month, day)
        datePickerDialog.datePicker.minDate = tripStartDate.time
        calendar.add(Calendar.DAY_OF_MONTH, trip.duration!!)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    override fun enableBudgetInput() {
        placeBudgetCategory.isEnabled = true
        placeBudgetAmount.isEnabled = true
    }

    override fun disableBudgetInput() {
        placeBudgetCategory.setText("")
        placeBudgetAmount.setText("")
        placeBudgetCategory.isEnabled = false
        placeBudgetAmount.isEnabled = false
    }

    override fun onMapLongTap(p0: Map, p1: Point) {}

    override fun onMapTap(map: Map, point: Point) {
        submitGeoSearch(point)
    }

    override fun onSearchError(error: Error) {
        var errorMessage = getString(R.string.unknownError)
        if (error is RemoteError) {
            errorMessage = getString(R.string.remoteError)
        } else if (error is NetworkError) {
            errorMessage = getString(R.string.networkError)
        }
        showMessage(errorMessage)
    }

    override fun onSearchResponse(response: Response) {
        val mapObjects = placesMapView.map.mapObjects
        mapObjects.clear()

        if (response.collection.children.isEmpty()) {
            showMessage(R.string.searchIsEmpty)
            return
        }

        val point = response.collection.children[0].obj!!.geometry[0].point!!
        moveMap(point)
        val foundObject = response.collection.children[0].obj
        if (response.metadata.requestText != trip.fullAddress && foundObject != null) {
            mapObjects.addPlacemark(point, ImageProvider.fromResource(this, R.drawable.placemark))

            addPlaceBtn.setOnClickListener {
                var budgetCategory: String? = null
                var budgetAmount: String? = null
                if (yesBudgetButton.isChecked) {
                    budgetCategory = placeBudgetCategory.text.toString()
                    budgetAmount = placeBudgetAmount.text.toString()
                }
                placesMapPresenter.addPlace(
                    trip,
                    placeNameView.text.toString(),
                    placeFullAddressView.text.toString(),
                    point.latitude.toString(),
                    point.longitude.toString(),
                    placeDate.text.toString(),
                    budgetCategory,
                    budgetAmount
                )
            }

            placeNameView.text = foundObject.name
            placeFullAddressView.text = foundObject.descriptionText
            bottomAddPlaceSheet.post {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
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

    private fun submitSearch(query: String) {
        searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(placesMapView.map.visibleRegion),
            SearchOptions(),
            this
        )
    }

    private fun submitGeoSearch(point: Point) {
        searchManager.submit(point, null, SearchOptions().setSearchTypes(SearchType.GEO.value), this)
    }

    override fun openPlaces() {
        val intent = Intent(this, TripActivity::class.java)
        intent.putExtra("trip", trip.uuid)
        startActivity(intent)
    }
}
