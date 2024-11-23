package com.example.ecommerceu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecommerceu.screens.LoginScreen
import com.example.ecommerceu.screens.ProductListScreen
import com.example.ecommerceu.screens.RegisterScreen
import com.example.ecommerceu.ui.theme.EcommerceUTheme
import com.example.ecommerceu.viewmodels.CartViewModel
import com.example.ecommerceu.viewmodels.ProductViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin{
            androidContext(this@MainActivity)
            modules(appModule)
        }
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    EcommerceUTheme {
        val navController = rememberNavController()
        val cartViewModel: CartViewModel = viewModel()
        val context = LocalContext.current

        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginScreen(
                    context=context,
                    onLoginSuccess = {
                        navController.navigate("productList")
                    },
                    onNavigateToRegister = {
                        navController.navigate("register")
                    }
                )
            }
            composable("productList") {
                ProductListScreen(cartViewModel) // Pasar el ViewModel
            }
            composable("register") {
                RegisterScreen(onRegisterSuccess = {
                    navController.popBackStack()
                    navController.navigate("login")
                })
            }
        }
    }

}


