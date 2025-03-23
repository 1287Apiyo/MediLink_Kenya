package com.example.medilinkapp.ui.screens.prescriptions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medilinkapp.model.Prescription
import com.example.medilinkapp.repository.FirestoreRepository
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Share

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionsScreen(navController: NavController) {
    val repository = FirestoreRepository()
    var prescriptions by remember { mutableStateOf(emptyList<Prescription>()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun loadPrescriptions() {
        coroutineScope.launch {
            try {
                prescriptions = repository.getPrescriptions()
                error = null
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) { loadPrescriptions() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Prescriptions", fontSize = 22.sp, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A237E))
            )
        }
    ) { paddingValues ->
        // Use a subtle gradient background for a modern look
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFF5F5F5), Color(0xFFE0E0E0))
                    )
                )
        ) {
            when {
                isLoading -> {
                    // Centered loading indicator with text
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color(0xFF1A237E))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Loading prescriptions...", style = MaterialTheme.typography.bodySmall)
                    }
                }
                error != null -> {
                    // Error state with retry button
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Error: $error",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = {
                            isLoading = true
                            loadPrescriptions()
                        }) {
                            Text("Retry")
                        }
                    }
                }
                prescriptions.isEmpty() -> {
                    // Empty state message
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No prescriptions available.", style = MaterialTheme.typography.bodySmall)
                    }
                }
                else -> {
                    // Prescription list with a LazyColumn and animated visibility for a smooth appearance
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(prescriptions) { prescription ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                PrescriptionCard(prescription)
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun PrescriptionCard(prescription: Prescription) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Prescription from Dr. ${prescription.doctorName}",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF1A237E)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Date: ${prescription.date}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Medications: ${prescription.medications.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium
            )
            // Add a spacer to bring the buttons a bit closer to the content
            Spacer(modifier = Modifier.height(16.dp))
            // Row for action buttons, centered horizontally and vertically
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        // TODO: Implement download logic here
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.FileDownload,
                        contentDescription = "Download Prescription",
                        tint = Color(0xFF1A237E)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(
                    onClick = {
                        // TODO: Implement share logic here
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share Prescription",
                        tint = Color(0xFF1A237E)
                    )
                }
            }
        }
    }
}