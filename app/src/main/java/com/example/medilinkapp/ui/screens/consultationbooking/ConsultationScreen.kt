// Full Kotlin file with enhanced functionality

package com.example.medilinkapp.ui.screens.consultationbooking

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.medilinkapp.R
import com.example.medilinkapp.model.Doctor
import com.example.medilinkapp.repository.FirestoreRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.round
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultationScreen(navController: NavController) {
    val repository = FirestoreRepository()
    var doctors by remember { mutableStateOf(emptyList<Doctor>()) }
    var filteredDoctors by remember { mutableStateOf(emptyList<Doctor>()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("All") }
    var minRating by remember { mutableStateOf(3.5f) }

    val coroutineScope = rememberCoroutineScope()
    val visible = remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    fun loadDoctors() {
        coroutineScope.launch {
            try {
                isRefreshing = true
                delay(600)
                doctors = repository.getDoctors().map { doctor ->
                    doctor.copy(
                        drawableId = when (doctor.name) {
                            "Dr Sharon" -> R.drawable.doctor1
                            "Dr Kimani" -> R.drawable.doctor2
                            "Dr Muli" -> R.drawable.doctor3
                            else -> R.drawable.doctor2
                        }
                    )
                }
                filteredDoctors = doctors
                error = null
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
                isRefreshing = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadDoctors()
        visible.value = true
    }

    LaunchedEffect(searchQuery, selectedType, minRating, doctors) {
        filteredDoctors = doctors.filter {
            val rating = Random.nextDouble(3.8, 5.0).toFloat()
            val matchesName = it.name.contains(searchQuery, ignoreCase = true)
            val matchesType = selectedType == "All" || it.specialization.equals(selectedType, ignoreCase = true)
            val matchesRating = rating >= minRating
            matchesName && matchesType && matchesRating
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Consult a Doctor", style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = MaterialTheme.colorScheme.onPrimary
                        ))
                        Text("Expert help at your fingertips", style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        ))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A237E))
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount > 20) loadDoctors()
                    }
                }
        ) {
            AnimatedVisibility(
                visible = visible.value,
                enter = fadeIn(tween(600)) + slideInVertically(
                    initialOffsetY = { it / 4 },
                    animationSpec = tween(600)
                )
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Search by name", fontFamily = FontFamily.Serif) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        DoctorTypeDropdown(selectedType) { selectedType = it }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Minimum Rating: ${round(minRating * 10) / 10}", fontFamily = FontFamily.Serif)
                        Slider(
                            value = minRating,
                            onValueChange = { minRating = it },
                            valueRange = 3.5f..5f,
                            steps = 2
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Available Doctors", style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Serif,
                            color = MaterialTheme.colorScheme.secondary
                        ))
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    when {
                        isLoading -> item { LoadingState() }
                        error != null -> item { ErrorState(error!!, onRetry = { isLoading = true; loadDoctors() }) }
                        filteredDoctors.isEmpty() -> item { EmptyState() }
                        else -> items(filteredDoctors) { doctor ->
                            SmallDoctorCard(
                                doctor = doctor,
                                onVideoCall = { navController.navigate("videoCallScreen/${doctor.name}") },
                                onChat = { navController.navigate("chatScreen/${doctor.name}") },
                                onBookAppointment = { navController.navigate("appointmentBookingScreen/${doctor.name}") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorTypeDropdown(selected: String, onSelected: (String) -> Unit) {
    val types = listOf("All", "General", "Pediatrician", "Dentist", "Psychologist", "Dermatologist")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text("Filter by specialization", fontFamily = FontFamily.Serif) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            types.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        onSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text("Loading doctors...", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ErrorState(error: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Error: $error", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(modifier = Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Filled.Star, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("No doctors available.", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun SmallDoctorCard(
    doctor: Doctor,
    onVideoCall: () -> Unit,
    onChat: () -> Unit,
    onBookAppointment: () -> Unit
) {
    val rating = remember { Random.nextDouble(3.8, 5.0).toFloat() }
    val isAvailable = remember { Random.nextBoolean() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onVideoCall() }
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = doctor.drawableId ?: R.drawable.doctor2),
                contentDescription = null,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(doctor.name, style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                ))
                Text(doctor.specialization, style = MaterialTheme.typography.bodySmall)
                Text(doctor.experience, style = MaterialTheme.typography.bodySmall)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                    Text("$rating", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.size(10.dp).clip(CircleShape).background(if (isAvailable) Color.Green else Color.Red)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isAvailable) "Available" else "Busy", style = MaterialTheme.typography.bodySmall)
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onVideoCall) {
                    Icon(Icons.Outlined.VideoCall, contentDescription = "Video Call", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onChat) {
                    Icon(Icons.Outlined.Chat, contentDescription = "Chat", tint = MaterialTheme.colorScheme.secondary)
                }
                Button(onClick = onBookAppointment, shape = RoundedCornerShape(4.dp), contentPadding = PaddingValues(4.dp)) {
                    Text("Book", fontSize = 12.sp)
                }
            }
        }
    }
}
