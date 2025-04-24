package com.example.medilinkapp.ui.screens.appointmenthistory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medilinkapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentHistoryScreen(navController: NavController) {
    val healthTopics = listOf(
        HealthTopic("Sexual Wellness", R.drawable.naah),
        HealthTopic("Mental Health", R.drawable.ai),
        HealthTopic("Diabetes", R.drawable.ai),
        HealthTopic("Hypertension", R.drawable.pharm),
        HealthTopic("Asthma", R.drawable.naah),
        HealthTopic("Pediatrics", R.drawable.naah)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Health Topics", fontFamily = FontFamily.Serif, color = MaterialTheme.colorScheme.onPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A237E)
                )
            )
        }
    ) { paddingValues ->
        // Main content area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            // Welcome Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Welcome to Afya Centre",
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Here, you'll find educational materials on various diseases, wellness tips, and ways to maintain your health. Explore the topics below to learn more.",
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            // Displaying health topics in a grid format (2 items per row)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(healthTopics) { topic ->
                    HealthTopicCard(topic = topic)
                }
            }
        }
    }
}

@Composable
fun HealthTopicCard(topic: HealthTopic) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // Adjust card size
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White) // White background for the card
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Display image corresponding to each health topic
            Image(
                painter = painterResource(id = topic.imageRes), // Use the image resource from the HealthTopic object
                contentDescription = "${topic.name} Icon",
                modifier = Modifier.size(60.dp) // Adjust image size
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = topic.name,
                fontFamily = FontFamily.Serif,
                color = Color.Black, // Black text color
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Data class to hold each health topic with its corresponding image resource
data class HealthTopic(val name: String, val imageRes: Int)
