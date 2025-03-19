package com.example.medilinkapp.ui.screens.consultationbooking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentBookingScreen(navController: NavController, doctorName: String) {
    val firestore = FirebaseFirestore.getInstance()
    var appointmentDate by remember { mutableStateOf("") }
    var appointmentTime by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isBooking by remember { mutableStateOf(false) }
    var bookingMessage by remember { mutableStateOf("") }

    val context = LocalContext.current

    // If the booking message indicates success, wait 2 seconds and navigate back to the Dashboard.
    if (bookingMessage.startsWith("Appointment booked successfully!")) {
        LaunchedEffect(bookingMessage) {
            delay(2000)
            navController.navigate("dashboard") {
                popUpTo("dashboard") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Book Appointment with Dr. $doctorName",
                        fontFamily = FontFamily.Serif
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        // Wrapping content in a vertical scroll so that the keyboard does not hide content.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(), // Ensures space for the keyboard
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Appointment Date Input
            OutlinedTextField(
                value = appointmentDate,
                onValueChange = { appointmentDate = it },
                label = { Text("Appointment Date (dd/MM/yyyy)", fontFamily = FontFamily.Serif) },
                placeholder = { Text("e.g., 25/12/2025", fontFamily = FontFamily.Serif) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Appointment Time Input
            OutlinedTextField(
                value = appointmentTime,
                onValueChange = { appointmentTime = it },
                label = { Text("Appointment Time (HH:mm)", fontFamily = FontFamily.Serif) },
                placeholder = { Text("e.g., 14:30", fontFamily = FontFamily.Serif) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Optional Notes Input
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Additional Notes", fontFamily = FontFamily.Serif) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Confirm Booking Button
            Button(
                onClick = {
                    if (appointmentDate.isNotBlank() && appointmentTime.isNotBlank()) {
                        isBooking = true
                        // Create appointment data with an extra "status" field for upcoming appointments.
                        val appointmentData = mapOf(
                            "doctorName" to doctorName,
                            "appointmentDate" to appointmentDate,
                            "appointmentTime" to appointmentTime,
                            "notes" to notes,
                            "timestamp" to System.currentTimeMillis(),
                            "status" to "upcoming"
                        )
                        firestore.collection("appointments")
                            .add(appointmentData)
                            .addOnSuccessListener {
                                bookingMessage = "Appointment booked successfully!"
                                isBooking = false
                            }
                            .addOnFailureListener { e ->
                                bookingMessage = "Error: ${e.message}"
                                isBooking = false
                            }
                    } else {
                        bookingMessage = "Please fill in the date and time."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm Booking", fontFamily = FontFamily.Serif)
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (isBooking) {
                CircularProgressIndicator()
            }
            if (bookingMessage.isNotBlank()) {
                Text(
                    text = bookingMessage,
                    fontFamily = FontFamily.Serif,
                    color = if (bookingMessage.startsWith("Error")) MaterialTheme.colorScheme.error else Color(0xFF4CAF50),
                    fontSize = 16.sp
                )
            }
        }
    }
}
