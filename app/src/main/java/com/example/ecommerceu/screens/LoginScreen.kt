package com.example.ecommerceu.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceu.viewmodels.CustomerViewModel
import com.example.ecommerceu.widgets.GoogleSignInButton
import com.example.ecommerceu.widgets.firebaseAuthWithGoogle
import org.koin.androidx.compose.getViewModel


@Composable
fun LoginScreen(context: Context, onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit) {

    val viewModel: CustomerViewModel = getViewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(viewModel.loginSuccess) {
        viewModel.loginSuccess?.let { success ->
            if (success) {
                onLoginSuccess()
            } else {
                errorMessage = "Credenciales invalidas"
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "EcommerceU",
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.login(email, password)
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))

            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            GoogleSignInButton(
                context = context,
                onSignInResult = { account ->
                    account?.idToken?.let { idToken ->
                        firebaseAuthWithGoogle(idToken) { isSuccess ->
                            onLoginSuccess()
                        }
                    }
                }
            )

            HorizontalDivider(modifier = Modifier.padding(top = 10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp), // Espaciado superior
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¿No estás registrado? Haz clic aquí para registrarte.",
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable(onClick = onNavigateToRegister)
                        .padding(top = 8.dp)
                )
            }


        }
    }
}

