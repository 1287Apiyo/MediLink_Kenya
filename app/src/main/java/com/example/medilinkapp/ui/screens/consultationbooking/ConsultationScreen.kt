// ConsultationScreen.kt
package com.example.medilinkapp.ui.screens.consultationbooking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.VideoCall
import androidx.compose.material.icons.outlined.Chat

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

val timesNewRoman = FontFamily.Serif

@Composable
fun ConsultationScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Text(
                "Consult a Doctor",
                style = TextStyle(
                    fontFamily = timesNewRoman,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Get expert medical advice anytime, anywhere.",
                style = TextStyle(
                    fontFamily = timesNewRoman,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Consultation Options
            ConsultationOption(
                title = "Video Consultation",
                description = "Connect with a doctor via video call.",
                icon = Icons.Outlined.VideoCall,  // Use Outlined version
                onClick = { /* Navigate to video consultation */ }
            )
            Spacer(modifier = Modifier.height(12.dp))

            ConsultationOption(
                title = "Chat Consultation",
                description = "Get medical advice through live chat.",
                icon = Icons.Default.ArrowForward,
                onClick = { /* Navigate to chat consultation */ }
            )
        }
    }
}

@Composable
fun ConsultationOption(title: String, description: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = timesNewRoman,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
            Text(
                text = description,
                style = TextStyle(
                    fontFamily = timesNewRoman,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            )
        }
    }
}
