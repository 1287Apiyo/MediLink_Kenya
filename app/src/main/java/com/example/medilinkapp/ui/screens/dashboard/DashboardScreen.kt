package com.example.medilinkapp.ui.screens.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medilinkapp.R
import com.example.medilinkapp.ui.theme.MedilinkAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

// Custom FontFamily for Times New Roman (using Serif as a proxy)
val timesNewRoman = FontFamily.Serif

@Composable
fun DashboardScreen(navController: NavController) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF1A237E) // Change this to your preferred color

    // Set the status bar color
    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
    }
    // Firebase instances
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    // State for user's full name
    var userName by remember { mutableStateOf("User") }
    var isNameLoading by remember { mutableStateOf(true) }

    // Compute greeting based on current hour.
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 0..11 -> "Good Morning"
        in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }

    // Fetch user name from Firestore when the composable is first launched
    LaunchedEffect(firebaseAuth.currentUser?.uid) {
        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    userName = documentSnapshot.getString("fullName") ?: "User"
                    isNameLoading = false
                }
                .addOnFailureListener {
                    isNameLoading = false
                }
        } else {
            isNameLoading = false
        }
    }

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

                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                if (isNameLoading) {
                                    Text(
                                        text = "Welcome Back",
                                        style = TextStyle(
                                            fontFamily = timesNewRoman,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = Color.White
                                        )
                                    )
                                } else {
                                    Text(
                                        text = greeting,
                                        style = TextStyle(
                                            fontFamily = timesNewRoman,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = Color.White
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = userName,
                                        style = TextStyle(
                                            fontFamily = timesNewRoman,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = Color.White
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Your Health, Our Priority",
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
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                ServiceCard(
                                    imageRes = R.drawable.symp,
                                    title = "AI Symptom Checker",
                                    description = "Instantly assess your symptoms ",
                                    modifier = Modifier.weight(1f),
                                    onClick = { navController.navigate("symptom_checker") }
                                )
                                ServiceCard(
                                    imageRes = R.drawable.presc,
                                    title = "E-Prescriptions",
                                    description = "Get digital prescriptions from doctors",
                                    modifier = Modifier.weight(1f),
                                    onClick = { navController.navigate("prescriptions") }
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                ServiceCard(
                                    imageRes = R.drawable.pharm,
                                    title = "Pharmacy Services",
                                    description = "Order and receive prescribed medications ",
                                    modifier = Modifier.weight(1f),
                                    onClick = { navController.navigate("pharmacy") }
                                )
                                ServiceCard(
                                    imageRes = R.drawable.rec,
                                    title = "Health Records",
                                    description = "Securely access and manage your health records ",
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
                .background(Color.White)
                .border(width = 1.dp, color = Color.Black, shape = CircleShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(bounded = true, radius = 32.dp, color = Color.LightGray)
                ) { onClick() }
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
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
