package com.khadar3344.myshop.ui.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.khadar3344.myshop.ui.theme.MyShopTheme

@Composable
fun MapScreen(viewModel: MapViewModel) {
    val markers = viewModel.markers
    val sydney = remember { LatLng(-34.0, 151.0) }
    val cameraPositionState = rememberCameraPositionState()
    cameraPositionState.position = CameraPosition.Builder()
        .target(sydney)
        .zoom(10f)
        .build()

    MyShopTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                markers.forEach { markerPosition ->
                    val state = rememberMarkerState(position = markerPosition)
                    Marker(
                        state = state,
                        title = "Marker at ${markerPosition.latitude}, ${markerPosition.longitude}"
                    )
                }
            }
        }
    }
}
