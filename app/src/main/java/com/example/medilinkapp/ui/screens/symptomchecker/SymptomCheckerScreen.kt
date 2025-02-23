package com.example.medilinkapp.ui.screens.symptomchecker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medilinkapp.ui.theme.MedilinkAppTheme
import androidx.compose.foundation.layout.imePadding
import com.example.medilinkapp.network.AIRequest
import com.example.medilinkapp.network.Content
import com.example.medilinkapp.network.MedicalApiService
import com.example.medilinkapp.network.Part
import kotlinx.coroutines.launch

@Composable
fun SymptomCheckerScreen(navController: NavController) {
    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    var chatHistory by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val apiService = remember { MedicalApiService.create() }

    MedilinkAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .imePadding(), // Ensures padding when keyboard is open
            verticalArrangement = Arrangement.SpaceBetween
        ) {
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
                    text = "AI Symptom Checker",
                    style = MaterialTheme.typography.headlineSmall.copy(color = Color.White, fontSize = 20.sp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    chatHistory.forEach { (user, ai) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            ChatBubble(text = user, isUser = true)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            ChatBubble(text = ai, isUser = false)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(24.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF1976D2), shape = RoundedCornerShape(24.dp)),
                    placeholder = { Text("Describe your symptoms...", color = Color.White) },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF1976D2),
                        unfocusedContainerColor = Color(0xFF1976D2),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (userInput.text.isNotBlank()) {
                            coroutineScope.launch {
                                val response = try {
                                    val apiKey = "AIzaSyDiILitp4R4MaTzfIdfyeclcs_hSOJIE6o" // Replace with your actual API key
                                    apiService.getMedicalResponse(
                                        apiKey,
                                        AIRequest(listOf(Content(listOf(Part(userInput.text)))))
                                    ).candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No response"
                                } catch (e: Exception) {
                                    "Failed to fetch response. Please try again."
                                }
                                chatHistory = chatHistory + (userInput.text to response)
                                userInput = TextFieldValue("")
                                keyboardController?.hide()
                            }
                        }
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Icon(imageVector = Icons.Filled.Send, contentDescription = "Send")
                }
            }
        }
    }
}

@Composable
fun ChatBubble(text: String, isUser: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f) // Reducing width to 80% for better spacing
            .padding(vertical = 4.dp)
            .background(
                if (isUser) Color(0xFF1976D2) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        Text(text = text, color = if (isUser) Color.White else Color.Black)
    }
}
