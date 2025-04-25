package com.example.medilinkapp.ui.screens.appointmenthistory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthTopicDetailScreen(topicName: String, navController: NavController) {
    val topicContent = healthTopicContents[topicName] ?: mapOf("Introduction" to "No content available for this topic.")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topicName,
                        fontFamily = FontFamily.Serif,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A237E)
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
            // Render content in structured sections
            SectionHeader("Introduction")
            Text(topicContent["Introduction"] ?: "No content available", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader("Causes / Risk Factors")
            Text(topicContent["Causes"] ?: "No content available", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader("Symptoms")
            Text(topicContent["Symptoms"] ?: "No content available", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader("Treatment / Management")
            Text(topicContent["Treatment"] ?: "No content available", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader("Prevention")
            Text(topicContent["Prevention"] ?: "No content available", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader("Resources / Further Reading")
            Text(topicContent["Resources"] ?: "No content available", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = Color(0xFF1A237E)
        ),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
