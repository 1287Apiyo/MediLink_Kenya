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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import com.example.medilinkapp.network.AIRequest
import com.example.medilinkapp.network.Content
import com.example.medilinkapp.network.MedicalApiService
import com.example.medilinkapp.network.Part
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomCheckerScreen(navController: NavController) {
    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    var chatHistory by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var loading by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val apiService = remember { MedicalApiService.create() }
    val listState = rememberLazyListState()

    MedilinkAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            // Header
            TopAppBar(
                title = { Text("AI Symptom Checker", fontSize = 22.sp, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A237E))

            )

            Spacer(modifier = Modifier.height(16.dp))
            // Chat History
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(chatHistory) { (user, ai) ->
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                ChatBubble(text = user, isUser = true)
                            }
                            if (ai.isNotEmpty()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    ChatBubble(text = ai, isUser = false)
                                }
                            }
                        }
                    }}}
            if (loading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    LoadingIndicator()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User Input
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
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Describe your symptoms...") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE3F2FD),
                        unfocusedContainerColor = Color(0xFFE3F2FD),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (userInput.text.isNotBlank()) {
                            coroutineScope.launch {
                                loading = true
                                val response = try {
                                    val apiKey = "AIzaSyDiILitp4R4MaTzfIdfyeclcs_hSOJIE6o" // Replace with your actual API key
                                    apiService.getMedicalResponse(
                                        apiKey = "Bearer AIzaSyDiILitp4R4MaTzfIdfyeclcs_hSOJIE6o", // ADD Bearer
                                        request = AIRequest(listOf(Content(listOf(Part(userInput.text)))))
                                    )
                                        .candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No response"
                                } catch (e: Exception) {
                                    "Failed to fetch response. Please try again."
                                }
                                chatHistory = chatHistory + (userInput.text to response)
                                userInput = TextFieldValue("")
                                keyboardController?.hide()
                                loading = false

                                // Scroll to the last message
                                listState.animateScrollToItem(chatHistory.size - 1)
                            }
                        }
                    },
                    shape = RoundedCornerShape(10),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)) ,
                    enabled = !loading
                ){
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(imageVector = Icons.Filled.Send, contentDescription = "Send")
                    }
                    }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            color = Color.Gray,
            strokeWidth = 2.dp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("AI is analyzing...", color = Color.Black)
    }
}



@Composable
fun ChatBubble(text: String, isUser: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(vertical = 4.dp)
            .background(if (isUser) Color(0xFF1A237E) else Color(0xFFE0E0E0), shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Text(text = text, color = if (isUser) Color.White else Color.Black)
    }
}
