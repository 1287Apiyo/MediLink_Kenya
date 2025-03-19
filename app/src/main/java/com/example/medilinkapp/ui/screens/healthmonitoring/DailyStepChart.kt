package com.example.medilinkapp.ui.screens.healthmonitoring

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.material3.Text

@Composable
fun DailyStepChart(dailyData: List<Pair<String, Int>>) {
    // Capture the primary color once.
    val primaryColor = MaterialTheme.colorScheme.primary
    // Determine the maximum step count.
    val maxSteps = (dailyData.maxOfOrNull { it.second } ?: 0).toFloat()
    // Use a Box for the chart area.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // Increase height a bit for clarity.
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            if (maxSteps == 0f) return@Canvas

            // Draw horizontal grid lines.
            val gridLineCount = 5
            val gridSpacing = height / gridLineCount
            for (i in 0..gridLineCount) {
                val y = i * gridSpacing
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Calculate spacing between points. (Ensure at least 1 to avoid division by zero.)
            val pointSpacing = if (dailyData.size > 1) width / (dailyData.size - 1) else width

            // Build a path for the line chart.
            val path = Path().apply {
                dailyData.forEachIndexed { index, data ->
                    val steps = data.second.toFloat()
                    val x = index * pointSpacing
                    val y = height - (steps / maxSteps * height)
                    if (index == 0) moveTo(x, y) else lineTo(x, y)
                }
            }
            // Draw the chart line.
            drawPath(
                path = path,
                color = primaryColor,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
            // Draw circles at each data point.
            dailyData.forEachIndexed { index, data ->
                val steps = data.second.toFloat()
                val x = index * pointSpacing
                val y = height - (steps / maxSteps * height)
                drawCircle(
                    color = primaryColor,
                    radius = 6.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }
        // Draw x-axis labels at the bottom.
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dailyData.forEach { (date, _) ->
                Text(
                    text = date,
                    fontFamily = FontFamily.Serif,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
