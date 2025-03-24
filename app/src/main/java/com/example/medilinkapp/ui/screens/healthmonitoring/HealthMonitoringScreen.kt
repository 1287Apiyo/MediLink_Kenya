package com.example.medilinkapp.ui.screens.healthmonitoring

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.medilinkapp.ui.components.StepCounterSensor
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthMonitoringScreen(
    navController: NavController,
    stepCount: MutableState<Int>,
    viewModel: HealthMonitoringViewModel = viewModel()
) {
    // Use sensor to update step count.
    StepCounterSensor(stepCount = stepCount)

    // Local UI states for dialogs.
    var showStepGoalDialog by remember { mutableStateOf(false) }

    // Observe Firebase health data via the ViewModel.
    val healthData by viewModel.healthData.collectAsState()

    // Extract values safely
    val stepGoal = healthData.stepGoal ?: 0
    val waterIntake = healthData.waterIntake
    val steps = healthData.steps

    // Ensure Firebase updates when step count changes
    LaunchedEffect(stepCount.value) {
        viewModel.updateSteps(stepCount.value)
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
            // Header Section.
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Daily Health Summary",
                    fontFamily = FontFamily.Serif,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Keep up the great work!",
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Divider(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primary)
            }

            // Row with Steps and Water Intake Cards side by side.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetricCard(
                    label = "Steps",
                    value = stepCount.value.toString(),
                    unit = "",
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.DirectionsWalk,
                            contentDescription = "Steps",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    extraText = if (stepGoal != 0) "Goal: $stepGoal" else "No Goal",
                    actionButton = {
                        TextButton(onClick = { showStepGoalDialog = true }) {
                            Text("Set Goal")
                        }
                    }
                )
                WaterIntakeCard(
                    waterIntake = waterIntake,
                    onAddWater = { if (waterIntake < 2000) viewModel.updateWaterIntake(waterIntake + 250) }
                )
            }
            // Congratulatory messages.
            AnimatedVisibility(
                visible = (stepGoal != 0 && stepCount.value >= stepGoal),
                enter = fadeIn()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Celebration,
                        contentDescription = "Congrats",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Congratulations! Step goal reached!", fontFamily = FontFamily.Serif, color = MaterialTheme.colorScheme.primary)
                }
            }
            AnimatedVisibility(
                visible = waterIntake >= 2000,
                enter = fadeIn()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Celebration,
                        contentDescription = "Congrats",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Congratulations! Water goal reached!", fontFamily = FontFamily.Serif, color = MaterialTheme.colorScheme.primary)
                }
            }
            // Activity Section.
            ActivitySelectionSection(navController = navController)
            // Graph Section: Bar Chart.
            Text("Daily Steps Bar Chart", fontFamily = FontFamily.Serif, fontSize = 20.sp)
            val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            val todayIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1

            // Rearrange steps data based on today
            val stepDataList = listOf(4000, 6500, 8000, 7500, 9000, 8500, stepCount.value)
            val orderedSteps = daysOfWeek.mapIndexed { index, day ->
                day to if (index == todayIndex) stepCount.value else stepDataList[index]
            }

            WeeklyStepsBarChart(stepData = orderedSteps)
        }
    }

    // Step Goal Dialog.
    if (showStepGoalDialog) {
        var tempGoal by remember { mutableStateOf(stepGoal.toString()) }
        AlertDialog(
            onDismissRequest = { showStepGoalDialog = false },
            title = { Text("Enter Daily Step Goal") },
            text = {
                OutlinedTextField(
                    value = tempGoal,
                    onValueChange = { tempGoal = it },
                    label = { Text("Step Goal") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    tempGoal.toIntOrNull()?.let { viewModel.updateStepGoal(it) }
                    showStepGoalDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStepGoalDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun WeeklyStepsBarChart(stepData: List<Pair<String, Int>>) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFEFEFEF))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val barCount = stepData.size
            val barWidth = canvasWidth / (barCount * 2f)
            val maxSteps = (stepData.maxOfOrNull { it.second } ?: 0).toFloat()
            stepData.forEachIndexed { index, (day, steps) ->
                val barHeight = if (maxSteps > 0) (steps / maxSteps) * canvasHeight else 0f
                val x = index * 2 * barWidth + barWidth / 2
                drawRect(
                    color = primaryColor,
                    topLeft = Offset(x, canvasHeight - barHeight),
                    size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            stepData.forEach { (day, _) ->
                Text(day, fontFamily = FontFamily.Serif, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun MetricCard(
    label: String,
    value: String,
    unit: String,
    icon: (@Composable () -> Unit)? = null,
    extraText: String = "",
    actionButton: (@Composable () -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(80.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                if (icon != null) { icon() }
                Text("$value $unit", fontFamily = FontFamily.Serif, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                if (extraText.isNotEmpty()) { Text(extraText, fontFamily = FontFamily.Serif, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                Text(label, fontFamily = FontFamily.Serif, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (actionButton != null) {
                Box(modifier = Modifier.align(Alignment.TopEnd)) { actionButton() }
            }
        }
    }
}

@Composable
fun WaterIntakeCard(waterIntake: Int, onAddWater: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(80.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val fillFraction = (waterIntake / 2000f).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fillFraction)
                    .background(Color.Cyan)
                    .align(Alignment.BottomCenter)
            )
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "$waterIntake mL", fontFamily = FontFamily.Serif, style = MaterialTheme.typography.titleMedium, color = Color.Black)
                Text(text = "Water Intake", fontFamily = FontFamily.Serif, style = MaterialTheme.typography.bodySmall, color = Color.Black)
            }
            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                IconButton(onClick = onAddWater) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Water", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun ActivitySelectionSection(navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Track Activity", fontFamily = FontFamily.Serif, fontSize = 20.sp)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ActivityCard(activity = "Running", icon = Icons.Filled.DirectionsRun, navController = navController)
            ActivityCard(activity = "Walking", icon = Icons.Filled.DirectionsWalk, navController = navController)
            ActivityCard(activity = "Biking", icon = Icons.Filled.DirectionsBike, navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityCard(activity: String, icon: ImageVector, navController: NavController) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .clickable { navController.navigate("map_screen?activity=${activity.lowercase()}") },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = activity, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(activity, fontFamily = FontFamily.Serif, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
