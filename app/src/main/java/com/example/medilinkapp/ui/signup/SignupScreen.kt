package com.example.medilinkapp.ui.screens.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medilinkapp.ui.theme.MedilinkAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    // Dummy state for various input fields
    val fullName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    MedilinkAppTheme {
        Scaffold(
            containerColor = Color.White
        ) { paddingValues ->
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Distinct title using Times New Roman (Serif) style
                    Text(
                        text = "Create Account",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            fontSize = 32.sp,
                            color = Color.Black
                        )
                    )

                    // Full Name Field
                    OutlinedTextField(
                        value = fullName.value,
                        onValueChange = { fullName.value = it },
                        label = {
                            Text(
                                "Full Name",
                                style = TextStyle(fontFamily = FontFamily.Serif)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.Black
                        )
                    )

                    // Email Field
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = {
                            Text(
                                "Email",
                                style = TextStyle(fontFamily = FontFamily.Serif)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.Black
                        )
                    )

                    // Password Field
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = {
                            Text(
                                "Password",
                                style = TextStyle(fontFamily = FontFamily.Serif)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.Black
                        )
                    )

                    // Confirm Password Field
                    OutlinedTextField(
                        value = confirmPassword.value,
                        onValueChange = { confirmPassword.value = it },
                        label = {
                            Text(
                                "Confirm Password",
                                style = TextStyle(fontFamily = FontFamily.Serif)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.Black
                        )
                    )

                    // Primary Sign Up Button
                    Button(
                        onClick = {
                            // For now, simulate successful signup by navigating to dashboard
                            navController.navigate("dashboard")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Sign Up",
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        )
                    }

                    // Outlined Button to Navigate Back to Login
                    OutlinedButton(
                        onClick = { navController.navigate("login") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Already have an account? Login",
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
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
