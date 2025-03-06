package com.example.medilinkapp.ui.screens.healthmonitoring

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.result.DataReadResponse
import kotlinx.coroutines.launch

@Composable
fun HealthMonitoringScreen(navController: NavController, context: Context) {
    val activity = context as Activity
    val coroutineScope = rememberCoroutineScope()
    var stepCount by remember { mutableStateOf(0) }
    var waterIntake by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            authorizeGoogleFit(activity) { authorized ->
                if (authorized) {
                    fetchStepCount(activity) { steps -> stepCount = steps }
                    fetchWaterIntake(activity) { water -> waterIntake = water }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(MaterialTheme.colorScheme.primary)
                .padding(24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                "Health Monitoring",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.White,
                    fontSize = 26.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Display Health Data
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Step Count: $stepCount", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Water Intake: $waterIntake L", fontSize = 18.sp)
        }
    }
}

fun authorizeGoogleFit(activity: Activity, onResult: (Boolean) -> Unit) {
    val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken("YOUR_WEB_CLIENT_ID_HERE") // Add your OAuth 2.0 Web Client ID
        .requestScopes(
            com.google.android.gms.fitness.Fitness.SCOPE_ACTIVITY_READ,
            com.google.android.gms.fitness.Fitness.SCOPE_NUTRITION_READ
        )
        .build()

    val googleSignInClient = GoogleSignIn.getClient(activity, signInOptions)
    val account = GoogleSignIn.getLastSignedInAccount(activity)

    if (account == null) {
        activity.startActivityForResult(googleSignInClient.signInIntent, 1001)
    } else {
        onResult(true)
    }
}

fun fetchStepCount(activity: Activity, onResult: (Int) -> Unit) {
    val fitnessClient = Fitness.getHistoryClient(activity, GoogleSignIn.getLastSignedInAccount(activity)!!)
    fitnessClient.readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
        .addOnSuccessListener { dataSet ->
            val steps = dataSet.dataPoints.firstOrNull()?.getValue(DataType.TYPE_STEP_COUNT_DELTA.fields[0])?.asInt() ?: 0
            onResult(steps)
        }
        .addOnFailureListener { e -> Log.e("HealthMonitoring", "Failed to fetch steps", e) }
}

fun fetchWaterIntake(activity: Activity, onResult: (Double) -> Unit) {
    val fitnessClient = Fitness.getHistoryClient(activity, GoogleSignIn.getLastSignedInAccount(activity)!!)
    fitnessClient.readDailyTotal(DataType.TYPE_HYDRATION)
        .addOnSuccessListener { dataSet ->
            val water = dataSet.dataPoints.firstOrNull()?.getValue(DataType.TYPE_HYDRATION.fields[0])?.asFloat()?.toDouble() ?: 0.0
            onResult(water)
        }
        .addOnFailureListener { e -> Log.e("HealthMonitoring", "Failed to fetch water intake", e) }
}

@Preview(showBackground = true)
@Composable
fun PreviewHealthMonitoringScreen() {
    HealthMonitoringScreen(navController = rememberNavController(), context = rememberNavController().context as Activity)
}