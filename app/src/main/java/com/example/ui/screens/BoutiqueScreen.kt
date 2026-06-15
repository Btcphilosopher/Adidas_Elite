package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CartItemEntity
import com.example.ui.AdidasEliteViewModel
import com.example.ui.MainTab
import com.example.ui.components.ProductVectorRenderer
import com.example.ui.theme.CharcoalGray
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MatteBlack
import com.example.ui.theme.SportsNeon
import com.example.ui.theme.IceWhite
import com.example.ui.theme.TechSilver
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun BoutiqueScreen(
    viewModel: AdidasEliteViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Observe DB Cart state
    val cartItems by viewModel.cartItems.collectAsState()
    val packaging by viewModel.customPackaging.collectAsState()

    // Simulation states
    val isCheckingOut by viewModel.isCheckingOut.collectAsState()
    val checkoutSuccess by viewModel.checkoutSuccess.collectAsState()

    // Animated loader values
    var loadingProgress by remember { mutableStateOf(0f) }
    var currentLoadingStep by remember { mutableStateOf("INITIATING MATRIX CHANNEL...") }

    // Surcharges
    val packagingCost = if (packaging.contains("Aluminum")) 25.0 else 0.0
    val shippingCost = 15.0 // Rapid German Air Delivery sCharge

    // Calculations
    val subtotal = cartItems.sumOf { it.price * it.quantity }
    val finalTotal = if (cartItems.isEmpty()) 0.0 else subtotal + packagingCost + shippingCost

    // Generate simulated order serial codes
    val orderId = remember(checkoutSuccess) { UUID.randomUUID().toString().substring(0, 13).uppercase() }

    // Start simulated payment loading progression
    LaunchedEffect(isCheckingOut) {
        if (isCheckingOut) {
            loadingProgress = 0f
            currentLoadingStep = "COMMENCING COCKPIT LINK..."
            delay(400)
            loadingProgress = 0.3f
            currentLoadingStep = "BIOMETRIC KEY ENCRYPTION ON-AIR..."
            delay(500)
            loadingProgress = 0.7f
            currentLoadingStep = "AUTHORIZING GERMAN LAB TRANSFERS..."
            delay(500)
            loadingProgress = 1.0f
            currentLoadingStep = "TRANSACTIONS REGISTERED PERFECTLY!"
            delay(300)
            viewModel.performSimulatedPurchase()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MatteBlack)
                .statusBarsPadding()
                .padding(bottom = 76.dp)
                .verticalScroll(scrollState)
        ) {
            // HEADER BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ACQUISITION MATRIX",
                        style = MaterialTheme.typography.labelSmall,
                        color = SportsNeon,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "BOUTIQUE BASKET",
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
                        imageVector = Icons.Default.Inventory,
                        contentDescription = "Stored items",
                        tint = SportsNeon,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // CARDS SHROUD
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = TechSilver,
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "NO HARNESSES REGISTERED FOR CHECKOUT",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Locate high-performance systems from Berlin, Football, and Alpine portfolios first.",
                            color = TechSilver,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.navigateToTab(MainTab.COLLECTIONS) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ElectricBlue,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "EXPEDITE CATALOGUE",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                // ACTIVE ITEMS ITERATOR
                Text(
                    text = "ACTIVE SHOPPING MANIFESTO",
                    style = MaterialTheme.typography.labelLarge,
                    color = ElectricBlue,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    cartItems.forEach { item ->
                        CartItemRow(
                            item = item,
                            onQtyChange = { rel -> viewModel.updateCartItemQuantity(item.id, rel) }
                        )
                    }
                }

                // CHOOSE CUSTOM PACKAGING SYSTEMS
                Text(
                    text = "SPECIFY PROTECTIVE CASING SYSTEM",
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
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        PackagingOptionCard(
                            title = "Standard Carbon-Neutral Deck",
                            description = "Form-fitted recycled cardboard fiber, certified zero carbon, lightweight (42g) - Included",
                            costString = "FREE",
                            isSelected = packaging.contains("Standard"),
                            onClick = { viewModel.selectPackaging("Standard Carbon-Neutral") }
                        )

                        PackagingOptionCard(
                            title = "Premium Milled Aluminum Pod Case",
                            description = "Industrial CNC laser-carved brushed aluminum pod box with dynamic shock foam lining (+380g) - Air-tight preservation",
                            costString = "€25.00",
                            isSelected = packaging.contains("Aluminum"),
                            onClick = { viewModel.selectPackaging("Premium Brushed Aluminum Case") }
                        )
                    }
                }

                // TRANSACTION RECEIPT CALCULATION
                Text(
                    text = "SECURE LEDGER BREAKDOWN",
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
                        .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Subtotal System Manifest", color = TechSilver, style = MaterialTheme.typography.bodySmall)
                            Text(text = "€$subtotal.00", color = Color.White, style = MaterialTheme.typography.labelLarge, fontFamily = FontFamily.Monospace)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Custom Pod Protection Case", color = TechSilver, style = MaterialTheme.typography.bodySmall)
                            Text(text = "€$packagingCost.00", color = Color.White, style = MaterialTheme.typography.labelLarge, fontFamily = FontFamily.Monospace)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Germany Rapid Air Courier", color = TechSilver, style = MaterialTheme.typography.bodySmall)
                            Text(text = "€$shippingCost.00", color = Color.White, style = MaterialTheme.typography.labelLarge, fontFamily = FontFamily.Monospace)
                        }

                        Divider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(vertical = 4.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "TOTAL METRIC SECURED", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            Text(text = "€$finalTotal.00", color = SportsNeon, fontSize = 20.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // EXPRESS TRANSACTION PORT BUTTON
                Button(
                    onClick = { viewModel.startCheckout() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SportsNeon,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp)
                        .testTag("one_tap_checkout_button")
                ) {
                    Icon(imageVector = Icons.Default.Fingerprint, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "ONE-TAP LAB DEPLOYMENT",
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }

        // COCKPIT LINK TRANSACTION LOADING SCREEN (Simulating checkout progress)
        AnimatedVisibility(
            visible = isCheckingOut,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MatteBlack.copy(alpha = 0.95f))
                    .clickable(enabled = false) {}, // Absorb interactions
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { loadingProgress },
                        color = SportsNeon,
                        strokeWidth = 6.dp,
                        modifier = Modifier.size(80.dp),
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "SECURE DEPLOYMENT TELEPORT ON-AIR",
                        color = SportsNeon,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.labelLarge,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = currentLoadingStep,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.DarkGray)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(loadingProgress)
                                .background(ElectricBlue)
                        )
                    }

                    Text(
                        text = "${(loadingProgress * 100).toInt()}% LOADED",
                        color = TechSilver,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // TRANS-RECEIPT POPUP DIALOGUE
        if (checkoutSuccess) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissCheckoutSuccess() },
                containerColor = CharcoalGray,
                shape = RoundedCornerShape(24.dp),
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Icon(imageVector = Icons.Default.OfflinePin, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "ORDER SECURED",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "CAD TRANSMID SYSTEM KEY",
                            color = TechSilver,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp
                        )
                        Text(
                            text = "#AD-ELITE-$orderId",
                            color = SportsNeon,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "German sports laboratories has initialized shipment packaging protocols. Recycled carbon containers with air-tight cases set to departure inside 2 hours.",
                            color = IceWhite,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.background(Color.Black, RoundedCornerShape(8.dp)).padding(8.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "DISPATCH HUB", color = TechSilver, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                Text(text = "HERZOGEN", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "TRANSIT PORT", color = TechSilver, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                Text(text = "GERMAN COURIER", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "DELIVERY MATRIX", style = MaterialTheme.typography.labelSmall, color = TechSilver, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                Text(text = "EXPRESS 24H", color = SportsNeon, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.dismissCheckoutSuccess() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "DISMISS DIAGNOSTIC RECEIPTS",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItemEntity,
    onQtyChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CharcoalGray, RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail Vector Canvas representer
        Box(
            modifier = Modifier
                .size(74.dp)
                .background(Color.Black, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            ProductVectorRenderer(
                silhouetteId = item.imageUrl,
                angleDegrees = 45f,
                visualMode = "Solid",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Title and meta sizes
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.productName,
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                Text(
                    text = "SPEC SIZE: ${item.size}",
                    color = TechSilver,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            Text(
                text = "€${item.price.toInt()}.00",
                color = SportsNeon,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Incremental state triggers
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = { onQtyChange(-1) },
                modifier = Modifier
                    .size(28.dp)
                    .background(Color.Black, RoundedCornerShape(6.dp))
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease qty", tint = Color.White, modifier = Modifier.size(12.dp))
            }

            Text(
                text = "${item.quantity}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp
            )

            IconButton(
                onClick = { onQtyChange(1) },
                modifier = Modifier
                    .size(28.dp)
                    .background(Color.Black, RoundedCornerShape(6.dp))
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase qty", tint = Color.White, modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
private fun PackagingOptionCard(
    title: String,
    description: String,
    costString: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Color.Black else Color(0xFF13151A), RoundedCornerShape(10.dp))
            .border(1.dp, if (isSelected) SportsNeon else Color.Transparent, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = SportsNeon,
                unselectedColor = Color.DarkGray
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = if (isSelected) Color.White else TechSilver,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Text(
                text = description,
                color = TechSilver.copy(alpha = 0.7f),
                fontSize = 10.sp,
                lineHeight = 14.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Text(
            text = costString,
            color = if (isSelected) SportsNeon else TechSilver,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
