package com.example.medilinkapp.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.medilinkapp.ui.screens.appointmenthistory.AppointmentHistoryScreen
import com.example.medilinkapp.ui.screens.consultationbooking.AppointmentBookingScreen
import com.example.medilinkapp.ui.screens.consultationbooking.ChatScreen
import com.example.medilinkapp.ui.screens.consultationbooking.ConsultationScreen
import com.example.medilinkapp.ui.screens.consultationbooking.VideoCallScreen
import com.example.medilinkapp.ui.screens.dashboard.DashboardScreen
import com.example.medilinkapp.ui.screens.healthmonitoring.HealthMonitoringScreen
import com.example.medilinkapp.ui.screens.healthrecords.HealthRecordsScreen
import com.example.medilinkapp.ui.screens.login.LoginScreen
import com.example.medilinkapp.ui.screens.maps.MapScreen
import com.example.medilinkapp.ui.screens.pharmacy.PharmacyScreen
import com.example.medilinkapp.ui.screens.prescriptions.PrescriptionsScreen
import com.example.medilinkapp.ui.screens.profile.ProfileScreen
import com.example.medilinkapp.ui.screens.signup.SignupScreen
import com.example.medilinkapp.ui.screens.symptomchecker.SymptomCheckerScreen
import com.example.medilinkapp.ui.screens.welcome.WelcomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavGraph(context: Context) {
    val navController = rememberNavController()
    // Determine the start destination:
    // If a user is logged in, start at "dashboard"; otherwise start at "welcome".
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) "dashboard" else "welcome"

    // Example state for HealthMonitoringScreen (if needed)
    val stepCount = remember { mutableStateOf(0) }

    NavHost(navController = navController, startDestination = startDestination) {
        // Welcome screen (only shown when not logged in)
        composable("welcome") { WelcomeScreen(navController) }
        // Authentication routes (only shown when not logged in)
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        // Main app routes (for logged in users)
        composable("dashboard") { DashboardScreen(navController) }
        composable("consultation") { ConsultationScreen(navController) }
        composable("appointments") { AppointmentHistoryScreen(navController) }
        composable("monitoring") { HealthMonitoringScreen(navController, stepCount) }
        composable("profile") { ProfileScreen(navController) }
        composable("symptom_checker") { SymptomCheckerScreen(navController) }
        composable("prescriptions") { PrescriptionsScreen(navController) }
        composable("pharmacy") { PharmacyScreen(navController) }
        // New Health Records route
        composable("health_records") { HealthRecordsScreen(navController) }
        // Map Screen with an optional activity argument
        composable(
            route = "map_screen?activity={activity}",
            arguments = listOf(
                navArgument("activity") {
                    type = NavType.StringType
                    defaultValue = "running"
                }
            )
        ) { backStackEntry ->
            val activity = backStackEntry.arguments?.getString("activity") ?: "running"
            MapScreen(navController = navController, activity = activity)
        }
        // Routes with parameters
        composable(
            route = "videoCallScreen/{doctorName}",
            arguments = listOf(navArgument("doctorName") { type = NavType.StringType })
        ) { backStackEntry ->
            VideoCallScreen(
                navController = navController,
                doctorName = backStackEntry.arguments?.getString("doctorName") ?: ""
            )
        }
        composable(
            route = "chatScreen/{doctorName}",
            arguments = listOf(navArgument("doctorName") { type = NavType.StringType })
        ) { backStackEntry ->
            ChatScreen(
                navController = navController,
                doctorName = backStackEntry.arguments?.getString("doctorName") ?: ""
            )
        }
        composable(
            route = "appointmentBookingScreen/{doctorName}",
            arguments = listOf(navArgument("doctorName") { type = NavType.StringType })
        ) { backStackEntry ->
            AppointmentBookingScreen(
                navController = navController,
                doctorName = backStackEntry.arguments?.getString("doctorName") ?: ""
            )
        }
    }
}
