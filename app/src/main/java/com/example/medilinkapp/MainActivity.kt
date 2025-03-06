package com.example.medilinkapp

import AppNavGraph
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.example.medilinkapp.ui.theme.MedilinkAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleSignInClient = GoogleSignIn.getClient(this, getGoogleSignInOptions())

        setContent {
            MedilinkAppTheme {
                AppNavGraph(context = this)
            }
        }
    }

    private fun getGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("591032813426-2r19507obunchj7en97om43e8ltecqun.apps.googleusercontent.com")
            .requestScopes(
                com.google.android.gms.fitness.Fitness.SCOPE_ACTIVITY_READ,
                com.google.android.gms.fitness.Fitness.SCOPE_NUTRITION_READ
            )
            .build()
    }

    fun signInToGoogleFit() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            startActivityForResult(googleSignInClient.signInIntent, 1001)
        } else {
            Log.d("GoogleFit", "Already signed in: ${account.email}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                if (account != null) {
                    Log.d("GoogleFit", "Google Fit Authorization Successful! User: ${account.email}")
                }
            } catch (e: ApiException) {
                Log.e("GoogleFit", "Sign-in failed: ${e.statusCode}", e)
            }
        }
    }
}
