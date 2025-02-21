package com.example.medilinkapp.ui.screens.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medilinkapp.R
import com.example.medilinkapp.ui.theme.MedilinkAppTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(navController: NavController) {
    MedilinkAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Enable scrolling
            ) {
                // Header with Gradient Background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF2196F3), Color.White)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(4.dp),
                            tint = Color(0xFF1976D2)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Welcome Back, Anne", style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ))
                            Text("Your Health, Our Priority", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Quick Action Buttons in a Grid Layout
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("Quick Access", style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ))
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        QuickActionButton(R.drawable.ic_consultation, "Consultation") { navController.navigate("consultation") }
                        QuickActionButton(R.drawable.ic_appointments, "Appointments") { navController.navigate("appointments") }
                        QuickActionButton(R.drawable.ic_health_monitoring, "Monitoring") { navController.navigate("monitoring") }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        "Featured Services",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                            ServiceCard(
                                imageRes = R.drawable.ai,
                                title = "AI Symptom Checker",
                                description = "Instantly assess your symptoms with AI-powered insights.",
                                modifier = Modifier.weight(1f) // Makes cards equally sized
                            )
                            ServiceCard(
                                imageRes = R.drawable.prescriptions,
                                title = "E-Prescriptions",
                                description = "Get digital prescriptions from doctors for easy access.",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                            ServiceCard(
                                imageRes = R.drawable.pharmacyy,
                                title = "Pharmacy Services",
                                description = "Order and receive prescribed medications hassle-free.",
                                modifier = Modifier.weight(1f)
                            )
                            ServiceCard(
                                imageRes = R.drawable.records, // Add a new drawable for health records
                                title = "Health Records",
                                description = "Securely access and manage your health records anytime.",
                                modifier = Modifier.weight(1f)
                            )
                        }

                    }
                }


            }
        }
    }
}


@Composable
fun QuickActionButton(icon: Any, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary) // Soft background
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            when (icon) {
                is ImageVector -> Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp)
                )
                is Int -> Icon(
                    painter = painterResource(id = icon),
                    contentDescription = label,
                    modifier = Modifier.size(32.dp),
                    tint = Color.Unspecified // Keeps original colors
                )
                else -> throw IllegalArgumentException("Unsupported icon type")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}



@Composable
fun ServiceCard(imageRes: Int, title: String, description: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp), // Fixed height to make all cards equal
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f) // Ensures a consistent image proportion
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2, // Limits text to avoid uneven heights
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}



@Composable
@Preview(showBackground = true)
fun PreviewDashboardScreen() {
    DashboardScreen(navController = rememberNavController())
}
