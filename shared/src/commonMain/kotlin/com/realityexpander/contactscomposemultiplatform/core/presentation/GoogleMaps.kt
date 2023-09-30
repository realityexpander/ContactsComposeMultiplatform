package com.realityexpander.contactscomposemultiplatform.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class LatLong(val latitude: Double = 0.0, val longitude: Double = 0.0)
class MapMarker(
    val key: String = "",
    val position: LatLong = LatLong(0.0, 0.0),
    val title: String = "",
    val alpha: Float = 1.0f
)
class CameraPositionLatLongBounds(
    val coordinates: List<LatLong> = listOf(),
    val padding: Int = 0
)

class CameraPosition(
    val target: LatLong = LatLong(0.0, 0.0),
    val zoom: Float = 0f
)

@Composable
expect fun GoogleMaps(
    modifier: Modifier,
    isControlsVisible: Boolean = true,
    onMarkerClick: ((MapMarker) -> Unit)? = {},
    onMapClick: ((LatLong) -> Unit)? = {},
    onMapLongClick: ((LatLong) -> Unit)? = {},
    markers: List<MapMarker>? = null,
    cameraPosition: CameraPosition? = null,
    cameraPositionLatLongBounds: CameraPositionLatLongBounds? = null,
    polyLine: List<LatLong>? = null,
)


//onMarkerClick: (MapMarker) -> Unit = {},
//onMapClick: (LatLong) -> Unit = {},
//onMapLongClick: (LatLong) -> Unit = {},
