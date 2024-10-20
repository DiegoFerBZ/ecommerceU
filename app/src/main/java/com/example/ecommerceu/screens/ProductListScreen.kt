package com.example.ecommerceu.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommerceu.viewmodels.CartViewModel
import com.example.ecommerceu.R

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
                title = { Text("Lista de Productos") },
                actions = {
                    IconButton(onClick = { showCartDialog = true }) {
                        Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                    }
                }
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
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Carrito de Compras") },
        text = {
            Column {
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

            Button(onClick = { cartViewModel.addToCart(product)  }) {
                Text("Agregar al carrito")
            }
        }
    }
}