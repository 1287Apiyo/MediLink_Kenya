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
import androidx.compose.material.icons.filled.Camera
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    darkThemeEnabled: Boolean,
    onToggleTheme: () -> Unit
) {
    // ---- Authentication & user data ----
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val name = currentUser?.displayName ?: "User Name"
    val email = currentUser?.email ?: "Email"
    val phoneNumber = currentUser?.phoneNumber ?: "Phone Number"

    // ---- Local UI state ----
    var notificationsEnabled by remember { mutableStateOf(true) }
    val profileImageUri = remember { mutableStateOf<Uri?>(null) }

    // ---- Image picker launcher ----
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        profileImageUri.value = it
    }

    Scaffold(
        containerColor = Color.White,               // make content area white
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

            // Profile Image
            // … inside your Column …

            Box(
                modifier = Modifier
                    .size(130.dp)
                    .border(2.dp, Color.Black, CircleShape)     // keep the border
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                // Clip only the image (or placeholder icon) to a circle:
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

                // Single camera icon, offset outside the circle:
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Change Picture",
                    tint = Color.Black,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 12.dp, y = 12.dp)      // nudge it a bit further
                        .size(40.dp)                       // larger container
                        .background(Color.White, CircleShape)
                        .padding(8.dp)                     // more breathing room inside
                )
            }


                Spacer(Modifier.height(20.dp))

            // User info
            Text(name,       fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(Modifier.height(8.dp))
            Text(email,      fontSize = 16.sp,                                  color = Color.Black)
            Spacer(Modifier.height(4.dp))
            Text(phoneNumber, fontSize = 16.sp,                                 color = Color.Black)

            Spacer(Modifier.weight(1f))

            // Logout
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
