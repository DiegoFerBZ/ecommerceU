package com.example.ecommerceu.widgets

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerState

@Composable
fun GoogleMapWithLocation() {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Cámara de mapa
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 10f)
    }
    // Crear la solicitud de permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Si el permiso es concedido, obtener la última ubicación conocida
                getLastLocation(fusedLocationClient, context,cameraPositionState)
            } else {
                // Si el permiso no es concedido, mostrar un mensaje
                Toast.makeText(context, "Permiso denegado. Habilite el acceso a la ubicación.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Comprobar el permiso en LaunchedEffect
    LaunchedEffect(Unit) {
        // Verificar si el permiso ya está concedido
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Si ya se tiene el permiso, obtener la última ubicación
                getLastLocation(fusedLocationClient, context,cameraPositionState)
            }
            else -> {
                // Si no se tiene el permiso, solicitarlo
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }



    // Google Map UI
    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(isMyLocationEnabled = true)
    )

    // Llamada a la función getLastLocation para obtener la ubicación y actualizar la cámara
    getLastLocation(fusedLocationClient, context, cameraPositionState)
}

// Función para obtener la última ubicación conocida
fun getLastLocation(
    fusedLocationClient: FusedLocationProviderClient,
    context: android.content.Context,
    cameraPositionState: CameraPositionState // Ahora pasamos cameraPositionState como parámetro
) {
    // Verificamos si el permiso ya está concedido
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
            loc?.let {
                // Si obtenemos la ubicación, actualizamos la cámara del mapa
                val latLng = LatLng(it.latitude, it.longitude)

                // Llamamos a la función para actualizar la cámara con la nueva ubicación
                updateCameraPosition(latLng, cameraPositionState) // Actualizamos la cámara
            } ?: run {
                // Si no se puede obtener la ubicación, mostrar un mensaje
                Toast.makeText(context, "No se pudo obtener la ubicación.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            // Si hubo un error en la obtención de la ubicación, mostrar un mensaje
            Toast.makeText(context, "Error al obtener la ubicación.", Toast.LENGTH_SHORT).show()
        }
    } else {
        // Si no se tiene permiso, solicitarlo
        Toast.makeText(context, "Se requiere el permiso de ubicación.", Toast.LENGTH_SHORT).show()
    }
}

// Función para actualizar la cámara del mapa con la ubicación obtenida
fun updateCameraPosition(latLng: LatLng, cameraPositionState: CameraPositionState) {
    // Actualiza la posición de la cámara para centrarla en la nueva ubicación
    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f) // Zoom de nivel 15
}