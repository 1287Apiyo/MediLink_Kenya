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
import androidx.compose.material.icons.filled.ArrowBack
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
    var pricePreference by remember { mutableStateOf(50f) }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage.takeIf { it.isNotEmpty() }?.let { snackbarHostState.showSnackbar(it) }
    }

    LaunchedEffect(viewModel.successMessage) {
        viewModel.successMessage.takeIf { it.isNotEmpty() }?.let {
            snackbarHostState.showSnackbar("Consultation booked successfully!")
            viewModel.resetFields()
            pricePreference = 50f
            showConfirmationDialog = true
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            SmallTopAppBar(
                title = { Text("Book a Consultation") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, tint = Color.White,contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF1A237E),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Category selection
            SectionCard(title = "Select Category") {
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
                                containerColor = Color.White,
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Consultation Type
            SectionCard(title = "Consultation Type") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Phone Consultation", "Video Consultation").forEach { method ->
                        FilterChip(
                            selected = viewModel.method == method,
                            onClick = { viewModel.method = method },
                            label = { Text(method) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White,
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Email Input
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = MaterialTheme.shapes.medium
            )

            // DateTime Picker
            DateTimeSelector(viewModel.dateTime) { viewModel.dateTime = it }

            // Price Preference
            Column {
                Text("Price Preference", fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Affordable", fontSize = 12.sp)
                    Text("Expensive", fontSize = 12.sp)
                }
                Slider(
                    value = pricePreference,
                    onValueChange = { pricePreference = it },
                    valueRange = 0f..100f,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Budget level: ${
                        when {
                            pricePreference < 33 -> "Low"
                            pricePreference < 66 -> "Medium"
                            else -> "High"
                        }
                    }",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Submit Button
            Button(
                onClick = { viewModel.submitRequest() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (viewModel.isSubmitting) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submitting...")
                } else {
                    Text("Submit Request", fontSize = 16.sp, color = Color.White)
                }
            }

            // View Consultations Button
            Button(
                onClick = { navController.navigate("view_consultations") },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("View Consultations", fontSize = 16.sp, color = Color.White)
            }
        }

        if (showConfirmationDialog && viewModel.selectedDoctor != null) {
            SummaryConfirmationDialog(
                doctorName = viewModel.selectedDoctor!!.name,
                category = viewModel.category,
                method = viewModel.method,
                email = viewModel.email,
                dateTime = viewModel.dateTime
            ) {
                showConfirmationDialog = false
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                Text(title, fontWeight = FontWeight.SemiBold)
                content()
            }
        )
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
        trailingIcon = {
            IconButton(onClick = {
                DatePickerDialog(context, { _, year, month, day ->
                    calendar.set(year, month, day)
                    TimePickerDialog(context, { _, hour, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)
                        onDateTimeSelected(
                            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.time)
                        )
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
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
        confirmButton = { TextButton(onClick = onDismiss) { Text("OK") } },
        title = { Text("Consultation Confirmed") },
        text = {
            Column {
                Text("✅ Your consultation has been successfully booked with the following details:\n")
                Text("👨‍⚕️ Doctor: $doctorName")
                Text("📧 Email: $email")
                Text("📂 Category: $category")
                Text("🧑‍💻 Method: $method")
                Text("📅 Date & Time: $dateTime")
            }
        }
    )
}
