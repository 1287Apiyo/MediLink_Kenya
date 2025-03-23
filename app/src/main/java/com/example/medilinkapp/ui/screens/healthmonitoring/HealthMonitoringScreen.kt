package com.example.medilinkapp.ui.screens.healthmonitoring

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medilinkapp.ui.components.StepCounterSensor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// ----------------------------
// Utility Functions
// ----------------------------

/**
 * Compute sleep hours from start/end times in "HH:mm" format.
 */
fun computeSleepHours(start: String, end: String): Float {
    if (!start.contains(":") || !end.contains(":")) {
        throw IllegalArgumentException("Time must be in HH:mm format")
    }
    val startParts = start.split(":")
    val endParts = end.split(":")
    if (startParts.size < 2 || endParts.size < 2) {
        throw IllegalArgumentException("Invalid time format")
    }
    val startH = startParts[0].toIntOrNull() ?: 0
    val startM = startParts[1].toIntOrNull() ?: 0
    val endH = endParts[0].toIntOrNull() ?: 0
    val endM = endParts[1].toIntOrNull() ?: 0
    val startMinutes = startH * 60 + startM
    val endMinutes = endH * 60 + endM
    val diffMinutes = if (endMinutes >= startMinutes) {
        endMinutes - startMinutes
    } else {
        (24 * 60 - startMinutes) + endMinutes
    }
    return diffMinutes / 60f
}

/**
 * Observes cumulative health data (steps) stored in Firebase.
 * The cumulative node is expected at "users/{uid}/cumulativeHealthData".
 */
fun observeCumulativeHealthData(onDataChanged: (Int) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"
    val cumulativeRef = FirebaseDatabase.getInstance()
        .getReference("users")
        .child(userId)
        .child("cumulativeHealthData")
    cumulativeRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            // Default to 0 if not present.
            val steps = snapshot.child("steps").getValue(Int::class.java) ?: 0
            onDataChanged(steps)
        }
        override fun onCancelled(error: DatabaseError) {
            Log.w("RealtimeDB", "Failed to read cumulative data.", error.toException())
        }
    })
}

/**
 * Updates cumulative health data in Firebase.
 */
fun updateCumulativeHealthData(healthData: Map<String, Any>) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"
    val cumulativeRef = FirebaseDatabase.getInstance()
        .getReference("users")
        .child(userId)
        .child("cumulativeHealthData")
    cumulativeRef.setValue(healthData)
        .addOnSuccessListener { Log.d("RealtimeDB", "Cumulative data updated!") }
        .addOnFailureListener { error ->
            Log.w("RealtimeDB", "Error updating cumulative data", error)
        }
}

// ----------------------------
// UI Code
// ----------------------------

/**
 * HealthMonitoringScreen uses a step sensor and input fields.
 * It loads cumulative data from Firebase and computes an "effective" step count:
 * stored cumulative steps + (current sensor reading - session start baseline).
 * Clicking the "Calculate Sleep Hours" button updates Firebase with the new cumulative values.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthMonitoringScreen(navController: NavController, stepCount: MutableState<Int>) {
    // Use the sensor to update the step count.
    StepCounterSensor(stepCount = stepCount)

    // Persist user inputs.
    var userHeartRate by rememberSaveable { mutableStateOf("") }
    var sleepStart by rememberSaveable { mutableStateOf("") }
    var sleepEnd by rememberSaveable { mutableStateOf("") }
    var computedSleepHours by rememberSaveable { mutableStateOf(0f) }

    // Auto-calculate sleep hours when valid input is provided.
    LaunchedEffect(sleepStart, sleepEnd) {
        if (sleepStart.contains(":") && sleepEnd.contains(":")) {
            try {
                computedSleepHours = computeSleepHours(sleepStart, sleepEnd)
            } catch (e: Exception) {
                Log.e("HealthMonitoring", "Sleep calculation error: ${e.message}")
            }
        }
    }

    // State for cumulative steps loaded from Firebase.
    var cumulativeSteps by rememberSaveable { mutableStateOf(0) }
    // Record the sensor's step count at session start.
    var sessionStartSteps by rememberSaveable { mutableStateOf(0) }

    // On startup, observe Firebase cumulative data.
    LaunchedEffect(Unit) {
        observeCumulativeHealthData { storedSteps ->
            cumulativeSteps = storedSteps
            // Set the baseline when data loads.
            sessionStartSteps = stepCount.value
        }
    }

    // Compute effective steps.
    val effectiveSteps = cumulativeSteps + (stepCount.value - sessionStartSteps)

    // For the graph, update "today's" step count.
    val dailyStepData = remember {
        mutableStateListOf(
            "Mon" to 4000,
            "Tue" to 6500,
            "Wed" to 8000,
            "Thu" to 7500,
            "Fri" to 9000,
            "Sat" to 8500,
            "Sun" to effectiveSteps
        )
    }
    LaunchedEffect(effectiveSteps) {
        dailyStepData[dailyStepData.size - 1] = "Sun" to effectiveSteps
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Health Monitoring", fontFamily = FontFamily.Serif, color = MaterialTheme.colorScheme.onPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Input Section
            HealthDataInputSection(
                userHeartRate = userHeartRate,
                onHeartRateChange = { userHeartRate = it },
                sleepStart = sleepStart,
                onSleepStartChange = { sleepStart = it },
                sleepEnd = sleepEnd,
                onSleepEndChange = { sleepEnd = it },
                onCalculateSleep = {
                    // Validate sleep input.
                    if (sleepStart.isBlank() || sleepEnd.isBlank() || !sleepStart.contains(":") || !sleepEnd.contains(":")) {
                        Log.w("HealthMonitoring", "Invalid sleep input.")
                        return@HealthDataInputSection
                    }
                    try {
                        computedSleepHours = computeSleepHours(sleepStart, sleepEnd)
                        // Convert heart rate.
                        val heartRateInt = userHeartRate.toIntOrNull() ?: 0
                        // Update cumulative data in Firebase.
                        // Here we add the new steps from this session.
                        val sessionIncrement = stepCount.value - sessionStartSteps
                        val newCumulativeSteps = cumulativeSteps + sessionIncrement
                        updateCumulativeHealthData(
                            mapOf(
                                "steps" to newCumulativeSteps,
                                "heartRate" to heartRateInt,
                                "sleepHours" to computedSleepHours,
                                "timestamp" to System.currentTimeMillis()
                            )
                        )
                        // After updating, reset the session baseline.
                        sessionStartSteps = stepCount.value
                    } catch (e: Exception) {
                        Log.e("HealthMonitoring", "Error: ${e.message}")
                    }
                }
            )
            // Display cumulative data in the cards.
            HealthMetricsSection(
                steps = effectiveSteps,
                heartRate = userHeartRate.toIntOrNull() ?: 0,
                sleepHours = computedSleepHours
            )
            Text(
                "Weekly Step Count",
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            SmoothStepsLineChart(stepData = dailyStepData.map { it.second })
            // (The realtime list has been removed.)
        }
    }
}

/**
 * Input section for heart rate and sleep times.
 * Includes a button to calculate sleep hours and update Firebase.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthDataInputSection(
    userHeartRate: String,
    onHeartRateChange: (String) -> Unit,
    sleepStart: String,
    onSleepStartChange: (String) -> Unit,
    sleepEnd: String,
    onSleepEndChange: (String) -> Unit,
    onCalculateSleep: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Enter Health Data", fontFamily = FontFamily.Serif, style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = userHeartRate,
            onValueChange = onHeartRateChange,
            label = { Text("Heart Rate (BPM)", fontFamily = FontFamily.Serif) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = sleepStart,
            onValueChange = onSleepStartChange,
            label = { Text("Sleep Start Time (HH:mm)", fontFamily = FontFamily.Serif) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = sleepEnd,
            onValueChange = onSleepEndChange,
            label = { Text("Wake Up Time (HH:mm)", fontFamily = FontFamily.Serif) },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onCalculateSleep,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Calculate Sleep Hours", fontFamily = FontFamily.Serif)
        }
    }
}

/**
 * Displays the health metrics (steps, heart rate, sleep hours) in cards.
 */
@Composable
fun HealthMetricsSection(steps: Int, heartRate: Int, sleepHours: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MetricCard(label = "Steps", value = steps.toString(), unit = "", iconColor = Color(0xFF4CAF50))
        MetricCard(label = "Heart Rate", value = heartRate.toString(), unit = "BPM", iconColor = Color.Red)
        MetricCard(label = "Sleep", value = String.format("%.1f", sleepHours), unit = "hrs", iconColor = Color.Blue)
    }
}

/**
 * A single metric card.
 */
@Composable
fun MetricCard(label: String, value: String, unit: String, iconColor: Color) {
    Card(
        modifier = Modifier.size(width = 100.dp, height = 100.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$value $unit", fontFamily = FontFamily.Serif, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontFamily = FontFamily.Serif, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/**
 * Draws a smooth line chart for weekly steps.
 */
@Composable
fun SmoothStepsLineChart(stepData: List<Int>) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val maxSteps = (stepData.maxOrNull() ?: 0).toFloat()
            if (maxSteps == 0f) return@Canvas
            // Draw grid lines.
            val gridLineCount = 5
            val gridSpacing = canvasHeight / gridLineCount
            for (i in 0..gridLineCount) {
                val y = i * gridSpacing
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, y),
                    end = Offset(canvasWidth, y),
                    strokeWidth = 1.dp.toPx()
                )
            }
            // Create chart points.
            val pointSpacing = canvasWidth / (stepData.size - 1)
            val points = stepData.mapIndexed { index, value ->
                Offset(x = index * pointSpacing, y = canvasHeight - (value / maxSteps * canvasHeight))
            }
            val path = Path().apply {
                if (points.isNotEmpty()) {
                    moveTo(points.first().x, points.first().y)
                    for (i in 0 until points.size - 1) {
                        val p0 = points[i]
                        val p1 = points[i + 1]
                        val midX = (p0.x + p1.x) / 2
                        cubicTo(x1 = midX, y1 = p0.y, x2 = midX, y2 = p1.y, x3 = p1.x, y3 = p1.y)
                    }
                }
            }
            drawPath(path = path, color = primaryColor, style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round))
            points.forEach { point ->
                drawCircle(color = primaryColor, radius = 6.dp.toPx(), center = point)
            }
        }
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                Text(text = day, fontFamily = FontFamily.Serif, fontSize = 10.sp, color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}
