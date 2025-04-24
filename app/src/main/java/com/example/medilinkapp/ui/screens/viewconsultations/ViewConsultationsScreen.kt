package com.example.medilinkapp.ui.screens.viewconsultations

import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.medilinkapp.model.Consultation
import com.example.medilinkapp.viewmodel.ConsultationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewConsultationsScreen(navController: NavHostController, viewModel: ConsultationViewModel) {
    // Fetch consultations when the screen is loaded
    LaunchedEffect(Unit) {
        viewModel.fetchConsultations()
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("View Consultations") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF1A237E),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            // Consultations List
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(viewModel.consultations) { consultation ->
                    ConsultationItem(consultation) {
                        // Navigate to the consultation detail view
                        navController.navigate("consultation_detail/${consultation.id}")
                    }
                }
            }

            // Book New Consultation Button
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate("consultation_booking") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Book a New Consultation", color = Color.White)
            }
        }
    }
}

@Composable
fun ConsultationItem(consultation: Consultation, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Doctor: ${consultation.doctorName}")
            Text("Category: ${consultation.category}")
            Text("Method: ${consultation.method}")
            Text("Date & Time: ${consultation.dateTime}")
        }
    }
}
