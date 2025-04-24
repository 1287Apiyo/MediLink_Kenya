package com.example.medilinkapp.ui.screens.consultationbooking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import java.util.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConsultationScreen(
    navController: NavHostController,
    viewModel: ConsultationViewModel = viewModel()
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }

    // React to changes in successMessage
    LaunchedEffect(viewModel.successMessage) {
        if (viewModel.successMessage.isNotEmpty()) {
            showConfirmationDialog = true
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Book a Consultation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )

        // Category Selection
        Text("Select Category", fontWeight = FontWeight.SemiBold)
        val categories = listOf("General Health", "Mental Health", "Pediatrics", "Dermatology", "Dentistry", "Gynecology")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.forEach { item ->
                FilterChip(
                    selected = viewModel.category == item,
                    onClick = { viewModel.category = item },
                    label = { Text(item) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.LightGray,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Method Selection
        Text("Consultation Type", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Phone Consultation", "Video Consultation").forEach { type ->
                FilterChip(
                    selected = viewModel.method == type,
                    onClick = { viewModel.method = type },
                    label = { Text(type) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.LightGray,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Email Field
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        // Date & Time Picker
        DateTimeSelector(dateTime = viewModel.dateTime) {
            viewModel.dateTime = it
        }

        // Submit Button
        Button(
            onClick = {
                viewModel.submitRequest()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (viewModel.isSubmitting) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Submitting...")
            } else {
                Text("Submit Request")
            }
        }

        viewModel.errorMessage.takeIf { it.isNotEmpty() }?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }

    if (showConfirmationDialog) {
        ConfirmationDialog(
            message = viewModel.successMessage,
            onDismiss = {
                showConfirmationDialog = false
                navController.popBackStack()
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
        modifier = Modifier.fillMaxWidth(),
        enabled = false,
        trailingIcon = {
            IconButton(onClick = {
                DatePickerDialog(
                    context,
                    { _, y, m, d ->
                        calendar.set(y, m, d)
                        TimePickerDialog(
                            context,
                            { _, h, min ->
                                calendar.set(Calendar.HOUR_OF_DAY, h)
                                calendar.set(Calendar.MINUTE, min)
                                onDateTimeSelected(
                                    java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.time)
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
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Date & Time")
            }
        }
    )
}

@Composable
fun ConfirmationDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        title = { Text("Consultation Confirmed") },
        text = { Text(message) }
    )
}
