package com.example.ecommerceu.widgets

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.MapProperties

@Composable
fun GoogleMapWithLocation() {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 10f)
    }

    LaunchedEffect(Unit) {
        getLastLocation(fusedLocationClient, context, cameraPositionState)
    }

    // UI del mapa
    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(isMyLocationEnabled = true)
    )
}

fun getLastLocation(
    fusedLocationClient: FusedLocationProviderClient,
    context: Context,
    cameraPositionState: CameraPositionState
) {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                updateCameraPosition(latLng, cameraPositionState)
            } ?: Toast.makeText(context, "No se pudo obtener la ubicación.", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Error al obtener la ubicación.", Toast.LENGTH_SHORT).show()
        }
    }
}

fun updateCameraPosition(latLng: LatLng, cameraPositionState: CameraPositionState) {
    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
}