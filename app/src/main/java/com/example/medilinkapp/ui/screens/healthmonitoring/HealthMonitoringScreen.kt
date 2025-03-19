package com.example.medilinkapp.ui.screens.healthmonitoring

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import kotlinx.coroutines.delay
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthMonitoringScreen(navController: NavController, stepCount: MutableState<Int>) {
    // Simulate automatic updates to stepCount every 2 seconds.
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000L)
            // Simulate a random step increment between 100 and 500.
            stepCount.value += Random.nextInt(100, 500)
        }
    }

    // Sample data for the line chart: steps for the past 7 days.
    // For simplicity, we use fixed sample values for days 1-6 and the current stepCount for day 7.
    val sampleStepData = listOf(4000, 6500, 8000, 7500, 9000, 8500, stepCount.value)
    // Sample static metrics.
    val heartRate = 72  // BPM.
    val sleepHours = 7.5f

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
        // Main content.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HealthMetricsSection(
                steps = stepCount.value,
                heartRate = heartRate,
                sleepHours = sleepHours
            )
            Text(
                text = "Weekly Step Count",
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            StepsLineChart(stepData = sampleStepData)
            // No manual update button, as step count updates automatically.
        }
    }
}

@Composable
fun HealthMetricsSection(steps: Int, heartRate: Int, sleepHours: Float) {
    // Display basic health metrics in a row.
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
            value = sleepHours.toString(),
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
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
fun StepsLineChart(stepData: List<Int>) {
    // Capture the primary color outside the Canvas lambda.
    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp) // Slightly taller for grid lines.
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val maxSteps = (stepData.maxOrNull() ?: 0).toFloat()
            if (maxSteps == 0f) return@Canvas

            // Draw horizontal grid lines.
            val gridLineCount = 5
            val gridLineSpacing = canvasHeight / gridLineCount
            for (i in 0..gridLineCount) {
                val y = i * gridLineSpacing
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, y),
                    end = Offset(canvasWidth, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Calculate spacing between data points.
            val pointSpacing = canvasWidth / (stepData.size - 1)

            // Create a path for the line chart.
            val path = Path().apply {
                moveTo(0f, canvasHeight - (stepData[0] / maxSteps * canvasHeight))
                stepData.forEachIndexed { index, steps ->
                    val x = index * pointSpacing
                    val y = canvasHeight - (steps / maxSteps * canvasHeight)
                    if (index == 0) {
                        moveTo(x, y)
                    } else {
                        lineTo(x, y)
                    }
                }
            }

            // Draw the line chart path.
            drawPath(
                path = path,
                color = primaryColor,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )

            // Draw circles at each data point.
            stepData.forEachIndexed { index, steps ->
                val x = index * pointSpacing
                val y = canvasHeight - (steps / maxSteps * canvasHeight)
                drawCircle(
                    color = primaryColor,
                    radius = 6.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }
    }
}
