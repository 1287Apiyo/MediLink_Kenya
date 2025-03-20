package com.example.medilinkapp.ui.screens.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

// Custom FontFamily for Times New Roman
val timesNewRoman = FontFamily.Serif
@Composable
fun DashboardScreen(navController: NavController) {
    MedilinkAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(Color(0xFF1A237E))
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.3f))
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "Profile",
                                    tint = Color.White,
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    "Welcome Back, Anne",
                                    style = TextStyle(
                                        fontFamily = timesNewRoman,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    "Your Health, Our Priority",
                                    style = TextStyle(
                                        fontFamily = timesNewRoman,
                                        fontSize = 16.sp,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                )
                            }
                        }
                    }
                }

                item {
                    // Quick Access Section
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Quick Access",
                        fontFamily = timesNewRoman,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        QuickActionButton(R.drawable.consultation, "Consultation") {
                            navController.navigate("consultation")
                        }
                        QuickActionButton(R.drawable.appointment, "Appointments") {
                            navController.navigate("appointments")
                        }
                        QuickActionButton(R.drawable.healthmon, "Monitoring") {
                            navController.navigate("monitoring")
                        }
                    }
                }

                item {
                    // Featured Services Section
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            "Featured Services",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Serif, // Times New Roman
                                fontWeight = FontWeight.Bold
                            )
                        )


                    Spacer(modifier = Modifier.height(20.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                                ServiceCard(
                                    imageRes = R.drawable.symp,
                                    title = "AI Symptom Checker",
                                    description = "Instantly assess your symptoms with AI-powered insights.",
                                    modifier = Modifier.weight(1f),
                                    onClick = { navController.navigate("symptom_checker") }
                                )
                                ServiceCard(
                                    imageRes = R.drawable.presc,
                                    title = "E-Prescriptions",
                                    description = "Get digital prescriptions from doctors for easy access.",
                                    modifier = Modifier.weight(1f),
                                    onClick = { navController.navigate("prescriptions") } // ✅ Add this line
                                )

                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                                ServiceCard(
                                    imageRes = R.drawable.pharm,
                                    title = "Pharmacy Services",
                                    description = "Order and receive prescribed medications hassle-free.",
                                    modifier = Modifier.weight(1f),
                                    onClick = { navController.navigate("pharmacy") } // Add navigation
                                )

                                ServiceCard(
                                    imageRes = R.drawable.rec,
                                    title = "Health Records",
                                    description = "Securely access and manage your health records anytime.",
                                    modifier = Modifier.weight(1f),
                                    onClick = { navController.navigate("health_records") }
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionButton(icon: Any, label: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .shadow(elevation = 8.dp, shape = CircleShape, clip = false)
                .size(64.dp)
                .clip(CircleShape)
                // Solid white background for a clean glass look
                .background(Color.White)
                // A light border for a defined edge
                .border(width = 1.dp, color = Color.Black, shape = CircleShape)
                // Circular ripple effect on click
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(bounded = true, radius = 32.dp, color = Color.LightGray)
                ) { onClick() }
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            // Subtle glossy overlay for a glass-like finish
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.7f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
            // Icon placed on top
            when (icon) {
                is ImageVector -> Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
                is Int -> Icon(
                    painter = painterResource(id = icon),
                    contentDescription = label,
                    modifier = Modifier.size(32.dp),
                    tint = Color.Unspecified
                )
                else -> throw IllegalArgumentException("Unsupported icon type")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = TextStyle(
                fontFamily = timesNewRoman,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}



@Composable
fun ServiceCard(
    imageRes: Int,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(onClick != null) { onClick?.invoke() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = timesNewRoman,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = description,
                    style = TextStyle(
                        fontFamily = timesNewRoman,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
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
