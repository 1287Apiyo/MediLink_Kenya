package com.example.medilinkapp.ui.screens.consultationbooking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medilinkapp.ui.components.CameraPermissionWrapper
import com.example.medilinkapp.ui.components.CameraPreviewView
import com.example.medilinkapp.ui.components.RecordingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCallScreen(navController: NavController, doctorName: String) {
    // States for mute and camera toggle (simulate call controls)
    var isMuted by remember { mutableStateOf(false) }
    var isCameraOn by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Video Call with $doctorName") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { isMuted = !isMuted }) {
                    Icon(
                        imageVector = if (isMuted) Icons.Filled.MicOff else Icons.Filled.Mic,
                        contentDescription = if (isMuted) "Unmute" else "Mute"
                    )
                }
                IconButton(onClick = { isCameraOn = !isCameraOn }) {
                    Icon(
                        imageVector = if (isCameraOn) Icons.Filled.Videocam else Icons.Filled.VideocamOff,
                        contentDescription = if (isCameraOn) "Turn camera off" else "Turn camera on"
                    )
                }
                IconButton(onClick = {
                    // End the call (simulate by navigating back)
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.CallEnd,
                        tint = Color.Red,
                        contentDescription = "End Call"
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            if (isCameraOn) {
                // Wrap the preview in the permission wrapper
                CameraPermissionWrapper {
                    CameraPreviewView(modifier = Modifier.fillMaxSize())
                }
            } else {
                Text(
                    text = "Camera is off",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            // Display the recording indicator if audio is active (not muted)
            if (!isMuted) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    RecordingIndicator()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, doctorName: String) {
    // Local state for current input and list of messages.
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat with  $doctorName") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Messages list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(messages) { message ->
                    Text(text = message, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            // Input field and send button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type your message...") }
                )
                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            // In a real app, send the message to your chat service.
                            messages.add("Me: $messageText")
                            messageText = ""
                        }
                    }
                ) {
                    Icon(Icons.Filled.Send, contentDescription = "Send Message")
                }
            }
        }
    }
}