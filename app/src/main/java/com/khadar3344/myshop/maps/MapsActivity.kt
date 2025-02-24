package com.khadar3344.myshop.maps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.khadar3344.myshop.ui.theme.MyShopTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyShopTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MapContent()
                }
            }
        }
    }
}

@Composable
fun MapContent() {
    GoogleMap(
        modifier = Modifier.fillMaxSize()
    ) {
        val sydney = LatLng(-34.0, 151.0)
        val markerState = rememberMarkerState(position = sydney)
        Marker(
            state = markerState,
            title = "Marker in Sydney"
        )
    }
}
