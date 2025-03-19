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
import com.example.medilinkapp.ui.components.StepCounterSensor
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthMonitoringScreen(navController: NavController, stepCount: MutableState<Int>) {
    // Get real step count from sensor.
    StepCounterSensor(stepCount = stepCount)

    // Sample data for the line chart: For example, we generate a list using the current stepCount for day 7.
    val sampleStepData = listOf(4000, 6500, 8000, 7500, 9000, 8500, stepCount.value)
    // Static metrics for demonstration.
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
        // Main content with white background.
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
            // Remove manual update button when using a real sensor.
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
    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
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

            val pointSpacing = canvasWidth / (stepData.size - 1)
            val path = Path().apply {
                moveTo(0f, canvasHeight - (stepData[0] / maxSteps * canvasHeight))
                stepData.forEachIndexed { index, steps ->
                    val x = index * pointSpacing
                    val y = canvasHeight - (steps / maxSteps * canvasHeight)
                    if (index == 0) moveTo(x, y) else lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = primaryColor,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )

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
