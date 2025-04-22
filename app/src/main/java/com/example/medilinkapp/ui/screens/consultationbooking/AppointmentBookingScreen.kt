package com.example.medilinkapp.ui.screens.consultationbooking

import android.app.TimePickerDialog
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentBookingScreen(navController: NavController, doctorName: String) {
    val firestore = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var appointmentDate by remember { mutableStateOf("") }
    var appointmentTime by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isBooking by remember { mutableStateOf(false) }
    var bookingMessage by remember { mutableStateOf("") }
    var showSuccessAnimation by remember { mutableStateOf(false) }
    var doctorImageUrl by remember { mutableStateOf<String?>(null) }

    var shouldSubmitBooking by remember { mutableStateOf(false) }

    // Fetch doctor image from Firestore
    LaunchedEffect(doctorName) {
        firestore.collection("doctors")
            .whereEqualTo("name", doctorName)
            .get()
            .addOnSuccessListener { docs ->
                if (!docs.isEmpty) {
                    doctorImageUrl = docs.first()["imageUrl"] as? String
                }
            }
    }

    // Date & Time pickers
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day -> appointmentDate = "$day/${month + 1}/$year" },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute -> appointmentTime = String.format("%02d:%02d", hour, minute) },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    val loadingAnim by rememberLottieComposition(LottieCompositionSpec.Asset("loading_animation.json"))
    val successAnim by rememberLottieComposition(LottieCompositionSpec.Asset("success_check.json"))

    // Handle booking logic with coroutine
    LaunchedEffect(shouldSubmitBooking) {
        if (shouldSubmitBooking) {
            isBooking = true
            bookingMessage = ""
            delay(1000)

            val appointmentData = mapOf(
                "doctorName" to doctorName,
                "appointmentDate" to appointmentDate,
                "appointmentTime" to appointmentTime,
                "notes" to notes,
                "status" to "upcoming",
                "timestamp" to System.currentTimeMillis()
            )
            firestore.collection("appointments")
                .add(appointmentData)
                .addOnSuccessListener {
                    showSuccessAnimation = true
                    bookingMessage = "Appointment booked!"
                }
                .addOnFailureListener {
                    bookingMessage = "Booking failed."
                }
                .addOnCompleteListener {
                    isBooking = false
                }
            shouldSubmitBooking = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Appointment Booking", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A237E))
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text("Book an Appointment", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("with  $doctorName", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(24.dp))

                Text("Select Date", fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = appointmentDate,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() },
                    enabled = false,
                    placeholder = { Text("Tap to pick date") },
                    trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Select Time", fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = appointmentTime,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { timePickerDialog.show() },
                    enabled = false,
                    placeholder = { Text("Tap to pick time") },
                    trailingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    placeholder = { Text("Additional notes...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (appointmentDate.isNotBlank() && appointmentTime.isNotBlank()) {
                            shouldSubmitBooking = true
                        } else {
                            bookingMessage = "Please select date and time."
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
                ) {
                    Text("Book Appointment", color = Color.White)
                }

                if (bookingMessage.isNotBlank()) {
                    Text(
                        text = bookingMessage,
                        color = if (bookingMessage.contains("failed")) Color.Red else Color(0xFF2E7D32),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            if (isBooking) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .zIndex(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Booking appointment...", color = Color.White)
                    }
                }
            }


            if (showSuccessAnimation) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .zIndex(2f),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(
                        composition = successAnim,
                        iterations = 1,
                        modifier = Modifier.size(180.dp)
                    )
                    Text(
                        text = "Appointment Booking  Successful!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                LaunchedEffect(true) {
                    delay(2000)
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            }
        }
    }
}
