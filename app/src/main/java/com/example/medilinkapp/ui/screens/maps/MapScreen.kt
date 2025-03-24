package com.example.medilinkapp.ui.screens.maps

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission", "UnrememberedMutableState")
@Composable
fun MapScreen(
    navController: NavController,
    activity: String
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as LifecycleOwner
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // A mutable list to hold route points.
    val routePoints = remember { mutableStateListOf<LatLng>() }

    // Create a camera state with a fallback default.
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(37.4221, -122.0841), 14f)
    }

    // Get initial location once.
    LaunchedEffect(Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                if (routePoints.isEmpty()) {
                    routePoints.add(latLng)
                }
                lifecycleOwner.lifecycleScope.launch {
                    try {
                        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
                    } catch (e: Exception) {
                        // Catch animation cancellations or interruptions.
                    }
                }
            }
        }
    }

    // Continuously update location every few seconds.
    LaunchedEffect(Unit) {
        while (true) {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    location?.let {
                        val newLatLng = LatLng(it.latitude, it.longitude)
                        routePoints.add(newLatLng)
                        lifecycleOwner.lifecycleScope.launch {
                            try {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(newLatLng, 14f)
                                )
                            } catch (e: Exception) {
                                // Ignore errors from interrupted animations.
                            }
                        }
                    }
                }
            delay(3000L) // Update every 3 seconds; adjust as needed.
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = true)
            ) {
                // Draw a polyline along the route.
                if (routePoints.isNotEmpty()) {
                    Polyline(
                        points = routePoints,
                        color = Color.Blue,
                        width = 5f
                    )
                }
                // Show the starting marker.
                if (routePoints.isNotEmpty()) {
                    Marker(
                        state = MarkerState(position = routePoints.first()),
                        title = "Start"
                    )
                }
                // Show a marker at the current location.
                if (routePoints.isNotEmpty()) {
                    Marker(
                        state = MarkerState(position = routePoints.last()),
                        title = "Current Location"
                    )
                }
            }
            // Back button.
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            // Activity tracking text overlay.
            Text(
                text = "Tracking: ${activity.replaceFirstChar { it.uppercase() }}",
                modifier = Modifier.align(Alignment.TopCenter),
                color = Color.Black
            )
        }
    }
}
