package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.ui.AdidasEliteViewModel
import com.example.ui.components.ProductVectorRenderer
import com.example.ui.theme.CharcoalGray
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MatteBlack
import com.example.ui.theme.SportsNeon
import com.example.ui.theme.TechSilver
import com.example.ui.theme.IceWhite
import kotlinx.coroutines.launch

@Composable
fun ProductDetailScreen(
    product: ProductSystem,
    viewModel: AdidasEliteViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Observe size recommendation
    val recommendedSize by viewModel.recommendedFootwearSize.collectAsState()
    val recommendedApparelSize by viewModel.recommendedApparelSize.collectAsState()

    // Match appropriate recommendation based on product type
    val computedRecSizeString = if (product.category == "Footwear") {
        recommendedSize.toString()
    } else {
        recommendedApparelSize.split(" ")[0]
    }

    // Interactive sizing selection
    val sizesList = if (product.category == "Footwear") {
        listOf("7.5", "8.0", "8.5", "9.0", "9.5", "10.0", "10.5", "11.0", "11.5", "12.0")
    } else {
        listOf("S", "M", "L", "XL", "XXL")
    }

    // Set interactive initial size choice based on computed fit
    var selectedSize by remember { mutableStateOf(computedRecSizeString) }
    // Fallback if computed recommendation is not in static sizes
    LaunchedEffect(computedRecSizeString) {
        if (sizesList.contains(computedRecSizeString)) {
            selectedSize = computedRecSizeString
        }
    }

    // View Model states mapped to details layout
    val angle by viewModel.selected360Angle.collectAsState()
    val visualMode by viewModel.selectedVisualMode.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MatteBlack)
            .statusBarsPadding()
    ) {
        // TOP APP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .background(CharcoalGray, RoundedCornerShape(12.dp))
                    .testTag("detail_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to list",
                    tint = Color.White
                )
            }

            Text(
                text = "ENGINEERING LAB REPORT",
                style = MaterialTheme.typography.labelLarge,
                color = SportsNeon,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp
            )

            Icon(
                imageVector = Icons.Default.Analytics,
                contentDescription = "Lab telemetry",
                tint = SportsNeon,
                modifier = Modifier.size(24.dp)
            )
        }

        // SCROLLABLE CONTENT BODY
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            // HEADER IDENTITY CARD
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = product.subName,
                    style = MaterialTheme.typography.labelSmall,
                    color = SportsNeon,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = (-0.5).sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(ElectricBlue, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = product.collection,
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "€${product.price.toInt()}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // ROTATABLE 3D VISUALIZER STAGE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .background(Color(0xFF07080B), RoundedCornerShape(24.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
            ) {
                // Background radar/diagnostic lines
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val step = size.height / 8f
                    for (i in 1..7) {
                        drawLine(
                            color = Color.White.copy(alpha = 0.03f),
                            start = Offset(0f, i * step),
                            end = Offset(size.width, i * step),
                            strokeWidth = 1f
                        )
                    }
                }

                // Vector CAD-like Canvas drawing with 360-degree rotation support
                ProductVectorRenderer(
                    silhouetteId = product.vectorSilhouetteId,
                    angleDegrees = angle,
                    visualMode = visualMode,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                )

                // Top right diagnostic state pill
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(CharcoalGray, RoundedCornerShape(8.dp))
                        .border(1.dp, SportsNeon.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "SYS: $visualMode",
                        color = SportsNeon,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Drag gesture instructions overlay
                Text(
                    text = "SWIPE TO PIN 360° ENGINE",
                    color = Color.White.copy(alpha = 0.4f),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                )
            }

            // VISUAL MODE SELECTORS & SPIN CONTROL
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val modes = listOf("Solid", "Material Close-Up", "Wireframe X-Ray")
                modes.forEach { mode ->
                    val isSelected = visualMode == mode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (isSelected) ElectricBlue else CharcoalGray,
                                RoundedCornerShape(10.dp)
                            )
                            .border(
                                1.dp,
                                if (isSelected) SportsNeon else Color.Transparent,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { viewModel.selectedVisualMode.value = mode }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mode.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) Color.White else TechSilver,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Interactive spin slider dial (Porsche Dash style)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CharcoalGray, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.RotateLeft,
                    contentDescription = "Rotate",
                    tint = SportsNeon,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Slider(
                    value = angle,
                    onValueChange = { viewModel.selected360Angle.value = it },
                    valueRange = 0f..360f,
                    colors = SliderDefaults.colors(
                        thumbColor = SportsNeon,
                        activeTrackColor = ElectricBlue,
                        inactiveTrackColor = Color.DarkGray
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${angle.toInt()}°",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.Monospace
                )
            }

            // BIO-MECHANIC PERFORMANCE METRICS PANEL
            Text(
                text = "BIOMECHANIC DIAGNOSTICS",
                style = MaterialTheme.typography.labelLarge,
                color = ElectricBlue,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CharcoalGray, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricBar(name = "BREATHABILITY INDEX", value = product.metrics.breathabilityPercent, suffix = "% Flow")
                    MetricBar(name = "ENERGY RETURN (BOOST)", value = product.metrics.energyReturnPercent, suffix = "% Rebound")
                    MetricBar(name = "CHASSIS STABILITY", value = product.metrics.stabilityPercent, suffix = "% Pivot")
                    MetricBar(name = "WEATHER INSULATION", value = product.metrics.weatherResistancePercent, suffix = "% Barrier")
                    MetricBar(name = "STRUCTURAL DURABILITY", value = product.metrics.durabilityPercent, suffix = "% Wear")

                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.White.copy(alpha = 0.08f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "TOTAL SYSTEM WEIGHT", style = MaterialTheme.typography.labelSmall, color = TechSilver)
                            Text(
                                text = "${product.metrics.weightGrams} grams",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "INTEGRATION LEVEL", style = MaterialTheme.typography.labelSmall, color = TechSilver)
                            Text(
                                text = product.subName.substringAfter("Ref. "),
                                style = MaterialTheme.typography.titleMedium,
                                color = SportsNeon,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // CORE TECHNICAL INNOVATION GRID
            Text(
                text = "TECHNOLOGY ARCHITECTURE",
                style = MaterialTheme.typography.labelLarge,
                color = ElectricBlue,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(CharcoalGray, RoundedCornerShape(14.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = SportsNeon)
                        Text(
                            text = product.techPrimaryName,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = product.techDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = TechSilver,
                            modifier = Modifier.padding(top = 4.dp),
                            lineHeight = 15.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(CharcoalGray, RoundedCornerShape(14.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Icon(imageVector = Icons.Default.Architecture, contentDescription = null, tint = ElectricBlue)
                        Text(
                            text = product.techSecondaryName,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = "Micro-woven high tensile reinforcement polymers locking down movement axes during high-velocity torque motions.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TechSilver,
                            modifier = Modifier.padding(top = 4.dp),
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // GERMAN LABORATORY & ATHLETE INSIGHTS STORY
            Text(
                text = "ENGINEERING BACKSTORY",
                style = MaterialTheme.typography.labelLarge,
                color = ElectricBlue,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CharcoalGray, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "DESIGN INSPIRATION",
                        style = MaterialTheme.typography.labelSmall,
                        color = SportsNeon,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = product.engineeringStory,
                        style = MaterialTheme.typography.bodyMedium,
                        color = IceWhite,
                        lineHeight = 20.sp
                    )

                    Divider(color = Color.White.copy(alpha = 0.06f))

                    Text(
                        text = "RESEARCH & LAB SIMULATIONS",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricBlue,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = product.researchProcess,
                        style = MaterialTheme.typography.bodyMedium,
                        color = IceWhite,
                        lineHeight = 20.sp
                    )

                    Divider(color = Color.White.copy(alpha = 0.06f))

                    Text(
                        text = "ELITE ATHLETE LABORATORY FEEDBACK",
                        style = MaterialTheme.typography.labelSmall,
                        color = TechSilver,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = product.testingInsight,
                        style = MaterialTheme.typography.bodyMedium,
                        color = IceWhite,
                        lineHeight = 20.sp
                    )
                }
            }

            // SUSTAINABILITY TRANSPARENCY CARD
            Text(
                text = "ENGINEERED RESPONSIBILITY",
                style = MaterialTheme.typography.labelLarge,
                color = ElectricBlue,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(CharcoalGray, MatteBlack)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(Color.Black, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Co2,
                            contentDescription = null,
                            tint = ElectricBlue,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CARBON SYSTEM METRICS",
                            style = MaterialTheme.typography.labelSmall,
                            color = SportsNeon,
                            fontFamily = FontFamily.Monospace
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = "${product.sustainabilityCarbonKg} kg CO2e",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            VerticalDivider(modifier = Modifier.height(14.dp), color = Color.White.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "${product.recycledPercent}% RECYCLED",
                                style = MaterialTheme.typography.labelSmall,
                                color = SportsNeon,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Our products present sustainability as an engineered structural constraint. Materials are 100% trackable back to central recycling hubs in Germany.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TechSilver,
                            modifier = Modifier.padding(top = 4.dp),
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // SIZING SELECTION
            Text(
                text = "SELECT TECHNICAL SIZE",
                style = MaterialTheme.typography.labelLarge,
                color = ElectricBlue,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )

            // Advice matching Profile
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF10192C), RoundedCornerShape(12.dp))
                    .border(1.dp, ElectricBlue.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = null,
                    tint = ElectricBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "AI FIT ENGINE RECOMMENDATION",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricBlue,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Detected physical size ${computedRecSizeString} based on your active sports profile.",
                        style = MaterialTheme.typography.bodySmall,
                        color = IceWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sizes grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                sizesList.forEach { sizeOption ->
                    val isSelected = selectedSize == sizeOption
                    val isRecommended = computedRecSizeString == sizeOption
                    Box(
                        modifier = Modifier
                            .size(width = 75.dp, height = 48.dp)
                            .background(
                                if (isSelected) ElectricBlue else CharcoalGray,
                                RoundedCornerShape(10.dp)
                            )
                            .border(
                                1.dp,
                                if (isRecommended) SportsNeon else if (isSelected) ColorsBorderActive() else Color.White.copy(alpha = 0.05f),
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedSize = sizeOption }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = sizeOption,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (isRecommended) {
                                Text(
                                    text = "RECOMMENDED",
                                    fontSize = 7.sp,
                                    color = SportsNeon,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }

        // FOOTER CHECKOUT INTERACTION TRAY
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CharcoalGray,
            tonalElevation = 10.dp,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
        ) {
            Row(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "TOTAL SYSTEM SECURED",
                        style = MaterialTheme.typography.labelSmall,
                        color = TechSilver,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "€${product.price.toInt()}.00",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Button(
                    onClick = {
                        viewModel.addProductToCart(product, selectedSize)
                        coroutineScope.launch {
                            // Instant temporary tab redirect or just simple notification
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SportsNeon,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .height(54.dp)
                        .weight(1f)
                        .padding(start = 24.dp)
                        .testTag("add_to_cart_button")
                ) {
                    val cartBadgeItems by viewModel.cartItems.collectAsState()
                    val quantityForProd = cartBadgeItems.find { it.productId == product.id && it.size == selectedSize }?.quantity ?: 0
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (quantityForProd > 0) "SECURED (${quantityForProd})" else "SECURE TO BOUTIQUE",
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.SansSerif,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricBar(name: String, value: Int, suffix: String) {
    val progress by animateFloatAsState(targetValue = value / 100f, label = "progress")
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp
            )
            Text(
                text = "$value$suffix",
                color = SportsNeon,
                style = MaterialTheme.typography.labelMedium,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color.Black.copy(alpha = 0.4f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(ElectricBlue, SportsNeon)
                        )
                    )
            )
        }
    }
}

@Composable
fun ColorsBorderActive(): Color {
    return ElectricBlue
}
