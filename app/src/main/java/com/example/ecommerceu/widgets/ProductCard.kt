package com.example.ecommerceu.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ecommerceu.R
import com.example.ecommerceu.data.entities.Product
import com.example.ecommerceu.viewmodels.CartViewModel
import java.text.DecimalFormat

@Composable
fun ProductCard(product: Product, cartViewModel: CartViewModel,onUpdate: (Product) -> Unit,onDelete:(Product)-> Unit) {
    var isDialogOpen by remember { mutableStateOf(false) } // Track dialog visibility
    val priceFormatted = DecimalFormat("#,###").format(product.price)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Product Image (Click to open the modal)
            Image(
                painter = rememberAsyncImagePainter(
                    model = product.imgUri,
                    placeholder = painterResource(R.drawable.box),
                    error = painterResource(R.drawable.box)
                ),
                contentDescription = product.title,
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 8.dp)
                    .clickable { isDialogOpen = true } // Open dialog on image click
            )

            // Product Title
            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Styled Price
            Text(
                text = "$$priceFormatted",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Add to Cart Button
            Button(
                onClick = { cartViewModel.addToCart(product) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Agregar al carrito")
            }
        }
    }

    // Dialog to edit product information
    if (isDialogOpen) {
        EditProductAlertDialog(
            product = product, // Pass the product object for editing
            onDismiss = { isDialogOpen = false }, // Close dialog
            onUpdate = {updatedProduct ->
                onUpdate(updatedProduct)
                isDialogOpen = false
            },
            onDelete ={deletedProduct ->
                onDelete(deletedProduct)
                isDialogOpen = false
            }
        )
    }
}