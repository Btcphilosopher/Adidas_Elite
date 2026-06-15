package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AdidasEliteViewModel
import com.example.ui.theme.CharcoalGray
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MatteBlack
import com.example.ui.theme.SportsNeon
import com.example.ui.theme.TechSilver

@Composable
fun MembershipScreen(
    viewModel: AdidasEliteViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Analyze logged workouts to adaptively upgrade level tier
    val workouts by viewModel.workouts.collectAsState()
    val totalRuns = workouts.size

    val memberLevel = when {
        totalRuns >= 8 -> "LEVEL IV PLATINUM EXPERIMENTAL"
        totalRuns >= 5 -> "LEVEL III ELITE COMPACT"
        totalRuns >= 2 -> "LEVEL II INTEGRATED ACTIVE"
        else -> "LEVEL I SYSTEM CANDIDATE"
    }

    val earlyAccessKey = when {
        totalRuns >= 5 -> "ALPHA ACTIVE GOLD KEY"
        else -> "BETA SECURED CARGO KEY"
    }

    // Reservation locks states
    var reservedPorscheDrop by remember { mutableStateOf(false) }
    var reservedBerlinDrop by remember { mutableStateOf(false) }

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
                    text = "MEMBER PRIVILEGE KEYWAYS",
                    style = MaterialTheme.typography.labelSmall,
                    color = SportsNeon,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "MEMBERSHIP ECOSYSTEM",
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
                    imageVector = Icons.Default.CardMembership,
                    contentDescription = "Stored pass",
                    tint = SportsNeon,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // DELIVER ADIDAS BLACK CARD (Interactive geometric gradient, Porsche interiors look)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(210.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF13151A), Color(0xFF07080B))
                    ),
                    shape = RoundedCornerShape(18.dp)
                )
                .border(
                    BorderStroke(
                        width = 1.5.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(ElectricBlue, SportsNeon)
                        )
                    ),
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(18.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header of card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ADIDAS ELITE PILOT CARD",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp
                    )

                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "Pass barcode scanner",
                        tint = SportsNeon,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Adaptive Status Readout
                Column {
                    Text(
                        text = "GERMAN SPEED LEVEL",
                        color = TechSilver,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 8.sp
                    )
                    Text(
                        text = memberLevel,
                        color = SportsNeon,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 15.sp,
                        letterSpacing = (-0.2).sp
                    )
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "COMPUTED CYCLE SYNC TETHER: $totalRuns LOGS",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Footer of card containing barcode lines
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SECURITY ACCESS KEY",
                            color = TechSilver,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 7.sp
                        )
                        Text(
                            text = earlyAccessKey,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Barcode generator (Simulated via simple geometric lines)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        val barWidths = listOf(2, 4, 1, 3, 1, 4, 2, 1, 3, 2, 4)
                        barWidths.forEach { w ->
                            Box(
                                modifier = Modifier
                                    .width(w.dp)
                                    .fillMaxHeight()
                                    .background(Color.White.copy(alpha = 0.65f))
                            )
                        }
                    }
                }
            }
        }

        // PRIVILEGES AND BENEFITS LIST
        Text(
            text = "DEDICATED PRIVILEGES FOR YOUR LEVEL",
            style = MaterialTheme.typography.labelLarge,
            color = ElectricBlue,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(CharcoalGray, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                PrivilegeItem(
                    title = "Priority Early Release Reservation",
                    description = "Pre-lock limited countdown drops 12 hours before standard user releases. Zero queues.",
                    icon = Icons.Default.LockClock,
                    unlocked = true
                )

                PrivilegeItem(
                    title = "Sports Science Telemetry Advisory",
                    description = "Automated custom traction diagnostics comparing your database run stats directly with compound formulas.",
                    icon = Icons.Default.Analytics,
                    unlocked = totalRuns >= 2
                )

                PrivilegeItem(
                    title = "Priority German Lab Supporter Support Labs",
                    description = "Live priority messaging pipeline with expert physical scientists to consult hardware queries.",
                    icon = Icons.Default.HeadsetMic,
                    unlocked = totalRuns >= 5
                )

                PrivilegeItem(
                    title = "Exclusive Alpine Secret Expedition Drops",
                    description = "Secret curated physical systems reserved for Level IV platinum members only.",
                    icon = Icons.Default.FilterHdr,
                    unlocked = totalRuns >= 8
                )
            }
        }

        // EARLY RESERVATION DROPS LOCK SYSTEM (Countdown drops)
        Text(
            text = "RESERVE FUTURE COCHPIT SYSTEM DROPS",
            style = MaterialTheme.typography.labelLarge,
            color = ElectricBlue,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 4.dp)
        )
        Text(
            text = "Utilize early reservations so systems are dispatched instantly upon timer launch.",
            style = MaterialTheme.typography.bodySmall,
            color = TechSilver,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
        )

        // Drop reservation card 1
        DropReservationCard(
            title = "AEROLITE PORSCHE SPEED CHASSIS",
            sku = "SYSTEM CO-OP: AD-PORSCHE-01",
            countdown = "Departure in 6 Days",
            reserved = reservedPorscheDrop,
            onToggle = { reservedPorscheDrop = !reservedPorscheDrop }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Drop reservation card 2
        DropReservationCard(
            title = "STADT-DECEL CONCRETE MIDSOLE KIT",
            sku = "REF CODE: DE-CONCRETE-02",
            countdown = "Departure in 11 Days",
            reserved = reservedBerlinDrop,
            onToggle = { reservedBerlinDrop = !reservedBerlinDrop }
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
private fun PrivilegeItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    unlocked: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    if (unlocked) ElectricBlue.copy(alpha = 0.15f) else Color.Black,
                    RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (unlocked) ElectricBlue else Color.DarkGray,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = if (unlocked) Color.White else TechSilver,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Text(
                text = description,
                color = if (unlocked) TechSilver else Color.DarkGray,
                fontSize = 10.sp,
                lineHeight = 14.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        if (!unlocked) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Locked",
                tint = Color.DarkGray,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Unlocked",
                tint = ColorsCheckColor(),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun ColorsCheckColor(): Color {
    return Color(0xFF4CAF50)
}

@Composable
private fun DropReservationCard(
    title: String,
    sku: String,
    countdown: String,
    reserved: Boolean,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(CharcoalGray, RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (reserved) SportsNeon.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.03f),
                RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sku,
                    color = SportsNeon,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .background(Color.Black, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = countdown,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )

            Divider(color = Color.White.copy(alpha = 0.04f), modifier = Modifier.padding(vertical = 10.dp))

            Button(
                onClick = onToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (reserved) Color.Black else SportsNeon,
                    contentColor = if (reserved) SportsNeon else Color.Black
                ),
                border = if (reserved) BorderStroke(1.dp, SportsNeon) else null,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (reserved) Icons.Default.BookmarkAdded else Icons.Default.BookmarkAdd,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (reserved) "RESERVATION KEY DISPATCHED" else "DEPLOY EARLY ACCESS RESERVATION",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
