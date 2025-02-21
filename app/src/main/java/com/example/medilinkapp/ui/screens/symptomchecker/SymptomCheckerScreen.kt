package com.example.medilinkapp.ui.screens.symptomchecker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SymptomCheckerScreen(navController: NavController) {
    var symptoms by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("AI Symptom Checker", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        // TextField for entering symptoms
        OutlinedTextField(
            value = symptoms,
            onValueChange = { symptoms = it },
            label = { Text("Enter Symptoms (e.g., headache, fever)") },
            singleLine = false,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { /* Close Keyboard */ })
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Check Symptoms Button
        Button(
            onClick = {
                isLoading = true
                // Call API to check symptoms
                result = "Possible Condition: Migraine or Flu" // Replace with real API response
                isLoading = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Check Symptoms")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show result
        if (isLoading) {
            CircularProgressIndicator()
        } else if (result.isNotEmpty()) {
            Text(result, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
