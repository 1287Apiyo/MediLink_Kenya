package com.example.medilinkapp.ui.screens.consultationbooking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medilinkapp.model.Doctor
import com.example.medilinkapp.repository.FirestoreRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Consult a Doctor",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Get expert medical advice anytime, anywhere.",
                style = TextStyle(fontSize = 16.sp, fontFamily = FontFamily.Serif, color = Color.Gray)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Available Doctors",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (doctors.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column {
                    doctors.forEach { doctor ->
                        SmallDoctorCard(
                            doctor,
                            onVideoCall = { /* TODO: Handle Video Call */ },
                            onChat = { /* TODO: Handle Chat */ }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ExtraInfoSection()
        }
    }
}
// ✅ IMPROVED SMALLER DOCTOR CARD
@Composable
fun SmallDoctorCard(doctor: Doctor, onVideoCall: () -> Unit, onChat: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)) // ✅ Reduced roundness
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onVideoCall() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = doctor.drawableId),
                contentDescription = doctor.name,
                modifier = Modifier
                    .size(80.dp)  // ✅ Smaller Image
                    .clip(RoundedCornerShape(12.dp)), // ✅ Less round
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = doctor.name,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(fontSize = 18.sp, fontFamily = FontFamily.Serif)
                )
                Text(
                    text = doctor.specialization,
                    style = TextStyle(fontSize = 14.sp, fontFamily = FontFamily.Serif, color = Color.Gray)
                )
                Text(
                    text = doctor.experience,
                    style = TextStyle(fontSize = 14.sp, fontFamily = FontFamily.Serif, color = Color.Gray)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
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

// ✅ EXTRA INFORMATION SECTION
@Composable
fun ExtraInfoSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Why Choose Us?",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "• 24/7 Availability\n• Certified & Experienced Doctors\n• Secure & Private Consultations\n• Affordable Pricing",
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif,
                color = Color.DarkGray
            )
        }
    }
}
