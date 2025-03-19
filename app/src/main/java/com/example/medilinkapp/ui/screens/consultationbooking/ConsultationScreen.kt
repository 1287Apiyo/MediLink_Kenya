package com.example.medilinkapp.ui.screens.consultationbooking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.VideoCall
import androidx.compose.material.ripple.rememberRipple
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
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                doctors = repository.getDoctors()
                error = null
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
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

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Loading doctors...", style = TextStyle(fontSize = 14.sp, color = Color.Gray))
                    }
                }
            } else if (error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $error", style = TextStyle(fontSize = 14.sp, color = Color.Red))
                }
            } else if (doctors.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No doctors available.", style = TextStyle(fontSize = 14.sp, color = Color.Gray))
                }
            } else {
                LazyColumn {
                    items(doctors) { doctor ->
                        SmallDoctorCard(
                            doctor,
                            onVideoCall = {
                                // Navigate to VideoCallScreen with the doctor's name as parameter
                                navController.navigate("videoCallScreen/${doctor.name}")
                            },
                            onChat = {
                                // Navigate to ChatScreen with the doctor's name as parameter
                                navController.navigate("chatScreen/${doctor.name}")
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


        }
    }
}

@Composable
fun SmallDoctorCard(doctor: Doctor, onVideoCall: () -> Unit, onChat: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
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
                contentDescription = "Doctor ${doctor.name}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
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
                    Icon(
                        Icons.Outlined.VideoCall,
                        contentDescription = "Video Call",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onChat) {
                    Icon(
                        Icons.Outlined.Chat,
                        contentDescription = "Chat",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}


@Composable
fun RegisterAsDoctorSection(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Are You a Medical Professional?",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Join our platform and connect with patients in need of medical advice.",
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { navController.navigate("doctorRegistrationScreen") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Register Now", color = Color.White)
            }
        }
    }
}