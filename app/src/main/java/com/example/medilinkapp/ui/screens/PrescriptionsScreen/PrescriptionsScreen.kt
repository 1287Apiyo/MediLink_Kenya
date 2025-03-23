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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medilinkapp.model.Prescription
import com.example.medilinkapp.repository.FirestoreRepository
import kotlinx.coroutines.launch

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

    Spacer(modifier = Modifier.height(3.dp))


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Your Prescriptions",
                            fontSize = 22.sp,
                            color = Color.White,
                            fontFamily = FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.height(5.dp))

                        // Optional subtitle with guidance
                        Text(
                            text = "Need your medicines? Tap the cart to order online!",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            fontFamily = FontFamily.Serif
                        )
                    }
                },
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
                onClick = { navController.navigate("pharmacy") },
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
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFF5F5F5), Color(0xFFE0E0E0))
                    )
                )
        ) {
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color(0xFF1A237E))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Loading prescriptions...", style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Serif))
                    }
                }
                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Error: $error",
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Serif),
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            isLoading = true
                            loadPrescriptions()
                        }) {
                            Text("Retry", fontFamily = FontFamily.Serif)
                        }
                    }
                }
                prescriptions.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No prescriptions available.", style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Serif))
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
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
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        // A Box to add a horizontal gradient background overlay
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                // Top Row: Icon and Prescription Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.MedicalServices,
                        contentDescription = "Prescription Icon",
                        tint = Color(0xFF1A237E),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Prescription from Dr. ${prescription.doctorName}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontSize = 20.sp
                        ),
                        color = Color(0xFF1A237E)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Row: Date on left, download and share buttons on right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Date: ${prescription.date}",
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Serif),
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
                // Medications details
                Text(
                    text = "Medications: ${prescription.medications.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp
                    ),
                    color = Color.DarkGray
                )
            }
        }
    }
}