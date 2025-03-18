package com.example.medilinkapp.ui.screens.consultationbooking

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medilinkapp.model.Doctor
import com.example.medilinkapp.repository.FirestoreRepository
import kotlinx.coroutines.launch

@Composable
fun ConsultationScreen(navController: NavController) {
    val repository = FirestoreRepository()
    var doctors by remember { mutableStateOf(emptyList<Doctor>()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            doctors = repository.getDoctors()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // ✅ HEADER
        Text(
            text = "Consult a Doctor",
            style = TextStyle(
                fontFamily = FontFamily.Serif, // Times New Roman
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Get expert medical advice anytime, anywhere.",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ✅ LOADING INDICATOR
        if (doctors.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // ✅ SECTION TITLE
            Text(
                text = "Available Doctors",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // ✅ SCROLLABLE DOCTOR LIST
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(doctors) { doctor ->
                    DoctorCard(
                        doctor = doctor,
                        onVideoCall = { /* TODO: Handle Video Call */ },
                        onChat = { /* TODO: Handle Chat */ }
                    )
                }
            }
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor, onVideoCall: () -> Unit, onChat: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)) // 🔽 Less rounded shape
            .clickable { onVideoCall() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)) // 🔽 Softer color
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ✅ SMALLER DOCTOR IMAGE
            Image(
                painter = painterResource(id = doctor.drawableId),
                contentDescription = doctor.name,
                modifier = Modifier
                    .size(120.dp)  // 🔽 Reduced from 150.dp to 120.dp
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // ✅ DOCTOR DETAILS
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = doctor.name,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = doctor.specialization,
                    color = Color.Gray,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                )
                Text(
                    text = doctor.experience,
                    color = Color.Gray,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                // ✅ BUTTONS (Video Call & Chat)
                Row {
                    IconButton(onClick = onVideoCall) {
                        Icon(Icons.Outlined.VideoCall, contentDescription = "Video Call", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onChat) {
                        Icon(Icons.Outlined.Chat, contentDescription = "Chat", tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}
