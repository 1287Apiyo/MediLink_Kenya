package com.example.medilinkapp.ui.components

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StepCounterSensor(stepCount: MutableState<Int>) {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    // Formatter for date in "yyyyMMdd" format.
    val dateFormat = remember { SimpleDateFormat("yyyyMMdd", Locale.getDefault()) }
    // Store current date and baseline for today.
    var currentDate by remember { mutableStateOf(dateFormat.format(Date())) }
    var baseline by remember { mutableStateOf(0f) }

    DisposableEffect(stepSensor) {
        val sensorListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { /* no op */ }
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    // Get today's date.
                    val today = dateFormat.format(Date())
                    // If the date has changed, reset baseline.
                    if (today != currentDate) {
                        currentDate = today
                        baseline = it.values[0]
                    }
                    // If baseline is zero, set it.
                    if (baseline == 0f) {
                        baseline = it.values[0]
                    }
                    // Update stepCount with the difference.
                    stepCount.value = (it.values[0] - baseline).toInt()
                }
            }
        }
        stepSensor?.also { sensor ->
            sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }
}
