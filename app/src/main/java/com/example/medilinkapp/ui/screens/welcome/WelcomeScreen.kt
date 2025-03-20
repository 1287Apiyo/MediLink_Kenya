package com.example.medilinkapp.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medilinkapp.R
import com.example.medilinkapp.ui.theme.MedilinkAppTheme

@Composable
fun WelcomeScreen(navController: NavController) {
    MedilinkAppTheme {
        Scaffold(containerColor = Color.White) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Image at the top
                    Image(
                        painter = painterResource(id = R.drawable.appointment), // Use your image resource
                        contentDescription = "Welcome Image",
                        modifier = Modifier.size(200.dp)
                    )

                    // Title
                    Text(
                        text = "Welcome to Medilink",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color.Black
                        )
                    )

                    // Subtitle
                    Text(
                        text = "Your Health, Our Priority",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    )

                    // Create Account Button
                    Button(
                        onClick = { navController.navigate("signup") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Create Account",
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        )
                    }

                    // Login Button
                    OutlinedButton(
                        onClick = { navController.navigate("login") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Login",
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                        )
                    }
                }
            }
        }
    }
}
