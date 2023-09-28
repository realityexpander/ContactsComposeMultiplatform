package com.realityexpander.contactscomposemultiplatform.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import cocoapods.GoogleMaps.GMSMarker
import cocoapods.GoogleMaps.GMSMutablePath
import cocoapods.GoogleMaps.GMSPolyline
import cocoapods.GoogleMaps.animateWithCameraUpdate
import cocoapods.GoogleMaps.kGMSTypeNormal
import cocoapods.GoogleMaps.kGMSTypeSatellite
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.UIKit.UIEdgeInsetsMake

@Composable
actual fun GoogleMaps(
    modifier: Modifier,
    markers: List<MapMarker>?,
    cameraPosition: CameraPosition?,
    cameraPositionLatLongBounds: CameraPositionLatLongBounds?,
    polyLine: List<LatLong>?
) {
    val mapsView = remember {
        GMSMapView()
    }
    var isMyLocationEnabled by remember {
        mutableStateOf(false)
    }
    var gsmMapViewType by remember {
        mutableStateOf(
            kGMSTypeNormal
        )
    }

    //initial height set at 0.dp
    var componentHeight by remember { mutableStateOf(0.dp) }

    // get local density from composable
    val density = LocalDensity.current

    Box(Modifier
        .fillMaxSize()
        .onGloballyPositioned {
            componentHeight = with(density) { // used to put the "my location" to the top of the map
                it.size.height.toDp()
            }
        }
    ) {

        UIKitView(
            modifier = modifier.fillMaxSize(),
            interactive = true,
            factory = {
                mapsView
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

            }
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomStart),
            horizontalAlignment = Alignment.Start
        ) {
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
                gsmMapViewType = if(shouldUseSatellite) kGMSTypeSatellite else kGMSTypeNormal
            }
        }
    }
}
