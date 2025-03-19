package com.example.medilinkapp.ui.screens.appointmenthistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medilinkapp.model.Appointment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentHistoryScreen(navController: NavController) {
    val firestore = FirebaseFirestore.getInstance()
    var appointments by remember { mutableStateOf(emptyList<Appointment>()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var listenerRegistration by remember { mutableStateOf<ListenerRegistration?>(null) }

    // Listen to appointments collection (assumed to be in Firestore)
    LaunchedEffect(Unit) {
        listenerRegistration = firestore.collection("appointments")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    error = e.message
                    isLoading = false
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val list = it.documents.mapNotNull { doc ->
                        doc.toObject(Appointment::class.java)
                    }
                    appointments = list
                    isLoading = false
                }
            }
    }

    DisposableEffect(Unit) {
        onDispose { listenerRegistration?.remove() }
    }

    // Group appointments by status
    val upcomingAppointments = appointments.filter { it.status == "upcoming" }
    val pastAppointments = appointments.filter { it.status == "past" }

    // Tab state: 0 for Upcoming, 1 for Past
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Upcoming", "Past")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Appointment History",
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimary) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        // Use a white background for the entire screen.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            // Tab Row for Upcoming and Past appointments.
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontFamily = FontFamily.Serif) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            when (selectedTab) {
                0 -> {
                    if (upcomingAppointments.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No upcoming appointments.", fontFamily = FontFamily.Serif)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(upcomingAppointments) { appointment ->
                                AppointmentCard(appointment = appointment)
                            }
                        }
                    }
                }
                1 -> {
                    if (pastAppointments.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No past appointments.", fontFamily = FontFamily.Serif)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(pastAppointments) { appointment ->
                                AppointmentCard(appointment = appointment)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppointmentCard(appointment: Appointment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Dr. ${appointment.doctorName}",
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Date: ${appointment.appointmentDate}",
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Time: ${appointment.appointmentTime}",
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Notes: ${appointment.notes}",
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Status: ${appointment.status}",
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.bodyMedium,
                color = if (appointment.status == "upcoming") Color.Green else Color.Red
            )
        }
    }
}
