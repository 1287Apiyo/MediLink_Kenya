package com.example.yourapp.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    darkThemeEnabled: Boolean,
    onToggleTheme: () -> Unit
) {
    // --- Firebase instances ---
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val firestore = FirebaseFirestore.getInstance()
    val uid = currentUser?.uid

    // --- UI state for user fields ---
    var name by remember { mutableStateOf("User Name") }
    var email by remember { mutableStateOf(currentUser?.email ?: "Email") }
    var phoneNumber by remember { mutableStateOf("Phone Number") }

    // Fetch fullName & phone from Firestore once
    LaunchedEffect(uid) {
        if (uid != null) {
            try {
                val doc = firestore.collection("users")
                    .document(uid)
                    .get()
                    .await()
                doc.getString("fullName")?.let { name = it }
                doc.getString("phone")?.let { phoneNumber = it }
            } catch (e: Exception) {
                // handle errors if needed
            }
        }
    }

    // --- Local UI state ---
    var notificationsEnabled by remember { mutableStateOf(true) }
    val profileImageUri = remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { profileImageUri.value = it }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Profile",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A237E)
                ),
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            Icons.Default.SettingsBrightness,
                            contentDescription = "Dark Mode",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { notificationsEnabled = !notificationsEnabled }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // Profile picture with border and camera button
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .border(2.dp, Color.Black, CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUri.value != null) {
                    Image(
                        painter = rememberAsyncImagePainter(profileImageUri.value),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF512DA8),
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                }

                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Change Picture",
                    tint = Color.Black,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 12.dp, y = 12.dp)
                        .size(40.dp)
                        .background(Color.White, CircleShape)
                        .padding(8.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            // User info
            Text(name,        fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(Modifier.height(8.dp))
            Text(email,       fontSize = 16.sp,                                  color = Color.Black)
            Spacer(Modifier.height(4.dp))
            Text(phoneNumber, fontSize = 16.sp,                                  color = Color.Black)

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    auth.signOut()
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Text("Logout", color = Color.White)
            }
        }
    }
}
