package com.example.medilinkapp.ui.navigation

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.medilinkapp.ui.screens.*
import com.example.medilinkapp.ui.screens.appointmenthistory.AppointmentHistoryScreen
import com.example.medilinkapp.ui.screens.consultationbooking.AppointmentBookingScreen
import com.example.medilinkapp.ui.screens.consultationbooking.ChatScreen
import com.example.medilinkapp.ui.screens.consultationbooking.ConsultationScreen
import com.example.medilinkapp.ui.screens.consultationbooking.VideoCallScreen
import com.example.medilinkapp.ui.screens.dashboard.DashboardScreen
import com.example.medilinkapp.ui.screens.dashboard.LoadingConsultationScreen
import com.example.medilinkapp.ui.screens.dashboard.MonitoringLoadingScreen
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(context: Context) {
    val navController = rememberNavController()
    val startDestination =
        if (FirebaseAuth.getInstance().currentUser != null) "dashboard" else "welcome"
    val stepCount = remember { mutableStateOf(0) }

    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("welcome") { WelcomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("loading-consultation") { LoadingConsultationScreen(navController) }
        composable("consultation") {  ConsultationScreen(navController = navController) }
        composable("appointments") { AppointmentHistoryScreen(navController) }
        composable("loading-monitoring") { MonitoringLoadingScreen(navController) }
        composable("monitoring") { HealthMonitoringScreen(navController, stepCount) }
        composable("profile") { ProfileScreen(navController) }
        composable("symptom_checker") { SymptomCheckerScreen(navController) }
        composable("prescriptions") { PrescriptionsScreen(navController) }

        // ✅ Animated transition for Pharmacy screen
        composable(
            route = "pharmacy",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(500)
                ) + fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(500)
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            }
        ) {
            PharmacyScreen(navController)
        }

        composable("health_records") { HealthRecordsScreen(navController) }

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
