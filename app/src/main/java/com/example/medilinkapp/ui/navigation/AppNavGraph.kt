import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.medilinkapp.ui.screens.prescriptions.PrescriptionsScreen
import com.example.medilinkapp.ui.screens.dashboard.DashboardScreen
import com.example.medilinkapp.ui.screens.consultationbooking.ConsultationScreen
import com.example.medilinkapp.ui.screens.consultationbooking.VideoCallScreen
import com.example.medilinkapp.ui.screens.consultationbooking.ChatScreen
import com.example.medilinkapp.ui.screens.appointmenthistory.AppointmentHistoryScreen
import com.example.medilinkapp.ui.screens.healthmonitoring.HealthMonitoringScreen
import com.example.medilinkapp.ui.screens.pharmacy.PharmacyScreen
import com.example.medilinkapp.ui.screens.profile.ProfileScreen
import com.example.medilinkapp.ui.screens.symptomchecker.SymptomCheckerScreen

@Composable
fun AppNavGraph(context: Context) {
    val navController = rememberNavController()

    // Store step count state here
    val stepCount = remember { mutableStateOf(0) }

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") { DashboardScreen(navController) }
        composable("consultation") { ConsultationScreen(navController) }
        composable("appointments") { AppointmentHistoryScreen(navController) }
        // Pass stepCount properly
        composable("monitoring") { HealthMonitoringScreen(navController, stepCount) }
        composable("profile") { ProfileScreen(navController) }
        composable("symptom_checker") { SymptomCheckerScreen(navController) }
        composable("prescriptions") { PrescriptionsScreen(navController) }
        composable("pharmacy") { PharmacyScreen(navController) }

        // New routes for VideoCall and Chat screens
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
    }
}
