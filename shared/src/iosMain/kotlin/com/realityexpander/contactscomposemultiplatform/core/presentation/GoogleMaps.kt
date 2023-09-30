package com.realityexpander.contactscomposemultiplatform.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import cocoapods.GoogleMaps.GMSCameraPosition
import cocoapods.GoogleMaps.GMSCameraUpdate
import cocoapods.GoogleMaps.GMSCameraUpdate.Companion.fitBounds
import cocoapods.GoogleMaps.GMSCoordinateBounds
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMapViewDelegateProtocol
import cocoapods.GoogleMaps.GMSMarker
import cocoapods.GoogleMaps.GMSMutablePath
import cocoapods.GoogleMaps.GMSPolyline
import cocoapods.GoogleMaps.animateWithCameraUpdate
import cocoapods.GoogleMaps.kGMSTypeNormal
import cocoapods.GoogleMaps.kGMSTypeSatellite
import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKAnnotationProtocol
import platform.MapKit.MKMapCamera
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.MapKit.MKPointAnnotation
import platform.UIKit.UIEdgeInsetsMake
import platform.darwin.NSObject


@Composable
actual fun GoogleMaps(
    modifier: Modifier,
    isControlsVisible: Boolean,
    onMarkerClick: ((MapMarker) -> Unit)?,
    onMapClick: ((LatLong) -> Unit)?,
    onMapLongClick: ((LatLong) -> Unit)?,
    markers: List<MapMarker>?,
    cameraPosition: CameraPosition?,
    cameraPositionLatLongBounds: CameraPositionLatLongBounds?,
    polyLine: List<LatLong>?
) {
    val googleMapView = remember { GMSMapView().apply {

    } }
    val mkMapView = remember { MKMapView().apply {
        setUserInteractionEnabled(true)
//        setDelegate(MKMapDelegate)
    } }
    val mkMapCamera = remember { MKMapCamera().apply {
        setCenterCoordinate(CLLocationCoordinate2DMake(18.982579225106615, -99.09380710785197))
        setAltitude(50000.0)
    } }
    var showSomething = remember { false }

    var isMyLocationEnabled by remember { mutableStateOf(false) }
    var gsmMapViewType by remember { mutableStateOf(kGMSTypeNormal) }

    // initial height set at 0.dp
    var componentHeight by remember { mutableStateOf(0.dp) }
    // get local density from composable
    val density = LocalDensity.current

    val onValueChange: (text: String) -> Unit = {
        println("value = $it")
    }

    val markers2 = mutableListOf<MapMarker>()

     Box(Modifier
        .fillMaxSize()
        .onGloballyPositioned {
            componentHeight = with(density) { // used to put the "my location" to the top of the map
                it.size.height.toDp()
            }
        }
    ) {

         if(false) {
             // MapKit
             UIKitView(
                 modifier = modifier.fillMaxSize(),
                 interactive = true,
                 factory = {
                     mkMapView.delegate = object : NSObject(), MKMapViewDelegateProtocol {
                      // NOTE: Delegates are not currently supported in Kotlin/Native

//                    override fun mapView(mapView: MKMapView, didSelectAnnotationView: MKAnnotationView) {
//                        showSomething = true
//                    }
//                    override fun mapView(mapView: MKMapView, didDeselectAnnotationView: MKAnnotationView) {
//                        showSomething = false
//                    }


//                    override fun mapView(mapView: MKMapView, didSelectAnnotation: MKAnnotationProtocol) {
//                        showSomething = true
//                    }
//                    override fun mapView(mapView: MKMapView, didDeselectAnnotation: MKAnnotationProtocol) {
//                        showSomething = false
//                    }


//                    override fun mapView(mapView: MKMapView, didUpdateUserLocation: MKUserLocation) {
//                        showSomething = true
//                    }
//                    override fun mapViewDidFinishLoadingMap(mapView: MKMapView) {
//                        showSomething = true
//                    }
                     }

                     mkMapView
                 },
                 update = { view ->
                     cameraPosition?.let {
                         view.setCamera(mkMapCamera, true)
                     }

                     markers?.forEach { marker ->
                         println("marker = ${marker.position.latitude}, ${marker.position.longitude}")
                         val annotation = object : NSObject(), MKAnnotationProtocol {
                             override fun coordinate(): CValue<CLLocationCoordinate2D> {
                                 return CLLocationCoordinate2DMake(
                                     marker.position.latitude,
                                     marker.position.longitude
                                 )
                             }

                             override fun title(): String {
                                 return marker.title
                             }

                             override fun subtitle(): String {
                                 return "Your mom"
                             }
                         }
//                    annotation.setCoordinate(CLLocationCoordinate2DMake(
//                        marker.position.latitude,
//                        marker.position.longitude
//                    ))
//                    view.addAnnotation(annotation)
//                    view.selectAnnotation(annotation, true)

                         val markerPoint = MKPointAnnotation()
                         markerPoint.setCoordinate(
                             CLLocationCoordinate2DMake(
                                 marker.position.latitude,
                                 marker.position.longitude
                             )
                         )
                         markerPoint.setTitle(marker.title)
                         markerPoint.setSubtitle("Your mom")
                         view.addAnnotation(markerPoint)
//                    view.selectAnnotation(markerPoint, true)
                     }
                 },

             )
         }

         // Google Maps
         if(true) {
            UIKitView(
                modifier = modifier.fillMaxSize(),
                interactive = true,
                factory = {
                    googleMapView.delegate = object :  NSObject(), GMSMapViewDelegateProtocol {
                        // NOTE: Delegates are not currently supported in Kotlin/Native for iOS

//                        override fun mapView(mapView: GMSMapView, didLongPressAtCoordinate: CValue<CLLocationCoordinate2D>) {
//                            showSomething = true
//                            didLongPressAtCoordinate.useContents {
//                                markers2.add(MapMarker("Chris", LatLong(this.latitude, this.longitude)))
//                            }
//                            println(markers2)
//                        }

    //                    override fun didTapMyLocationButtonForMapView(mapView: GMSMapView): Boolean {
    //                        showSomething = true
    //
    //                        return true
    //                    }

//                        override fun mapViewDidFinishTileRendering(mapView: cocoapods.GoogleMaps.GMSMapView) {
//                            println("mapViewDidFinishTileRendering")
//                        }

//                        override fun mapView(mapView: GMSMapView, didTapAtCoordinate: CValue<CLLocationCoordinate2D>) {
//                            println("didTapAtCoordinate")
//                        }
                    }

                    googleMapView
                },
                update = { view ->
                    cameraPosition?.let {
                        view.setCamera(
                            GMSCameraPosition.cameraWithLatitude(
                                it.target.latitude,
                                it.target.longitude,
                                it.zoom
                            )
                        )
                    }

                    cameraPositionLatLongBounds?.let { cameraPositionLatLongBounds ->

                        val bounds = GMSCoordinateBounds()
                        cameraPositionLatLongBounds.coordinates.forEach { latLong ->
                            bounds.includingCoordinate(
                                CLLocationCoordinate2DMake(
                                    latitude = latLong.latitude,
                                    longitude = latLong.longitude
                                )
                            )
                        }
                        GMSCameraUpdate().apply {
                            fitBounds(bounds, cameraPositionLatLongBounds.padding.toDouble())
                            view.animateWithCameraUpdate(this)
                        }
                    }

                    markers?.forEach { marker ->
                        GMSMarker().apply {
                            position = CLLocationCoordinate2DMake(
                                marker.position.latitude,
                                marker.position.longitude
                            )
                            title = marker.title
                            map = view
                        }
                    }

                    polyLine?.let { polyLine ->
                        val points = polyLine.map {
                            CLLocationCoordinate2DMake(it.latitude, it.longitude)
                        }
                        val path = GMSMutablePath().apply {
                            points.forEach { point ->
                                addCoordinate(point)
                            }
                        }

                        GMSPolyline().apply {
                            this.path = path
                            this.map = view
                        }
                    }

                    view.settings.setZoomGestures(true)
                    view.settings.setCompassButton(true)

                    view.myLocationEnabled = isMyLocationEnabled
                    view.settings.myLocationButton = isMyLocationEnabled
                    view.mapType = gsmMapViewType
                    view.padding = UIEdgeInsetsMake(0.0, 0.0, 100.0, 0.0)

                    // get the bounds of this UI view and set the padding to the bottom of the view
                    // so that the bottom controls are not covered by the map
                    view.padding = UIEdgeInsetsMake(0.0, 0.0, componentHeight.value - 80.0, 0.0)
                },

            )
         }

        if(isControlsVisible) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomStart),
                horizontalAlignment = Alignment.Start
            ) {
                if(showSomething) {
                    Text("SOMETHING")
                }

                SwitchWithLabel(
                    label = "My location",
                    state = isMyLocationEnabled,
                    darkOnLightTextColor = gsmMapViewType == kGMSTypeSatellite
                ) {
                    isMyLocationEnabled = !isMyLocationEnabled
                }
                SwitchWithLabel(
                    label = "Satellite",
                    state = gsmMapViewType == kGMSTypeSatellite,
                    darkOnLightTextColor = gsmMapViewType == kGMSTypeSatellite
                ) { shouldUseSatellite ->
                    gsmMapViewType = if (shouldUseSatellite) kGMSTypeSatellite else kGMSTypeNormal
                }
            }
        }
    }
}
