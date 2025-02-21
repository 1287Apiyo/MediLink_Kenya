package com.example.medilinkapp.ui.screens.appointmenthistory


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medilinkapp.ui.theme.MedilinkAppTheme

// Sample data for appointment history
data class Appointment(val doctorName: String, val date: String, val status: String)

@Composable
fun AppointmentHistoryScreen(navController: NavController) {
    Text("Appointment Settings - Coming Soon")
}