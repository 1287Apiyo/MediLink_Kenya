package com.example.medilinkapp.ui.screens.PrescriptionsScreen



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

@Composable
fun PrescriptionsScreen(navController: NavController) {
    val context = LocalContext.current
    val prescriptions = remember { mutableStateListOf("Prescription 1", "Prescription 2") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1976D2), shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "E-Prescriptions",
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.White, fontSize = 20.sp)
            )
        }

        // Prescription List
        Column(modifier = Modifier.weight(1f)) {
            prescriptions.forEach { prescription ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { downloadPrescription(context, prescription) },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = prescription,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }

        // Upload Button
        Button(
            onClick = { uploadPrescription(context) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Upload New Prescription", color = Color.White)
        }
    }
}

fun uploadPrescription(context: Context) {
    Toast.makeText(context, "Upload feature coming soon!", Toast.LENGTH_SHORT).show()
}

fun downloadPrescription(context: Context, prescription: String) {
    Toast.makeText(context, "Downloading $prescription...", Toast.LENGTH_SHORT).show()
}
