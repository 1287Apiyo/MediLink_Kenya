package com.example.medilinkapp.ui.screens.appointmenthistory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medilinkapp.ui.theme.MedilinkAppTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign

// Sample data for appointment history
data class Appointment(val doctorName: String, val date: String, val time: String, val status: String)

val upcomingAppointments = listOf(
    Appointment("Dr. John Doe", "March 10, 2025", "10:00 AM", "Upcoming"),
    Appointment("Dr. Jane Smith", "March 12, 2025", "2:00 PM", "Upcoming")
)

val pastAppointments = listOf(
    Appointment("Dr. Emily White", "Feb 15, 2025", "11:30 AM", "Completed"),
    Appointment("Dr. Mark Brown", "Feb 20, 2025", "4:00 PM", "Completed")
)

@Composable
fun AppointmentHistoryScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf("Upcoming") }

    MedilinkAppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    "Appointment History",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 24.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Toggle Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Upcoming",
                    modifier = Modifier
                        .clickable { selectedTab = "Upcoming" }
                        .padding(8.dp),
                    color = if (selectedTab == "Upcoming") MaterialTheme.colorScheme.primary else Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Past",
                    modifier = Modifier
                        .clickable { selectedTab = "Past" }
                        .padding(8.dp),
                    color = if (selectedTab == "Past") MaterialTheme.colorScheme.primary else Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(if (selectedTab == "Upcoming") upcomingAppointments else pastAppointments) { appointment ->
                    AppointmentCard(appointment)
                }
            }
        }
    }
}

@Composable
fun AppointmentCard(appointment: Appointment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = appointment.doctorName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Date: ${appointment.date}", style = MaterialTheme.typography.bodyMedium)
            Text("Time: ${appointment.time}", style = MaterialTheme.typography.bodyMedium)
            Text("Status: ${appointment.status}", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewAppointmentHistoryScreen() {
    AppointmentHistoryScreen(navController = rememberNavController())
}