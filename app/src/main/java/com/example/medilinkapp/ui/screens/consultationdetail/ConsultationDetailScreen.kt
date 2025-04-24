package com.example.medilinkapp.ui.screens.consultationdetail

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.medilinkapp.viewmodel.ConsultationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultationDetailScreen(consultationId: String?, viewModel: ConsultationViewModel) {
    val consultation = viewModel.getConsultationById(consultationId ?: "")

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Consultation Detail") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation */ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        consultation?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues)
            ) {
                Text("Doctor: ${it.doctorName}", style = MaterialTheme.typography.bodyLarge)
                Text("Category: ${it.category}", style = MaterialTheme.typography.bodyMedium)
                Text("Method: ${it.method}", style = MaterialTheme.typography.bodyMedium)
                Text("Date & Time: ${it.dateTime}", style = MaterialTheme.typography.bodyMedium)
            }
        } ?: run {
            Text("Consultation not found.")
        }
    }
}
