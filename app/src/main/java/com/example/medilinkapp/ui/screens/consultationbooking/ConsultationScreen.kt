package com.example.medilinkapp.ui.screens.consultationbooking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.medilinkapp.viewmodel.ConsultationViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ConsultationScreen(
    navController: NavHostController,
    viewModel: ConsultationViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    // Local UI state for new Slider feature
    var pricePreference by remember { mutableStateOf(50f) }

    // Show error & success snackbars
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage.takeIf { it.isNotEmpty() }?.let { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }
    LaunchedEffect(viewModel.successMessage) {
        viewModel.successMessage.takeIf { it.isNotEmpty() }?.let {
            snackbarHostState.showSnackbar("Consultation booked successfully!")
            showConfirmationDialog = true
        }
    }

    Scaffold(
        containerColor = Color.White,  // ensure content background is white
        topBar = {
            SmallTopAppBar(
                title = { Text("Book a Consultation") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Existing UI blocks unchanged ---

            // Category selection card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Select Category", fontWeight = FontWeight.SemiBold)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(
                            "General Health", "Mental Health", "Pediatrics",
                            "Dermatology", "Dentistry", "Gynecology"
                        ).forEach { category ->
                            FilterChip(
                                selected = viewModel.category == category,
                                onClick = { viewModel.category = category },
                                label = { Text(category) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color.LightGray,
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Consultation type card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Consultation Type", fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Phone Consultation", "Video Consultation").forEach { method ->
                            FilterChip(
                                selected = viewModel.method == method,
                                onClick = { viewModel.method = method },
                                label = { Text(method) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color.LightGray,
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Email input
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = MaterialTheme.shapes.medium
            )

            // Date & time selector
            DateTimeSelector(dateTime = viewModel.dateTime) {
                viewModel.dateTime = it
            }

            // ===== New Feature: Price Preference Slider =====
            Column {
                Text("Price Preference", fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Affordable", fontSize = 12.sp)
                    Text("Expensive", fontSize = 12.sp)
                }
                Slider(
                    value = pricePreference,
                    onValueChange = { pricePreference = it },
                    valueRange = 0f..100f,
                    steps = 4,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Budget level: ${
                        when {
                            pricePreference < 33 -> "Low"
                            pricePreference < 66 -> "Medium"
                            else -> "High"
                        }
                    }",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Buttons: Reset & Submit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        // existing reset logic
                        viewModel.apply {
                            category = ""
                            method = ""
                            email = ""
                            dateTime = ""
                            errorMessage = ""
                            successMessage = ""
                        }
                        pricePreference = 50f  // also reset slider
                    },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Reset")
                }

                Button(
                    onClick = { viewModel.submitRequest() },
                    modifier = Modifier.weight(2f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    if (viewModel.isSubmitting) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Submitting...", fontSize = 16.sp)
                        }
                    } else {
                        Text("Submit Request", fontSize = 16.sp)
                    }
                }
            }
        }

        // Confirmation dialog
        if (showConfirmationDialog) {
            SummaryConfirmationDialog(
                doctorName = viewModel.selectedDoctor!!.name,
                category = viewModel.category,
                method = viewModel.method,
                email = viewModel.email,
                dateTime = viewModel.dateTime,
                onDismiss = {
                    showConfirmationDialog = false
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun DateTimeSelector(dateTime: String, onDateTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    OutlinedTextField(
        value = dateTime,
        onValueChange = {},
        label = { Text("Preferred Date & Time") },
        leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        enabled = false,
        shape = MaterialTheme.shapes.medium,
        trailingIcon = {
            IconButton(onClick = {
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        calendar.set(year, month, day)
                        TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                calendar.set(Calendar.HOUR_OF_DAY, hour)
                                calendar.set(Calendar.MINUTE, minute)
                                onDateTimeSelected(
                                    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                        .format(calendar.time)
                                )
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }
    )
}

@Composable
fun SummaryConfirmationDialog(
    doctorName: String,
    email: String,
    category: String,
    method: String,
    dateTime: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK") }
        },
        title = { Text("Consultation Confirmed") },
        text = {
            Column {
                Text("Your consultation has been successfully booked with the following details:\n")
                Text("Doctor: $doctorName")
                Text("Email: $email")
                Text("Category: $category")
                Text("Method: $method")
                Text("Date & Time: $dateTime")
            }
        }
    )
}
