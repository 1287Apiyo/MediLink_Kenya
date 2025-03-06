// ConsultationScreen.kt
package com.example.medilinkapp.ui.screens.consultationbooking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medilinkapp.R

@Composable
fun ConsultationScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFC))
    ) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Consult a Doctor",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "Get expert medical advice anytime, anywhere.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        var searchQuery by remember { mutableStateOf("") }
        SearchBar(query = searchQuery, onQueryChange = { searchQuery = it })

        Spacer(modifier = Modifier.height(20.dp))

        // Featured Doctors Section
        SectionContainer(title = "Featured Doctors") {
            FeaturedDoctorsList()
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Consultation History Section
        SectionContainer(title = "Consultation History") {
            ConsultationHistoryList(navController)
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        placeholder = { Text("Search for doctors...") },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
            focusedBorderColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun SectionContainer(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
fun FeaturedDoctorsList() {
    val doctors = listOf(
        Doctor("Dr. John Doe", "General Practitioner", R.drawable.doctor1),
        Doctor("Dr. Jane Smith", "Cardiologist", R.drawable.doctor3),
        Doctor("Dr. Emily Johnson", "Dermatologist", R.drawable.doctor3)
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(doctors) { doctor ->
            DoctorCard(doctor)
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { /* Handle doctor click */ },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = doctor.imageRes),
                contentDescription = doctor.name,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = doctor.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = doctor.specialty,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* Book consultation */ },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Book Now", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun ConsultationHistoryList(navController: NavController) {
    val consultations = listOf(
        "Consultation with Dr. Smith - Jan 10, 2025",
        "Consultation with Dr. Doe - Feb 5, 2025",
        "Consultation with Dr. Brown - Feb 20, 2025"
    )

    LazyColumn {
        items(consultations) { consultation ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Doctor",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = consultation,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

data class Doctor(val name: String, val specialty: String, val imageRes: Int)
