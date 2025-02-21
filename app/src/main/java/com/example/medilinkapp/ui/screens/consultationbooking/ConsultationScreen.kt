package com.example.medilinkapp.ui.screens.consultationbooking
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medilinkapp.R

@Composable
fun ConsultationScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val doctors = listOf(
        Doctor("Dr. Alice Kamau", "Cardiologist", R.drawable.hospital_icon),
        Doctor("Dr. Brian Ouma", "Dermatologist", R.drawable.hospital_icon),
        Doctor("Dr. Clara Njoroge", "Neurologist", R.drawable.hospital_icon)
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Consult a Doctor",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        SearchBar(searchQuery) { searchQuery = it }
        Spacer(modifier = Modifier.height(16.dp))

        doctors.forEach { doctor ->
            DoctorCard(doctor) { navController.navigate("book_consultation/${doctor.name}") }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = query,
                onValueChange = onQueryChanged,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = doctor.imageRes),
                contentDescription = "Doctor Image",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = doctor.name, fontWeight = FontWeight.Bold)
                Text(text = doctor.specialty, color = Color.Gray)
            }
        }
    }
}

data class Doctor(val name: String, val specialty: String, val imageRes: Int)
