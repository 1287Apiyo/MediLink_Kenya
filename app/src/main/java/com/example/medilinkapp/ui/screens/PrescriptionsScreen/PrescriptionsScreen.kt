package com.example.medilinkapp.ui.screens.prescriptions

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionsScreen(navController: NavController) {
    val context = LocalContext.current
    val prescriptions = remember { mutableStateListOf("Prescription 1", "Prescription 2") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        TopAppBar(
            title = { Text("E-Prescriptions", fontSize = 22.sp, color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A237E))

        )

        Spacer(modifier = Modifier.height(16.dp))

        // Prescription List
        PrescriptionList(prescriptions, context)

        Spacer(modifier = Modifier.height(16.dp))

        // Upload Button
        UploadPrescriptionButton(context)
    }
}

@Composable
fun PrescriptionList(prescriptions: List<String>, context: Context) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        prescriptions.forEach { prescription ->
            PrescriptionCard(prescription, context)
        }
    }
}

@Composable
fun PrescriptionCard(prescription: String, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { downloadPrescription(context, prescription) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = prescription,
            modifier = Modifier.padding(16.dp),
            fontSize = 16.sp
        )
    }
}

@Composable
fun UploadPrescriptionButton(context: Context) {
    Button(
        onClick = { uploadPrescription(context) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor =Color(0xFF1A237E))
    ) {
        Text("Upload New Prescription", color = Color.White)
    }
}

// Functions for Toast Messages
fun uploadPrescription(context: Context) {
    Toast.makeText(context, "Upload feature coming soon!", Toast.LENGTH_SHORT).show()
}

fun downloadPrescription(context: Context, prescription: String) {
    Toast.makeText(context, "Downloading $prescription...", Toast.LENGTH_SHORT).show()
}
