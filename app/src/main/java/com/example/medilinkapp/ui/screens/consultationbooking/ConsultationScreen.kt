package com.example.medilinkapp.ui.screens.consultationbooking

import ConsultationViewModel
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
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
import java.util.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConsultationScreen(
    navController: NavHostController,
    viewModel: ConsultationViewModel = viewModel()
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Book a Consultation",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        // Category Selection
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select Category", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                val categories = listOf(
                    "General Health", "Mental Health", "Pediatrics",
                    "Dermatology", "Dentistry", "Gynecology"
                )
                FlowRow {
                    categories.forEach { categoryItem ->
                        FilterChip(
                            selected = (viewModel.category == categoryItem),
                            onClick = { viewModel.category = categoryItem },
                            label = { Text(categoryItem) },
                            shape = MaterialTheme.shapes.medium,
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White,
                                labelColor = Color.Black,
                                selectedContainerColor = Color(0xFF2196F3),
                                selectedLabelColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }

        // Consultation Type Selection
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Consultation Type", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf("Phone Consultation", "Video Consultation").forEach { methodItem ->
                        FilterChip(
                            selected = (viewModel.method == methodItem),
                            onClick = { viewModel.method = methodItem },
                            label = { Text(methodItem) },
                            shape = MaterialTheme.shapes.medium,
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White,
                                labelColor = Color.Black,
                                selectedContainerColor = Color(0xFF2196F3),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Email Input
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        // Date & Time Picker
        DateTimeSelector(
            dateTime = viewModel.dateTime,
            onDateTimeSelected = { viewModel.dateTime = it }
        )

        // Submit Button
        Button(
            onClick = {
                viewModel.submitRequest()
                if (viewModel.successMessage.isNotEmpty()) showConfirmationDialog = true
            },
            enabled = !viewModel.isSubmitting,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (viewModel.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.width(8.dp))
                Text("Submitting...")
            } else {
                Text("Submit Request")
            }
        }

        // Messages
        viewModel.errorMessage.takeIf { it.isNotEmpty() }?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        viewModel.successMessage.takeIf { it.isNotEmpty() }?.let {
            Text(it, color = MaterialTheme.colorScheme.primary)
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = dateTime,
            onValueChange = {},
            label = { Text("Preferred Date & Time") },
            modifier = Modifier.weight(1f),
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
                                        java.text.SimpleDateFormat(
                                            "yyyy-MM-dd HH:mm", Locale.getDefault()
                                        ).format(calendar.time)
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
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Date & Time"
                    )
                }
            }
        )
    }
}

@Composable
fun ConfirmationDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmation", fontWeight = FontWeight.Bold) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}
