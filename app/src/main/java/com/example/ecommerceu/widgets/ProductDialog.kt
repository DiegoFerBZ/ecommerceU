package com.example.ecommerceu.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ecommerceu.R
import com.example.ecommerceu.data.entities.Product

@Composable
fun ProductFormDialog(
    onDismiss: () -> Unit, // Función para cerrar el formulario
    onSubmit: (Product) -> Unit // Función para enviar los datos del formulario
) {
    // Estado para cada uno de los campos
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    // Función para manejar el envío del formulario
    fun submitForm() {
        if (title.isNotEmpty() && description.isNotEmpty() && imageUrl.isNotEmpty() && price.isNotEmpty()) {
            val product = Product(
                title = title,
                description = description,
                imgUri = imageUrl,
                price = price.toDouble() // Convierte el precio a Double
            )
            onSubmit(product)
        }
    }

    // Dialogo flotante para agregar el producto
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Agregar Producto") },
        text = {
            Column(modifier = Modifier.padding(16.dp)) {
                // Título del producto
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Descripción
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Enlace de imagen
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Enlace de Imagen") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Precio
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        confirmButton = {
            Button(onClick = { submitForm() }) {
                Text("Agregar Producto")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}
