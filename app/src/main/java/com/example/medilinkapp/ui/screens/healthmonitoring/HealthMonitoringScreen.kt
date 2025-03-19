package com.example.medilinkapp.ui.screens.healthmonitoring

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
import kotlin.math.roundToInt

// Utility function to compute sleep hours given start and end times in "HH:mm" format.
fun computeSleepHours(start: String, end: String): Float {
    val (startH, startM) = start.split(":").map { it.toIntOrNull() ?: 0 }
    val (endH, endM) = end.split(":").map { it.toIntOrNull() ?: 0 }
    val startMinutes = startH * 60 + startM
    val endMinutes = endH * 60 + endM
    val diffMinutes = if (endMinutes >= startMinutes) {
        endMinutes - startMinutes
    } else {
        (24 * 60 - startMinutes) + endMinutes
    }
    return diffMinutes / 60f
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthMonitoringScreen(navController: NavController, stepCount: MutableState<Int>) {
    // Update step count from real sensor.
    StepCounterSensor(stepCount = stepCount)

    // Mutable states for manual input of heart rate and sleep times.
    var userHeartRate by remember { mutableStateOf("") }
    var sleepStart by remember { mutableStateOf("") }   // e.g., "23:00"
    var sleepEnd by remember { mutableStateOf("") }     // e.g., "07:00"
    var computedSleepHours by remember { mutableStateOf(0f) }

    // Simulate daily step data for the past 7 days.
    val dailyStepData = remember {
        mutableStateListOf(
            "Mon" to 4000,
            "Tue" to 6500,
            "Wed" to 8000,
            "Thu" to 7500,
            "Fri" to 9000,
            "Sat" to 8500,
            "Sun" to stepCount.value  // Today's steps.
        )
    }
    LaunchedEffect(stepCount.value) {
        dailyStepData[dailyStepData.size - 1] = "Sun" to stepCount.value
    }

    // Static sample metrics for demonstration (manual heart rate and computed sleep).
    // In a real app, these might be updated by sensors or health APIs.
    val heartRate = if (userHeartRate.isNotBlank()) userHeartRate.toIntOrNull() ?: 0 else 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Health Monitoring",
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
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
            // Health Data Input Section.
            HealthDataInputSection(
                userHeartRate = userHeartRate,
                onHeartRateChange = { userHeartRate = it },
                sleepStart = sleepStart,
                onSleepStartChange = { sleepStart = it },
                sleepEnd = sleepEnd,
                onSleepEndChange = { sleepEnd = it },
                onCalculateSleep = {
                    computedSleepHours = computeSleepHours(sleepStart, sleepEnd)
                    // Clear the input fields after calculation.
                    userHeartRate = ""
                    sleepStart = ""
                    sleepEnd = ""
                }
            )
            // Display metrics.
            HealthMetricsSection(
                steps = stepCount.value,
                heartRate = heartRate,
                sleepHours = computedSleepHours
            )
            Text(
                text = "Weekly Step Count",
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            // Smooth line chart.
            SmoothStepsLineChart(stepData = dailyStepData.map { it.second })
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
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Enter Health Data",
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium
        )
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

@Composable
fun HealthMetricsSection(steps: Int, heartRate: Int, sleepHours: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MetricCard(
            label = "Steps",
            value = steps.toString(),
            unit = "",
            iconColor = Color(0xFF4CAF50)
        )
        MetricCard(
            label = "Heart Rate",
            value = heartRate.toString(),
            unit = "BPM",
            iconColor = Color.Red
        )
        MetricCard(
            label = "Sleep",
            value = String.format("%.1f", sleepHours),
            unit = "hrs",
            iconColor = Color.Blue
        )
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
            Text(
                text = "$value $unit",
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, y),
                    end = Offset(canvasWidth, y),
                    strokeWidth = 1.dp.toPx()
                )
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
                        cubicTo(
                            x1 = midX, y1 = p0.y,
                            x2 = midX, y2 = p1.y,
                            x3 = p1.x, y3 = p1.y
                        )
                    }
                }
            }

            drawPath(
                path = path,
                color = primaryColor,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )

            points.forEach { point ->
                drawCircle(
                    color = primaryColor,
                    radius = 6.dp.toPx(),
                    center = point
                )
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                Text(
                    text = day,
                    fontFamily = FontFamily.Serif,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
