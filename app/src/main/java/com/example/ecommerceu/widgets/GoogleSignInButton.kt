package com.example.ecommerceu.widgets

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ecommerceu.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleSignInButton(
    context: Context,
    onSignInResult: (GoogleSignInAccount?) -> Unit
) {


    // Configure Google Sign-In
    val googleSignInOptions = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("781740763356-qced8thpbthavqs6ejlpun53qlgnaumk.apps.googleusercontent.com") // Replace with your Web Client ID from Firebase
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, googleSignInOptions) }

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                onSignInResult(account) // Pass the GoogleSignInAccount if successful
            } catch (e: ApiException) {
                // Log the error and handle it
                Log.e("RegisterScreen", "Google sign-in failed: ${e.localizedMessage}")
                onSignInResult(null) // Handle error by passing null
            }
        }
    )


        Box(
            modifier = Modifier.fillMaxWidth(), // Makes the Box take up all available space
            contentAlignment = Alignment.Center // Centers the Button inside the Box
        ) {
            Button(
                onClick = { signInLauncher.launch(googleSignInClient.signInIntent) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.wrapContentSize() // Ensures Button does not grow beyond content size
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_logo), // Google logo
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(24.dp) // Adjust size if necessary
                )
                Spacer(modifier = Modifier.width(8.dp)) // Space between the logo and text
                Text(text = "Iniciar con google", color = Color.Black)
            }

    }


}

fun firebaseAuthWithGoogle(idToken: String, onSignInComplete: (Boolean) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            onSignInComplete(task.isSuccessful)
        }
}
