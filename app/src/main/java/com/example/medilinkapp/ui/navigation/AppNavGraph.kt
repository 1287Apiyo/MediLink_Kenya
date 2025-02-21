package com.example.medilinkapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medilinkapp.ui.screens.dashboard.DashboardScreen
import com.example.medilinkapp.ui.screens.consultationbooking.ConsultationScreen
import com.example.medilinkapp.ui.screens.appointmenthistory.AppointmentHistoryScreen
import com.example.medilinkapp.ui.screens.healthmonitoring.HealthMonitoringScreen
import com.example.medilinkapp.ui.screens.profile.ProfileScreen
import com.example.medilinkapp.ui.screens.symptomchecker.SymptomCheckerScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") { DashboardScreen(navController) }
        composable("consultation") { ConsultationScreen(navController) }
        composable("appointments") { AppointmentHistoryScreen(navController) }
        composable("monitoring") { HealthMonitoringScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("symptom_checker") { SymptomCheckerScreen(navController) }

    }
}
