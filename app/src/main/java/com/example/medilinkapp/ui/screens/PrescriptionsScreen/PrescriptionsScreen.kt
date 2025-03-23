package com.example.medilinkapp.ui.screens.prescriptions

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.platform.LocalContext

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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("pharmacyScreen") },
                containerColor = Color(0xFF1A237E)
            ) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Order Medicine",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color(0xFF1A237E))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Loading prescriptions...", style = MaterialTheme.typography.bodySmall)
                    }
                }
                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Error: $error",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            isLoading = true
                            loadPrescriptions()
                        }) {
                            Text("Retry")
                        }
                    }
                }
                prescriptions.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No prescriptions available.", style = MaterialTheme.typography.bodySmall)
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(prescriptions) { prescription ->
                            PrescriptionCard(prescription)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun PrescriptionCard(prescription: Prescription) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Doctor's name at the top
            Text(
                text = "Prescription from Dr. ${prescription.doctorName}",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF1A237E)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Row containing the Date on the left and action buttons on the right
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Date: ${prescription.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        Toast.makeText(context, "Downloading prescription...", Toast.LENGTH_SHORT).show()
                        // TODO: Implement actual download functionality
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.FileDownload,
                        contentDescription = "Download Prescription",
                        tint = Color(0xFF1A237E)
                    )
                }
                IconButton(
                    onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Prescription from Dr. ${prescription.doctorName}\n" +
                                        "Date: ${prescription.date}\n" +
                                        "Medications: ${prescription.medications.joinToString(", ")}"
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Prescription via"))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share Prescription",
                        tint = Color(0xFF1A237E)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Medications text placed below the row with date and buttons
            Text(
                text = "Medications: ${prescription.medications.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

