package com.realityexpander.contactscomposemultiplatform.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
actual fun GoogleMaps(
    modifier: Modifier,
    markers: List<MapMarker>?,
    cameraPosition: CameraPosition?,
    cameraPositionLatLongBounds: CameraPositionLatLongBounds?,
    polyLine: List<LatLong>?
) {

    val cameraPositionState = rememberCameraPositionState()
    var uiSettings by remember {
        mutableStateOf(MapUiSettings(
            myLocationButtonEnabled = false,
            compassEnabled = false,
            mapToolbarEnabled = false,
            zoomControlsEnabled = false,
            scrollGesturesEnabled = false
        )) }
    var properties by remember {
        mutableStateOf(MapProperties(
                isMyLocationEnabled = true,
                minZoomPreference = 1f,
                maxZoomPreference = 20f,
            )
        )
    }

    LaunchedEffect(cameraPosition) {
        cameraPosition?.let { cameraPosition ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        cameraPosition.target.latitude,
                        cameraPosition.target.longitude
                    ),
                    cameraPosition.zoom
                )
            )
        }
    }

    LaunchedEffect(cameraPositionLatLongBounds) {
        cameraPositionLatLongBounds?.let {

            val latLngBounds = LatLngBounds.builder().apply {
                it.coordinates.forEach { latLong ->
                    include(LatLng(latLong.latitude, latLong.longitude))
                }
            }.build()

            cameraPositionState.move(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, it.padding)
            )
        }
    }

    Box(Modifier.fillMaxSize()) {

        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = modifier,
            uiSettings = uiSettings,
            properties = properties,
        ) {
            markers?.forEach { marker ->
                Marker(
                    state = rememberMarkerState(
                        key = marker.key,
                        position = LatLng(marker.position.latitude, marker.position.longitude)
                    ),
                    alpha = marker.alpha,
                    title = marker.title
                )
            }

            polyLine?.let { polyLine ->
                Polyline(
                    points = List(polyLine.size) {
                        val latLong = polyLine[it]
                        LatLng(latLong.latitude, latLong.longitude)
                    },
                    color = Color(0XFF1572D5),
                    width = 16f
                )
                Polyline(
                    points = List(polyLine.size) {
                        val latLong = polyLine[it]
                        LatLng(latLong.latitude, latLong.longitude)
                    },
                    color = Color(0XFF00AFFE),
                    width = 8f
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomStart),
            horizontalAlignment = Alignment.Start
        ) {
            SwitchWithLabel(
                label = "My location",
                state = properties.isMyLocationEnabled,
                darkOnLightTextColor = properties.mapType == MapType.SATELLITE
            ) {
                uiSettings =
                    uiSettings.copy(myLocationButtonEnabled = !uiSettings.myLocationButtonEnabled)
                properties =
                    properties.copy(isMyLocationEnabled = !properties.isMyLocationEnabled)
            }
            SwitchWithLabel(
                label = "Satellite",
                state = properties.mapType == MapType.SATELLITE,
                darkOnLightTextColor = properties.mapType == MapType.SATELLITE
            ) {
                properties = properties.copy(
                    mapType = if (properties.mapType == MapType.SATELLITE)
                        MapType.NORMAL
                    else
                        MapType.SATELLITE
                )
            }
        }
    }
}

