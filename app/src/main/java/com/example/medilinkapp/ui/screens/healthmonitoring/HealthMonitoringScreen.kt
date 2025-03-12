package com.example.medilinkapp.ui.screens.healthmonitoring

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class HealthMonitoringActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var _stepCount = mutableStateOf(0) // Step count

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize sensors
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Load previous total steps to calculate actual steps taken today
        previousTotalSteps = loadPreviousStepCount()

        setContent {
            HealthMonitoringScreen(navController = rememberNavController(), stepCount = _stepCount)
        }
    }

    override fun onResume() {
        super.onResume()
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        } ?: Log.e("StepTracker", "Step Counter Sensor not available!")
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = event.values[0]
            _stepCount.value = (totalSteps - previousTotalSteps).toInt() // Subtract initial steps
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for step counter
    }

    // Load previous step count (so that step count resets properly)
    private fun loadPreviousStepCount(): Float {
        val sharedPreferences = getSharedPreferences("stepCounter", Context.MODE_PRIVATE)
        return sharedPreferences.getFloat("previousTotalSteps", 0f)
    }

    // Save current step count when the app is closed (optional)
    private fun savePreviousStepCount() {
        val sharedPreferences = getSharedPreferences("stepCounter", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("previousTotalSteps", totalSteps)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        savePreviousStepCount() // Save step count when the app closes
    }
}

@Composable
fun HealthMonitoringScreen(navController: NavController, stepCount: State<Int>) {
    Column(modifier = Modifier.fillMaxSize()) {

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
            Text("Step Count: ${stepCount.value}", fontSize = 18.sp)
        }
    }
}
