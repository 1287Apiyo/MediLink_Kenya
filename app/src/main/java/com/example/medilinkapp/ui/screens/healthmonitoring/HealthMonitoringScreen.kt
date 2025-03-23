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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medilinkapp.ui.components.StepCounterSensor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// Utility: Compute sleep hours (expects "HH:mm" format)
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

// Save health data to Firebase Realtime Database.
fun saveHealthDataToRealtimeDatabase(heartRate: Int, sleepHours: Float, steps: Int) {
    // Use a consistent UID (ensure anonymous auth is enabled if not signing in)
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"
    val database = FirebaseDatabase.getInstance()
    val userHealthRef = database.getReference("users").child(userId).child("healthData")

    val newRecordRef = userHealthRef.push()
    val healthData = mapOf(
        "heartRate" to heartRate,
        "sleepHours" to sleepHours,
        "steps" to steps,
        "timestamp" to System.currentTimeMillis()
    )
    newRecordRef.setValue(healthData)
        .addOnSuccessListener {
            Log.d("RealtimeDB", "Health data saved successfully!")
        }
        .addOnFailureListener { error ->
            Log.w("RealtimeDB", "Error saving health data", error)
        }
}

// Observe realtime data from Firebase.
fun observeHealthData(onDataChanged: (List<Map<String, Any>>) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"
    val database = FirebaseDatabase.getInstance()
    val healthRef = database.getReference("users").child(userId).child("healthData")

    healthRef.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
        override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
            val dataList = mutableListOf<Map<String, Any>>()
            snapshot.children.forEach { dataSnapshot ->
                val data = dataSnapshot.value as? Map<String, Any>
                if (data != null) {
                    dataList.add(data)
                }
            }
            onDataChanged(dataList)
        }
        override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
            Log.w("RealtimeDB", "Failed to read health data.", error.toException())
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthMonitoringScreen(navController: NavController, stepCount: MutableState<Int>) {
    // Update step count using a sensor.
    StepCounterSensor(stepCount = stepCount)

    // States for user input.
    var userHeartRate by remember { mutableStateOf("") }
    var sleepStart by remember { mutableStateOf("") }
    var sleepEnd by remember { mutableStateOf("") }
    var computedSleepHours by remember { mutableStateOf(0f) }

    // State to hold realtime records.
    val realtimeHealthData = remember { mutableStateListOf<Map<String, Any>>() }

    // Observe realtime data on screen start.
    LaunchedEffect(Unit) {
        observeHealthData { dataList ->
            realtimeHealthData.clear()
            realtimeHealthData.addAll(dataList)
        }
    }

    // Simulated daily step data (ensure this is updated when sensor changes).
    val dailyStepData = remember {
        mutableStateListOf(
            "Mon" to 4000,
            "Tue" to 6500,
            "Wed" to 8000,
            "Thu" to 7500,
            "Fri" to 9000,
            "Sat" to 8500,
            "Sun" to stepCount.value // Today's steps.
        )
    }
    LaunchedEffect(stepCount.value) {
        dailyStepData[dailyStepData.size - 1] = "Sun" to stepCount.value
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
            HealthDataInputSection(
                userHeartRate = userHeartRate,
                onHeartRateChange = { userHeartRate = it },
                sleepStart = sleepStart,
                onSleepStartChange = { sleepStart = it },
                sleepEnd = sleepEnd,
                onSleepEndChange = { sleepEnd = it },
                onCalculateSleep = {
                    // Validate and compute sleep hours.
                    if (sleepStart.isBlank() || sleepEnd.isBlank() || !sleepStart.contains(":") || !sleepEnd.contains(":")) {
                        Log.w("HealthMonitoring", "Invalid sleep input.")
                        return@HealthDataInputSection
                    }
                    try {
                        computedSleepHours = computeSleepHours(sleepStart, sleepEnd)
                        val heartRateInt = userHeartRate.toIntOrNull() ?: 0
                        saveHealthDataToRealtimeDatabase(heartRateInt, computedSleepHours, stepCount.value)
                    } catch (e: Exception) {
                        Log.e("HealthMonitoring", "Error: ${e.message}")
                    }
                }
            )
            HealthMetricsSection(
                steps = stepCount.value,
                heartRate = userHeartRate.toIntOrNull() ?: 0,
                sleepHours = computedSleepHours
            )
            Text("Weekly Step Count", fontFamily = FontFamily.Serif, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
            SmoothStepsLineChart(stepData = dailyStepData.map { it.second })
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            Text("Realtime Health Data Records", fontFamily = FontFamily.Serif, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            RealtimeHealthDataList(records = realtimeHealthData)
        }
    }
}

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
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
        ) {
            Text("Calculate Sleep Hours", fontFamily = FontFamily.Serif)
        }
    }
}

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

@Composable
fun SmoothStepsLineChart(stepData: List<Int>) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val maxSteps = (stepData.maxOrNull() ?: 0).toFloat()
            if (maxSteps == 0f) return@Canvas
            val gridLineCount = 5
            val gridSpacing = canvasHeight / gridLineCount
            for (i in 0..gridLineCount) {
                val y = i * gridSpacing
                drawLine(color = Color.LightGray, start = Offset(0f, y), end = Offset(canvasWidth, y), strokeWidth = 1.dp.toPx())
            }
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

@Composable
fun RealtimeHealthDataList(records: List<Map<String, Any>>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        records.forEach { record ->
            val heartRate = record["heartRate"] ?: "N/A"
            val sleepHours = record["sleepHours"] ?: "N/A"
            val steps = record["steps"] ?: "N/A"
            Text(text = "HR: $heartRate, Sleep: $sleepHours hrs, Steps: $steps", fontFamily = FontFamily.Serif, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(4.dp))
        }
    }
}
