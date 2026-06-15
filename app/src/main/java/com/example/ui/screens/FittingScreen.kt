package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.ui.components.ProductVectorRenderer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.FitProfileEntity
import com.example.data.model.SampleData
import com.example.ui.AdidasEliteViewModel
import com.example.ui.theme.CharcoalGray
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MatteBlack
import com.example.ui.theme.SportsNeon
import com.example.ui.theme.TechSilver

@Composable
fun FittingScreen(
    viewModel: AdidasEliteViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Observe DB Profile
    val activeProfile by viewModel.fitProfile.collectAsState()

    // Interactive view model text fields
    val hVal by viewModel.inputHeight.collectAsState()
    val wVal by viewModel.inputWeight.collectAsState()
    val flVal by viewModel.inputFootLength.collectAsState()
    val bodyVal by viewModel.inputBodyProfile.collectAsState()
    val sportVal by viewModel.inputSport.collectAsState()

    // Load DB values on first load
    LaunchedEffect(activeProfile) {
        viewModel.initFittingInputs(activeProfile)
    }

    // Recommended Size Logic results
    val recommendedSize by viewModel.recommendedFootwearSize.collectAsState()
    val recommendedApparel by viewModel.recommendedApparelSize.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MatteBlack)
            .statusBarsPadding()
            .padding(bottom = 76.dp)
            .verticalScroll(scrollState)
    ) {
        // SYSTEMS HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "BIOMETRIC SIZING TELE-PORT",
                    style = MaterialTheme.typography.labelSmall,
                    color = SportsNeon,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "AI FIT STUDIO",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Box(
                modifier = Modifier
                    .background(CharcoalGray, RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Biotech,
                    contentDescription = "Precision HUD",
                    tint = SportsNeon,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // OUTPUT SPEC READOUT (Porsche Dash Instrumentation style)
        Text(
            text = "ACTIVATED DIAGNOSTIC METRIC OUTPUT",
            style = MaterialTheme.typography.labelLarge,
            color = ElectricBlue,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(Color(0xFF070B13), RoundedCornerShape(16.dp))
                .border(2.dp, ElectricBlue.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(CharcoalGray, RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "RECOMMENDED SIZE",
                            color = TechSilver,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "US $recommendedSize",
                            color = SportsNeon,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = "FOOTWEAR CHASSIS",
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(CharcoalGray, RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "RECOMMENDED APPAREL",
                            color = TechSilver,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = recommendedApparel.substringBefore(" ("),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = "APPAREL SILHOUETTE",
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Divider(color = Color.White.copy(alpha = 0.05f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "INTEGRATION SYSTEM STATE", color = TechSilver, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    Text(
                        text = "ACTIVE SYNC: PROFILE L0${activeProfile.id}",
                        color = Color(0xFF4CAF50),
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // INPUT COEFFICIENTS PANEL
        Text(
            text = "CALIBRATE PHYSICAL COEFFICIENTS",
            style = MaterialTheme.typography.labelLarge,
            color = ElectricBlue,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(CharcoalGray, RoundedCornerShape(16.dp))
                .border(1.dp, Color.White.copy(alpha = 0.04f), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Height cm input
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "HEIGHT: $hVal CM",
                            style = MaterialTheme.typography.labelSmall,
                            color = TechSilver,
                            fontFamily = FontFamily.Monospace
                        )
                        Slider(
                            value = hVal.toFloatOrNull() ?: 180f,
                            onValueChange = { viewModel.inputHeight.value = it.toInt().toString() },
                            valueRange = 140f..220f,
                            colors = SliderDefaults.colors(
                                thumbColor = SportsNeon,
                                activeTrackColor = ElectricBlue
                            )
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "WEIGHT: $wVal KG",
                            style = MaterialTheme.typography.labelSmall,
                            color = TechSilver,
                            fontFamily = FontFamily.Monospace
                        )
                        Slider(
                            value = wVal.toFloatOrNull() ?: 75f,
                            onValueChange = { viewModel.inputWeight.value = it.toInt().toString() },
                            valueRange = 40f..140f,
                            colors = SliderDefaults.colors(
                                thumbColor = SportsNeon,
                                activeTrackColor = ElectricBlue
                            )
                        )
                    }
                }

                // Foot length input
                Column {
                    Text(
                        text = "FOOT LENGTH: $flVal CM",
                        style = MaterialTheme.typography.labelSmall,
                        color = TechSilver,
                        fontFamily = FontFamily.Monospace
                    )
                    Slider(
                        value = flVal.toFloatOrNull() ?: 27.5f,
                        onValueChange = { viewModel.inputFootLength.value = String.format("%.1f", it) },
                        valueRange = 21.0f..33.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = SportsNeon,
                            activeTrackColor = ElectricBlue
                        )
                    )
                }

                // Body structure select
                Column {
                    Text(text = "BODY STRUCTURAL PROFILE", style = MaterialTheme.typography.labelSmall, color = TechSilver, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val bodies = listOf("Athletic", "Lean", "Muscular", "Standard")
                        bodies.forEach { b ->
                            val isSel = bodyVal == b
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (isSel) ElectricBlue else Color.Black,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.inputBodyProfile.value = b }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = b.uppercase(),
                                    color = if (isSel) Color.White else TechSilver,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                // Target Sport Select
                Column {
                    Text(text = "PRIMARY SPEED DISCIPLINE", style = MaterialTheme.typography.labelSmall, color = TechSilver, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val sports = listOf("Running", "Football", "Training", "Hiking")
                        sports.forEach { s ->
                            val isSel = sportVal == s
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isSel) ElectricBlue else Color.Black,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSel) SportsNeon else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.inputSport.value = s }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = s.uppercase(),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                // Submit button to save to Room local database
                Button(
                    onClick = { viewModel.triggerFittingSave() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SportsNeon,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_fitting_btn")
                ) {
                    Icon(imageVector = Icons.Default.CloudSync, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "COMPUTE SYSTEM COEFFICIENTS",
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // DYNAMICS SPEC TAILORED SELECTIONS
        Text(
            text = "RECOMMENDED RE-ENFORCEMENTS",
            style = MaterialTheme.typography.labelLarge,
            color = ElectricBlue,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 4.dp)
        )
        Text(
            text = "Engineered hardware systems perfectly matched for raw $sportVal performance:",
            style = MaterialTheme.typography.bodySmall,
            color = TechSilver,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Find recommended product based on selection
            val filteredRecs = SampleData.PRODUCTS.filter {
                it.collection.contains(sportVal, ignoreCase = true) ||
                        it.name.contains(sportVal, ignoreCase = true) ||
                        (sportVal == "Running" && it.category == "Footwear" && it.id.contains("4d")) ||
                        (sportVal == "Hiking" && it.id.contains("monolith")) ||
                        (sportVal == "Football" && it.id.contains("kaiser")) ||
                        (sportVal == "Training" && it.id.contains("sweatshirt"))
            }

            val displayList = filteredRecs.ifEmpty { SampleData.PRODUCTS.take(2) }

            displayList.forEach { product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CharcoalGray, RoundedCornerShape(14.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(14.dp))
                        .clickable { viewModel.selectProduct(product) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(Color.Black, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        ProductVectorRenderer(
                            silhouetteId = product.vectorSilhouetteId,
                            angleDegrees = 45f,
                            visualMode = "Solid",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Optimized for high comfort stress at size US $recommendedSize",
                            color = TechSilver,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "€${product.price.toInt()}",
                            color = SportsNeon,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Diagnose product",
                        tint = SportsNeon,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
