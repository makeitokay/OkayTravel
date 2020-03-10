package com.example.okaytravel.activities

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.okaytravel.R
import com.example.okaytravel.presenters.PlaceRoutesPresenter
import com.example.okaytravel.views.PlaceRoutesView
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.geometry.SubpolylineHelper
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.*
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import kotlinx.android.synthetic.main.activity_place_routes.*

class PlaceRoutesActivity : BaseActivity(), PlaceRoutesView, Session.RouteListener {

    override val fragmentContainer: Int?
        get() = null

    @InjectPresenter
    lateinit var placeRoutesPresenter: PlaceRoutesPresenter

    private lateinit var mtRouter: PedestrianRouter
    private lateinit var routeMapObjects: MapObjectCollection

    private fun getPlacesUuidsFromExtra(): ArrayList<String> {
        val placesUuids = intent.extras?.getStringArrayList("places")
        return placesUuids!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.initialize(this)
        TransportFactory.initialize(this)

        setContentView(R.layout.activity_place_routes)
        super.onCreate(savedInstanceState)

        placeRoutesPresenter.showInputPlaces(getPlacesUuidsFromExtra())

        routeMapObjects = routesMapView.map.mapObjects.addCollection()
        mtRouter = TransportFactory.getInstance().createPedestrianRouter()

    }

    override fun showPlace(point: Point) {
        routesMapView.map.mapObjects.addPlacemark(
            point, ImageProvider.fromResource(
                applicationContext, R.drawable.placemark
            )
        )
    }

    override fun showRoute(points: MutableList<Point>) {
        val requestPoints: MutableList<RequestPoint> = mutableListOf()
        points.forEach {
            requestPoints.add(RequestPoint(it, RequestPointType.WAYPOINT, null))
        }

        mtRouter.requestRoutes(requestPoints, TimeOptions(), this)
    }

    override fun onMasstransitRoutesError(error: Error) {
        var errorMessage = getString(R.string.unknownError)
        if (error is RemoteError) {
            errorMessage = getString(R.string.remoteError)
        } else if (error is NetworkError) {
            errorMessage = getString(R.string.networkError)
        }
        showMessage(errorMessage)
    }

    override fun onMasstransitRoutes(routes: MutableList<Route>) {
        if (routes.size > 0) {
            for (section in routes[0].sections) {
                drawSection(SubpolylineHelper.subpolyline(routes[0].geometry, section.geometry))
            }
        } else {
            showMessage(R.string.routeError)
        }
    }

    private fun drawSection(geometry: Polyline) {
        val polylineMapObject = routeMapObjects.addPolyline(geometry)
        polylineMapObject.strokeColor = -0xffff01
    }

    override fun moveMap(point: Point) {
        routesMapView.map.move(
            CameraPosition(point, 14f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        routesMapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
        routesMapView.onStop()
    }
}
