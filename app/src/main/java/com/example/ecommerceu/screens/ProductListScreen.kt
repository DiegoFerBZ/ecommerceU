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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.ecommerceu.viewmodels.CartViewModel
import com.example.ecommerceu.viewmodels.ProductViewModel
import com.example.ecommerceu.widgets.CartDialog
import com.example.ecommerceu.widgets.ProductCard
import com.example.ecommerceu.widgets.ProductFormDialog
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(cartViewModel: CartViewModel = viewModel()) {

    val productViewModel:ProductViewModel = getViewModel()
    val products by productViewModel.products.collectAsState()

    var showCartDialog by remember { mutableStateOf(false) }
    var showAddProductDialog by remember { mutableStateOf(false) }


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

        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddProductDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add product")
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
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

        if (showAddProductDialog) {
            ProductFormDialog(onDismiss = { showAddProductDialog = false }){ newProduct ->
                productViewModel.addProduct(newProduct)
                showAddProductDialog = false
            }
        }
    }
}


