package com.example.medilinkapp.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.medilinkapp.ui.theme.MedilinkAppTheme
import com.example.medilinkapp.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    // State for email and password fields
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    MedilinkAppTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("MediLink Login", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(32.dp))

                // Email field
                TextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Login Button
                Button(
                    onClick = {
                        // Assuming login is successful, navigate to Dashboard
                        navController.navigate("dashboard")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Forgot Password Link
                Text(
                    text = "Forgot Password?",
                    color = Color.Blue,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sign-Up Link
                Text(
                    text = "Don't have an account? Sign Up",
                    color = Color.Blue,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = rememberNavController()) // Preview requires a NavController
}
