package com.khadar3344.myshop.ui.maps

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {
    private val _markers = mutableListOf<LatLng>()
    val markers: List<LatLng> get() = _markers

    fun addMarker(location: LatLng) {
        _markers.add(location)
    }

    fun clearMarkers() {
        _markers.clear()
    }
}
