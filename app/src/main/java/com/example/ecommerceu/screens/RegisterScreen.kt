package com.example.ecommerceu.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceu.data.entities.Customer
import com.example.ecommerceu.viewmodels.CustomerViewModel
import org.koin.androidx.compose.getViewModel



@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit) {

    val viewModel: CustomerViewModel = getViewModel()


    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var registrationSuccess by remember { mutableStateOf(false) }
    var registrationError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro de usuario",
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Name TextField
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Register Button
        Button(onClick = {
            val customer = Customer(name = name, email = email, password = password)

            // Use the ViewModel to register the customer
            viewModel.registerCustomer(customer) { registrationSuccess ->
                if (registrationSuccess) {
                    onRegisterSuccess() // Call the success callback
                } else {
                    registrationError = "Error en el registro"
                }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Registrar")
        }

        // Display registration error if any
        registrationError?.let {
            Text(text = it, color = Color.Red)
        }
    }
}
