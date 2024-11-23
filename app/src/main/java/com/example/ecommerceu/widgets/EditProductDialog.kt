package com.example.ecommerceu.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.ecommerceu.data.entities.Product

@Composable
fun EditProductAlertDialog(
    product: Product,
    onDismiss: () -> Unit,
    onUpdate: (Product) -> Unit,
    onDelete: ((Product) -> Unit)? = null
) {
    var name by remember { mutableStateOf(TextFieldValue(product.title)) }
    var price by remember { mutableStateOf(TextFieldValue(product.price.toString())) }
    var description by remember { mutableStateOf(TextFieldValue(product.description ?: "")) }

    // Function to update the product
    fun updateProduct() {
        val updatedProduct = Product(
            id = product.id,
            title = name.text,
            description = description.text,
            imgUri = product.imgUri,
            price = price.text.toDouble()
        )
        onUpdate(updatedProduct)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar producto") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Edit Name
                TextField(
                    value = name,
                    onValueChange = { name = it }, // Update name
                    label = { Text("Titulo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Edit Price
                TextField(
                    value = price,
                    onValueChange = { price = it }, // Update price
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Edit Description
                TextField(
                    value = description,
                    onValueChange = { description = it }, // Update description
                    label = { Text("Descripcion") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(10.dp))




                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween // Space out the buttons
                ) {
                    if (onDelete != null) {
                        Button(
                            onClick = { onDelete(product) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Eliminar", color = Color.White)
                        }
                    }
                    // Update Button
                    Button(onClick = { updateProduct() }) {
                        Text("Actualizar producto")
                    }
                }
            }

        },


        confirmButton = {

        },
        dismissButton = {

        }
    )
}
