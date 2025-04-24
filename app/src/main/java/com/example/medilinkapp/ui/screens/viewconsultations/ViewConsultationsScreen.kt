import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.medilinkapp.R
import com.example.medilinkapp.model.Consultation
import com.example.medilinkapp.viewmodel.ConsultationViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewConsultationsScreen(navController: NavHostController, viewModel: ConsultationViewModel) {
    LaunchedEffect(Unit) {
        viewModel.fetchConsultations()
    }

    val selectedTab = remember { mutableStateOf(0) }

    Scaffold(
        containerColor = Color.White, // White background
        topBar = {
            SmallTopAppBar(
                title = { Text("View Consultations") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, tint = Color.White, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF1A237E),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Tabs
            TabRow(selectedTabIndex = selectedTab.value) {
                Tab(text = { Text("Upcoming") }, selected = selectedTab.value == 0, onClick = { selectedTab.value = 0 })
                Tab(text = { Text("Past") }, selected = selectedTab.value == 1, onClick = { selectedTab.value = 1 })
            }

            val consultationsToShow = if (selectedTab.value == 0) {
                viewModel.consultations.filter { it.isUpcoming() }
            } else {
                viewModel.consultations.filter { !it.isUpcoming() }
            }

            if (consultationsToShow.isEmpty()) {
                // No consultations placeholder
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.naah), // Replace with your actual image
                        contentDescription = "No Consultations",
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (selectedTab.value == 0) "No upcoming consultations" else "No past consultations",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(consultationsToShow) { consultation ->
                        ConsultationItem(
                            consultation = consultation,
                            onClick = {
                                navController.navigate("consultation_detail/${consultation.id}")
                            },
                            onVideoCall = {
                                // Navigate to the Video Call screen with consultation ID
                                navController.navigate("video_call/${consultation.id}")
                            },
                            onChat = {
                                // Navigate to the Chat screen with consultation ID
                                navController.navigate("chat/${consultation.id}")
                            },
                            onDelete = {
                                // Call ViewModel function to delete the consultation
                                viewModel.deleteConsultation(it)
                            }
                        )
                    }
                }

            }

            // Optional Booking Button (kept visible regardless)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { navController.navigate("consultation_booking") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Book a New Consultation", color = Color.White)
            }
        }
    }
}

@Composable
fun ConsultationItem(
    consultation: Consultation,
    onClick: () -> Unit,
    onVideoCall: (Consultation) -> Unit,
    onChat: (Consultation) -> Unit,
    onDelete: (Consultation) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Doctor: ${consultation.doctorName}")
            Text("Category: ${consultation.category}")
            Text("Method: ${consultation.method}")
            Text("Date & Time: ${consultation.dateTime}")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onVideoCall(consultation) }) {
                    Icon(
                        imageVector = Icons.Filled.VideoCall,
                        contentDescription = "Video Call",
                        tint = Color(0xFF1A237E) // Blue
                    )
                }
                IconButton(onClick = { onChat(consultation) }) {
                    Icon(
                        imageVector = Icons.Filled.Chat,
                        contentDescription = "Chat",
                        tint = Color(0xFF1A237E) // Blue
                    )
                }
                IconButton(onClick = { onDelete(consultation) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFF1A237E) // Blue
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun Consultation.isUpcoming(): Boolean {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val consultationDateTime = LocalDateTime.parse(this.dateTime, formatter)
        consultationDateTime.isAfter(LocalDateTime.now())
    } catch (e: DateTimeParseException) {
        false // If parsing fails, consider it not upcoming
    }
}
