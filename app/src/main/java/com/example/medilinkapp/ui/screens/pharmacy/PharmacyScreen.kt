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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medilinkapp.data.Pharmacy
import com.example.medilinkapp.data.pharmacies

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacyScreen(navController: NavController) {
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
                    color = Color.White
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
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            item {
                Section(title = " Top Rated Pharmacies") {
                    PharmacyRow(
                        pharmacies.filter { it.name in listOf("MYDAWA", "Goodlife Pharmacy", "Portal Pharmacy") }
                    )
                }
            }
            item {
                Section(title = " Affordable Pharmacies") {
                    PharmacyRow(
                        pharmacies.filter { it.name in listOf("Pharmaplus Pharmacy", "Malibu Pharmacy", "Lifecare Pharmacy") }
                    )
                }
            }
            item {
                Section(title = "24/7 Pharmacies") {
                    PharmacyRow(
                        pharmacies.filter { it.name in listOf("Aga Khan University Hospital Pharmacy", "Checkups Medical Hub") }
                    )
                }
            }
            item {
                Section(title = " Nationwide Pharmacies") {
                    PharmacyRow(
                        pharmacies.filter { it.name in listOf("Haltons Pharmacy", "Lifecare Pharmacy") }
                    )
                }
            }
        }
    }
}


@Composable
fun Section(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(start = 16.dp, top = 12.dp)) {
        Text(
            title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            color = Color.Gray,
            thickness = 2.dp
        )

        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
fun PharmacyRow(pharmacyList: List<Pharmacy>) {
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
        items(pharmacyList) { pharmacy ->
            PharmacyCard(pharmacy)
        }
    }
}

@Composable
fun PharmacyCard(pharmacy: Pharmacy) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(140.dp)
            .padding(end = 12.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pharmacy.website))
                context.startActivity(intent)
            }
    ) {
        // Background Image
        Image(
            painter = painterResource(id = com.example.medilinkapp.R.drawable.pharm),
            contentDescription = "Pharmacy Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                    )
                )
        )
        // Pharmacy Details
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(pharmacy.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Visit Website", fontSize = 12.sp, color = Color.White)
        }
    }
}

// ✅ NEW: List of E-Pharmacy Websites
@Composable
fun WebsiteList(websites: List<Pair<String, String>>) {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        websites.forEach { website ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(website.second))
                        context.startActivity(intent)
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(website.first, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                    Text("Visit", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Blue)
                }
            }
        }
    }
}