package com.example.medilinkapp.ui.screens.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medilinkapp.ui.theme.MedilinkAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    // States for input fields
    val fullName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val dob = remember { mutableStateOf("") }      // Date of Birth as MM/DD/YYYY
    val gender = remember { mutableStateOf("") }     // e.g., "Male", "Female", etc.
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    // State for error messages and loading
    val errorMessage = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }

    // Firebase instances
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    MedilinkAppTheme {
        Scaffold(
            containerColor = Color.White
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) {
                // Main content scrollable column
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 32.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Title
                    Text(
                        text = "Create Account",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            color = Color.Black
                        )
                    )

                    // Display error message if any
                    if (errorMessage.value.isNotEmpty()) {
                        Text(
                            text = errorMessage.value,
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = Color.Red
                            )
                        )
                    }

                    // Full Name Field
                    OutlinedTextField(
                        value = fullName.value,
                        onValueChange = { fullName.value = it },
                        label = { Text("Full Name", style = TextStyle(fontFamily = FontFamily.Serif)) },
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
                        label = { Text("Email", style = TextStyle(fontFamily = FontFamily.Serif)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.Black
                        )
                    )

                    // Phone Number Field
                    OutlinedTextField(
                        value = phone.value,
                        onValueChange = { phone.value = it },
                        label = { Text("Phone Number", style = TextStyle(fontFamily = FontFamily.Serif)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.Black
                        )
                    )

                    // Date of Birth Field
                    OutlinedTextField(
                        value = dob.value,
                        onValueChange = { dob.value = it },
                        label = { Text("Date of Birth (MM/DD/YYYY)", style = TextStyle(fontFamily = FontFamily.Serif)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.Black
                        )
                    )

                    // Gender Field
                    OutlinedTextField(
                        value = gender.value,
                        onValueChange = { gender.value = it },
                        label = { Text("Gender", style = TextStyle(fontFamily = FontFamily.Serif)) },
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
                        label = { Text("Password", style = TextStyle(fontFamily = FontFamily.Serif)) },
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
                        label = { Text("Confirm Password", style = TextStyle(fontFamily = FontFamily.Serif)) },
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
                            // Clear previous error
                            errorMessage.value = ""

                            // Simple validation check for password match
                            if (password.value != confirmPassword.value) {
                                errorMessage.value = "Passwords do not match"
                                return@Button
                            }
                            isLoading.value = true

                            // Sign up user using FirebaseAuth
                            firebaseAuth.createUserWithEmailAndPassword(email.value, password.value)
                                .addOnCompleteListener { task ->
                                    isLoading.value = false
                                    if (task.isSuccessful) {
                                        // User created, now save additional user data in Firestore
                                        val uid = firebaseAuth.currentUser?.uid
                                        if (uid != null) {
                                            val userData = hashMapOf(
                                                "fullName" to fullName.value,
                                                "email" to email.value,
                                                "phone" to phone.value,
                                                "dob" to dob.value,
                                                "gender" to gender.value
                                            )
                                            firestore.collection("users")
                                                .document(uid)
                                                .set(userData)
                                                .addOnSuccessListener {
                                                    navController.navigate("dashboard")
                                                }
                                                .addOnFailureListener { e ->
                                                    errorMessage.value = e.message ?: "Failed to save user details"
                                                }
                                        } else {
                                            errorMessage.value = "User ID is null"
                                        }
                                    } else {
                                        // Sign up failed, show error message
                                        errorMessage.value = task.exception?.message ?: "Sign up failed"
                                    }
                                }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = if (isLoading.value) "Signing Up..." else "Sign Up",
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
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
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                    }
                }

                // Full-screen loading overlay when isLoading is true
                if (isLoading.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
