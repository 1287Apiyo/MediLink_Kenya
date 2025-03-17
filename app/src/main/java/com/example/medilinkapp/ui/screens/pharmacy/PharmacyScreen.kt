package com.example.medilinkapp.ui.screens.pharmacy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PharmacyScreen(navController: NavController) {
    val medications = listOf(
        Medication("Paracetamol", "Pain relief", "KES 200"),
        Medication("Amoxicillin", "Antibiotic", "KES 500"),
        Medication("Cough Syrup", "For cough and flu", "KES 300")
    )

    var cart by remember { mutableStateOf(mutableListOf<Medication>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header with cart button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pharmacy Services",
                    style = MaterialTheme.typography.headlineSmall.copy(color = Color.White, fontSize = 20.sp)
                )
            }
            IconButton(onClick = { navController.navigate("cart") }) {
                Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "Cart", tint = Color.White)
            }
        }

        // Medication List
        LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
            items(medications) { medication ->
                MedicationItem(medication, onAddToCart = { cart.add(it) })
            }
        }
    }
}

@Composable
fun MedicationItem(medication: Medication, onAddToCart: (Medication) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(medication.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(medication.description, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(medication.price, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onAddToCart(medication) }) {
                Text("Add to Cart")
            }
        }
    }
}

data class Medication(val name: String, val description: String, val price: String)
