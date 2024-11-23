package com.example.ecommerceu.widgets

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.ecommerceu.viewmodels.CartViewModel

@Composable
fun CartDialog(cartViewModel: CartViewModel, onDismiss: () -> Unit) {
    val context = LocalContext.current

    // Estado para controlar si el permiso fue concedido
    var locationPermissionGranted by remember { mutableStateOf(false) }

    // Lanzador para la solicitud de permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            locationPermissionGranted = isGranted
            if (!isGranted) {
                Toast.makeText(
                    context,
                    "Permiso denegado. Habilite el acceso a la ubicación.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    // Comprobar el permiso en LaunchedEffect
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Carrito de Compras") },
        text = {
            Column {
                // Cart content
                if (cartViewModel.cartItems.isEmpty()) {
                    Text("El carrito está vacío.")
                } else {
                    cartViewModel.cartItems.forEach { cartItem ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(cartItem.product.title)
                            Text("Cantidad: ${cartItem.quantity}")
                            Text("Precio: ${cartItem.product.price * cartItem.quantity}")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Calculate and display the total amount
                    val totalAmount = cartViewModel.cartItems.sumOf { it.product.price * it.quantity }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End // Align total to the right
                    ) {
                        Text(
                            text = "Total: $$totalAmount",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Mostrar el mapa solo si el permiso fue concedido
                if (locationPermissionGranted) {
                    GoogleMapWithLocation()
                } else {
                    Text(
                        text = "Habilite el permiso de ubicación para ver el mapa.",
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cerrar")
            }
        }
    )
}