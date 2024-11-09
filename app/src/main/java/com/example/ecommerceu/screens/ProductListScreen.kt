package com.example.ecommerceu.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommerceu.viewmodels.CartViewModel
import com.example.ecommerceu.R
import com.example.ecommerceu.widgets.GoogleMapWithLocation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(cartViewModel: CartViewModel = viewModel()) {
    val products = listOf(
        Product("Producto 1", R.drawable.box),
        Product("Producto 2", R.drawable.box),
        Product("Producto 3", R.drawable.box),
        Product("Producto 4", R.drawable.box)
    )

    var showCartDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text="Lista de Productos", color = Color.White) },
                actions = {
                    IconButton(onClick = { showCartDialog = true }) {
                        Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "Carrito", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )

            )

        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products.size) { index ->
                ProductCard(product = products[index], cartViewModel = cartViewModel)
            }
        }

        if (showCartDialog) {
            CartDialog(cartViewModel) { showCartDialog = false }
        }
    }
}

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
                        }
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

data class Product(val title: String, val imageRes: Int)

@Composable
fun ProductCard(product: Product, cartViewModel: CartViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.title,
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { cartViewModel.addToCart(product)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary)) {
                Text("Agregar al carrito")
            }
        }
    }
}