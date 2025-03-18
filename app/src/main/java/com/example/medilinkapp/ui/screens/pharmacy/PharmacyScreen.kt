package com.example.medilinkapp.ui.screens.pharmacy

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medilinkapp.data.Pharmacy
import com.example.medilinkapp.data.pharmacies
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.example.medilinkapp.R
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder


import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacyScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var favoritePharmacies by remember { mutableStateOf(setOf<String>()) }
    var selectedSort by remember { mutableStateOf("Name") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    "Pharmacy Services",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily.Serif
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Implement filter logic */ }) {
                    Icon(imageVector = Icons.Filled.FavoriteBorder, contentDescription = "Filter")
                }
                IconButton(onClick = { /* Implement sorting logic */ }) {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = "Sort")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A237E))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Pharmacies") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        val filteredPharmacies = pharmacies.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }

        LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Section(title = "Popular & Affordable Pharmacies") {
                    PharmacyRow(
                        filteredPharmacies.filter {
                            it.name in listOf(
                                "MYDAWA", "Goodlife Pharmacy", "Portal Pharmacy",
                                "Pharmaplus Pharmacy", "Malibu Pharmacy", "Lifecare Pharmacy"
                            )
                        },
                        favoritePharmacies
                    )
                }
            }
            item {
                Section(title = "24/7 & Nationwide Pharmacies") {
                    PharmacyRow(
                        filteredPharmacies.filter {
                            it.name in listOf(
                                "Aga Khan University Hospital Pharmacy", "Checkups Medical Hub",
                                "Haltons Pharmacy", "Lifecare Pharmacy"
                            )
                        },
                        favoritePharmacies
                    )
                }
            }
        }
    }
}
@Composable
fun Section(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,  // Slightly smaller for a cleaner look
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Serif,  // Times New Roman style
            color = Color(0xFF37474F),  // Dark gray-blue instead of bright white
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Divider(color = Color(0xFFBDBDBD), thickness = 1.dp) // Light gray divider for separation

        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}
@Composable
fun PharmacyRow(pharmacyList: List<Pharmacy>, favoritePharmacies: Set<String>) {
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
        items(pharmacyList) { pharmacy ->
            PharmacyCard(pharmacy, favoritePharmacies)
        }
    }
}

@Composable
fun PharmacyCard(pharmacy: Pharmacy, favoritePharmacies: Set<String>) {
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(pharmacy.name in favoritePharmacies) }

    Card(
        modifier = Modifier
            .width(220.dp)
            .height(220.dp)
            .padding(end = 16.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pharmacy.website))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box {
            Image(
                painter = painterResource(id = pharmacy.imageResId),
                contentDescription = "Pharmacy Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    pharmacy.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Visit Website",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Serif
                )
            }
            IconButton(
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
        }
    }
}


