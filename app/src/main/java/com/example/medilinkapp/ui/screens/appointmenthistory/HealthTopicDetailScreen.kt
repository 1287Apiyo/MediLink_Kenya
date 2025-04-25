package com.example.medilinkapp.ui.screens.appointmenthistory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medilinkapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthTopicDetailScreen(topicName: String, navController: NavController) {
    val topicContent = healthTopicContents[topicName]!!
    val firstBannerImageRes = getBannerImage(topicName)
    val secondBannerImageRes = getSecondBannerImage(topicName)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topicName,
                        fontFamily = FontFamily.Serif,
                        color = Color.White
                    )
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF9F9F9))
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            // First Banner Image – full height, no cropping
            Image(
                painter = painterResource(id = firstBannerImageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(bottom = 20.dp),
                contentScale = ContentScale.FillWidth
            )

            var sectionCounter = 0
            topicContent.forEach { (sectionTitle, sectionContent) ->
                Text(
                    text = sectionTitle,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A237E)
                    ),
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
                )

                if (sectionTitle.contains("Resources", ignoreCase = true)) {
                    sectionContent.split("\n").forEach { line ->
                        val regex = Regex("""\[(.*?)\]\((.*?)\)""")
                        val match = regex.find(line)
                        if (match != null) {
                            val (linkText, url) = match.destructured
                            ClickableLink(linkText, url)
                        }
                    }
                } else {
                    sectionContent.trim().lines().forEach { line ->
                        if (line.startsWith("-")) {
                            BulletPointText(line.removePrefix("- ").trim())
                        } else {
                            Text(
                                text = line.trim(),
                                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.sp),
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }
                    }
                }

                sectionCounter++
                if (sectionCounter == 3) {
                    // Second Banner Image – also full height, no cropping
                    Image(
                        painter = painterResource(id = secondBannerImageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 20.dp),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun BulletPointText(text: String) {
    Row(modifier = Modifier.padding(bottom = 6.dp)) {
        Text("•", modifier = Modifier.padding(end = 8.dp), color = Color.Black)
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ClickableLink(text: String, url: String) {
    val uriHandler = LocalUriHandler.current
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = Color(0xFF1A73E8),
            textDecoration = TextDecoration.Underline
        ),
        modifier = Modifier
            .padding(bottom = 8.dp)
            .clickable { uriHandler.openUri(url) }
    )
}

@Composable
fun getBannerImage(topicName: String): Int {
    return when (topicName.lowercase()) {
        "sexual wellness" -> R.drawable.sexualhealth
        "mental health"    -> R.drawable.mental
        "diabetes"         -> R.drawable.diabetes
        "hypertension"     -> R.drawable.hypertension
        "asthma"           -> R.drawable.asthma
        "pediatrics"       -> R.drawable.pediatrics
        else               -> R.drawable.pharm
    }
}

@Composable
fun getSecondBannerImage(topicName: String): Int {
    return when (topicName.lowercase()) {
        "sexual wellness" -> R.drawable.sexualhealth2
        "mental health"    -> R.drawable.mental2
        "diabetes"         -> R.drawable.diabetes2
        "hypertension"     -> R.drawable.hypertension2
        "asthma"           -> R.drawable.asthma2
        "pediatrics"       -> R.drawable.pediatrics2
        else               -> R.drawable.pharm
    }
}
