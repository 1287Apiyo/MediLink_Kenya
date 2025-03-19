package com.example.medilinkapp.ui.screens.consultationbooking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medilinkapp.model.ChatMessage
import com.example.medilinkapp.ui.components.CameraPermissionWrapper
import com.example.medilinkapp.ui.components.CameraPreviewView
import com.example.medilinkapp.ui.components.RecordingIndicator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCallScreen(navController: NavController, doctorName: String) {
    var isMuted by remember { mutableStateOf(false) }
    var isCameraOn by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Video Call with $doctorName",
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        androidx.compose.runtime.CompositionLocalProvider(
                            LocalContentColor provides Color.White
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
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
                IconButton(onClick = { navController.popBackStack() }) {
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
                CameraPermissionWrapper {
                    CameraPreviewView(modifier = Modifier.fillMaxSize())
                }
            } else {
                Text(
                    text = "Camera is off",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium.copy(fontFamily = FontFamily.Serif)
                )
            }
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
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val firestore = FirebaseFirestore.getInstance()
    var listenerRegistration by remember { mutableStateOf<ListenerRegistration?>(null) }

    LaunchedEffect(doctorName) {
        listenerRegistration = firestore.collection("chats")
            .document(doctorName)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val chatMessages = it.documents.mapNotNull { doc ->
                        doc.toObject(ChatMessage::class.java)
                    }
                    messages.clear()
                    messages.addAll(chatMessages)
                }
            }
    }

    DisposableEffect(Unit) {
        onDispose {
            listenerRegistration?.remove()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Chat with $doctorName",
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        // Set the background of the entire screen to white.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(messages) { chatMessage ->
                    val isMe = chatMessage.sender == "Me"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                    ) {
                        Card(
                            modifier = Modifier
                                .widthIn(max = 250.dp)
                                .padding(horizontal = 4.dp)
                                .background(Color.Transparent),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isMe)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${chatMessage.sender}: ${chatMessage.message}",
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Serif)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
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
                    placeholder = {
                        Text(
                            "Type your message...",
                            fontFamily = FontFamily.Serif,
                            color = Color.Black
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily.Serif,
                        color = Color.Black
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                )

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            val newMessage = ChatMessage(
                                sender = "Me",
                                message = messageText,
                                timestamp = System.currentTimeMillis()
                            )
                            firestore.collection("chats")
                                .document(doctorName)
                                .collection("messages")
                                .add(newMessage)
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