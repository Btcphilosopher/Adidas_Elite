package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProductSystem
import com.example.data.model.SampleData
import com.example.ui.AdidasEliteViewModel
import com.example.ui.MainTab
import com.example.ui.components.ProductVectorRenderer
import com.example.ui.theme.CharcoalGray
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MatteBlack
import com.example.ui.theme.SportsNeon
import com.example.ui.theme.TechSilver

@Composable
fun HomeScreen(
    viewModel: AdidasEliteViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Real-time ticking simulated countdown for the Limited Drops
    var countdownSeconds by remember { mutableStateOf(172800) } // 48 Hours
    LaunchedEffect(Unit) {
        while (countdownSeconds > 0) {
            kotlinx.coroutines.delay(1000)
            countdownSeconds--
        }
    }

    val hours = countdownSeconds / 3600
    val minutes = (countdownSeconds % 3600) / 60
    val seconds = countdownSeconds % 60
    val countdownFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MatteBlack)
            .verticalScroll(scrollState)
            .padding(bottom = 76.dp) // Cushion for bottom bar
    ) {
        // RADAR HEADER BANNER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(290.dp)
        ) {
            // Background futuristic grid canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val step = size.width / 16f
                for (i in 0..16) {
                    drawLine(
                        color = ElectricBlue.copy(alpha = 0.04f),
                        start = Offset(i * step, 0f),
                        end = Offset(i * step, size.height),
                        strokeWidth = 1f
                    )
                }
                drawLine(
                    color = SportsNeon.copy(alpha = 0.15f),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 3f
                )
            }

            // Foreground brush gradient mapping
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MatteBlack.copy(alpha = 0.6f),
                                MatteBlack
                            )
                        )
                    )
            )

            // Dynamic header content (Berlin Flagship concept)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "BERLIN HUB // GERMAN ENGINEERING",
                    style = MaterialTheme.typography.labelSmall,
                    color = SportsNeon,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "ADIDAS ELITE",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = (-1.5).sp
                )
                Text(
                    text = "PERFORMANCE IS ENGINEERED. LUXURY IS PRECISION.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { viewModel.navigateToTab(MainTab.COLLECTIONS) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricBlue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("explore_collections_button")
                    ) {
                        Text(
                            text = "EXPLORE SYSTEMS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Button(
                        onClick = { viewModel.navigateToTab(MainTab.FITTING) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CharcoalGray,
                            contentColor = SportsNeon
                        ),
                        border = BorderStroke(1.dp, SportsNeon.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("home_fitting_redirect_button")
                    ) {
                        Text(
                            text = "AI FIT STUDIO",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // FEATURED DROPS countdown banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(CharcoalGray, MatteBlack)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.2f)), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(SportsNeon, RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "NEXT-GEN LAB DROP LIVE",
                            style = MaterialTheme.typography.labelMedium,
                            color = SportsNeon,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(Color.Black, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = countdownFormatted,
                            color = SportsNeon,
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Text(
                    text = "EQT CARBON 4D // GERMAN SPEED GRID",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "Strictly limited to 400 serial numbered packages in Berlin. Tuned lattice carbon grids for ultimate deceleration kinetics.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val eqtProduct = SampleData.PRODUCTS.firstOrNull { it.id == "product_eqt_carbon_4d" }
                        if (eqtProduct != null) {
                            viewModel.selectProduct(eqtProduct)
                        } else {
                            viewModel.navigateToTab(MainTab.COLLECTIONS)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SportsNeon,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(
                        text = "VIEW DIAGNOSTICS & ENROLL",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.labelMedium,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // HORIZONTAL SPORTS LAB GALLERIES
        Text(
            text = "ENGINEERED SYSTEMS // THE COLLECTIONS",
            style = MaterialTheme.typography.labelLarge,
            color = ElectricBlue,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 12.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val colls = listOf(
                "Berlin Performance" to "Urban Architecture & Lightweight Grid Speeds",
                "Alpine Engineering" to "Extremity Mountaineering Thermals & Wind Walls",
                "Football Heritage" to "Surgical Pitch Command & SpeedFrame Carbon Studs",
                "Future Motion" to "Sports Science Laboratories & Generative Biomechanics"
            )
            items(colls) { (name, desc) ->
                Box(
                    modifier = Modifier
                        .width(260.dp)
                        .height(160.dp)
                        .background(CharcoalGray, RoundedCornerShape(16.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                        .clickable {
                            viewModel.selectCollectionFilter(name)
                            viewModel.navigateToTab(MainTab.COLLECTIONS)
                        }
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(ElectricBlue.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (name) {
                                        "Berlin Performance" -> Icons.Default.DirectionsRun
                                        "Alpine Engineering" -> Icons.Default.Landscape
                                        "Football Heritage" -> Icons.Default.SportsFootball
                                        else -> Icons.Default.Analytics
                                    },
                                    contentDescription = null,
                                    tint = ElectricBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = name.uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                            )
                        }

                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f),
                            lineHeight = 15.sp
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "OPEN CORE CATALOGUE",
                                style = MaterialTheme.typography.labelSmall,
                                color = SportsNeon,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = null,
                                tint = SportsNeon,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }

        // FLAGSHIP PRODUCTS SHOWCASE
        Text(
            text = "LAB SELECTIONS",
            style = MaterialTheme.typography.labelLarge,
            color = ElectricBlue,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 12.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SampleData.PRODUCTS.take(3).forEach { product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CharcoalGray, RoundedCornerShape(16.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                        .clickable { viewModel.selectProduct(product) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(Color.Black, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        ProductVectorRenderer(
                            silhouetteId = product.vectorSilhouetteId,
                            angleDegrees = 45f,
                            visualMode = "Solid",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.subName,
                            style = MaterialTheme.typography.labelSmall,
                            color = SportsNeon,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp
                        )
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = "€${product.price.toInt()}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .background(ElectricBlue.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = product.category.uppercase(),
                                    color = ElectricBlue,
                                    fontSize = 7.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Details",
                        tint = TechSilver,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // PORSCHE / FUTURISTIC TRAINING TELEMETRY SHORTCUT
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(CharcoalGray, MatteBlack)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(1.dp, ElectricBlue.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                .clickable { viewModel.navigateToTab(MainTab.TRAINING) }
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "GERMAN SPORTS LABS",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricBlue,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "BIO-TELEMETRY & LOGS",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Log workout stress indexes to receive customized footwear traction compound and thermal layer recommendations.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = SportsNeon,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
