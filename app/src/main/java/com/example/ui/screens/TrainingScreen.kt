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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.LoggedWorkoutEntity
import com.example.data.model.SampleData
import com.example.ui.AdidasEliteViewModel
import com.example.ui.theme.CharcoalGray
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MatteBlack
import com.example.ui.theme.SportsNeon
import com.example.ui.theme.TechSilver
import com.example.ui.theme.IceWhite

@Composable
fun TrainingScreen(
    viewModel: AdidasEliteViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Observe Room data
    val workoutsList by viewModel.workouts.collectAsState()
    val totalWorkouts by viewModel.totalWorkoutsLogged.collectAsState()
    val totalDistance by viewModel.totalDistanceKm.collectAsState()
    val totalMinutes by viewModel.totalMinutes.collectAsState()

    // Interactive logger states
    val logSport by viewModel.logSport.collectAsState()
    val logDuration by viewModel.logDuration.collectAsState()
    val logDistance by viewModel.logDistance.collectAsState()
    val logIntensity by viewModel.logIntensity.collectAsState()
    val logNotes by viewModel.logNotes.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MatteBlack)
            .statusBarsPadding()
            .padding(bottom = 76.dp)
            .verticalScroll(scrollState)
    ) {
        // HEADER TITLE
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "BIOMECHANIC TELEMETRY HUB",
                    style = MaterialTheme.typography.labelSmall,
                    color = SportsNeon,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "PERFORMANCE LABS",
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
                    imageVector = Icons.Default.Timeline,
                    contentDescription = "Stats Engine",
                    tint = SportsNeon,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // METRICS DASHBOARD ROW
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatsCard(
                label = "LOGS",
                value = "$totalWorkouts sec",
                icon = Icons.Default.DoneOutline,
                modifier = Modifier.weight(1f),
                testTag = "total_workouts_badge"
            )
            StatsCard(
                label = "DISTANCE",
                value = String.format("%.1f km", totalDistance),
                icon = Icons.Default.DirectionsRun,
                modifier = Modifier.weight(1.2f),
                testTag = "total_distance_badge"
            )
            StatsCard(
                label = "DURATION",
                value = "$totalMinutes min",
                icon = Icons.Default.Schedule,
                modifier = Modifier.weight(1.1f),
                testTag = "total_minutes_badge"
            )
        }

        // THERMAL RECO RECTANGLE
        DynamicTractionRecommendation(workoutsList)

        // LOG ACTION PANEL
        Text(
            text = "LOG RAW PERFORMANCE DATA",
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
                // Sport select
                Column {
                    Text(text = "SPORT DISCIPLINE", style = MaterialTheme.typography.labelSmall, color = TechSilver, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val sports = listOf("Running", "Football", "Training", "Cycling", "Hiking", "Gym")
                        sports.forEach { s ->
                            val isSel = logSport == s
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
                                    .clickable { viewModel.logSport.value = s }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("log_sport_chip_$s")
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

                // Hours and Minutes slider controls
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "DURATION: $logDuration MIN",
                            style = MaterialTheme.typography.labelSmall,
                            color = TechSilver,
                            fontFamily = FontFamily.Monospace
                        )
                        Slider(
                            value = logDuration.toFloatOrNull() ?: 30f,
                            onValueChange = { viewModel.logDuration.value = it.toInt().toString() },
                            valueRange = 5f..180f,
                            colors = SliderDefaults.colors(
                                thumbColor = SportsNeon,
                                activeTrackColor = ElectricBlue
                            )
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "DISTANCE: $logDistance KM",
                            style = MaterialTheme.typography.labelSmall,
                            color = TechSilver,
                            fontFamily = FontFamily.Monospace
                        )
                        Slider(
                            value = logDistance.toFloatOrNull() ?: 5f,
                            onValueChange = { viewModel.logDistance.value = String.format("%.1f", it) },
                            valueRange = 0f..25f,
                            colors = SliderDefaults.colors(
                                thumbColor = SportsNeon,
                                activeTrackColor = ElectricBlue
                            )
                        )
                    }
                }

                // INTENSITY RANGE SELECTOR
                Column {
                    Text(text = "EXERTION INTENSITY PROFILE", style = MaterialTheme.typography.labelSmall, color = TechSilver, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val intensities = listOf("Low", "Medium", "High", "Elite")
                        intensities.forEach { intensity ->
                            val isSel = logIntensity == intensity
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (isSel) ElectricBlue else Color.Black,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.logIntensity.value = intensity }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = intensity.uppercase(),
                                    color = if (isSel) Color.White else TechSilver,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                // Notes input field (Sleek filled style)
                Column {
                    Text(text = "BIOMECHANIC STRESS / ENVIRONMENT NOTES", style = MaterialTheme.typography.labelSmall, color = TechSilver, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = logNotes,
                        onValueChange = { viewModel.logNotes.value = it },
                        placeholder = { Text("e.g., Damp concrete, slight heel impact, wet turf", color = Color.Gray, fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Color.DarkGray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("log_notes_input"),
                        singleLine = true
                    )
                }

                // Submit Button
                Button(
                    onClick = { viewModel.logWorkout() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SportsNeon,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_workout_log_button")
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SYNC TELEMETRY FILE",
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // HISTORICAL LOGS
        Text(
            text = "TELEMETRY HISTOGRAM",
            style = MaterialTheme.typography.labelLarge,
            color = ElectricBlue,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 8.dp)
        )

        if (workoutsList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NO TELEMETRY LOGS RECORDED. RUN A CYCLE TO SYNC.",
                    color = Color.White.copy(alpha = 0.3f),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                workoutsList.forEach { log ->
                    WorkoutLogItemrow(log, onDelete = { viewModel.deleteLoggedWorkout(log.id) })
                }
            }
        }

        // INTEGRATED KNOWLEDGE PLATFORM ARTICLES (Sports Science)
        Text(
            text = "SPORTS SCIENCE RESEARCHED DOSSIERS",
            style = MaterialTheme.typography.labelLarge,
            color = ElectricBlue,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 12.dp)
        )

        DossierArticleItem(
            title = "KINETIC ENERGY TRANSFER IN 4D LATTICE MIDSOLES",
            author = "Dr. H. Weber, Herzogenaurach Lab",
            summary = "Measuring multi-axial vertical deceleration impacts on concrete versus asphalt reveals 4D polymer elasticity arrays return up to 88% kinetic energy while reducing tibial shock loads.",
            icon = Icons.Default.Science
        )

        DossierArticleItem(
            title = "POLAR INSULATION PROFILE UNDER SUBACTIVE CLIMATE INDEX",
            author = "Prof. S. Lindner, Alpine Tech Munich",
            summary = "Gore-Tex membrane microencapsulations change molecular alignment at -10°C, expanding wind block capability by 26% without restricting sweat moisture transpiration pathways.",
            icon = Icons.Default.Landscape
        )
    }
}

@Composable
private fun WorkoutLogItemrow(
    log: LoggedWorkoutEntity,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CharcoalGray, RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Sport Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.Black, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when (log.sportType) {
                    "Running" -> Icons.Default.DirectionsRun
                    "Football" -> Icons.Default.SportsFootball
                    "Hiking" -> Icons.Default.Landscape
                    "Cycling" -> Icons.Default.DirectionsBike
                    "Gym" -> Icons.Default.FitnessCenter
                    else -> Icons.Default.FitnessCenter
                },
                contentDescription = null,
                tint = SportsNeon,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Text Info
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = log.sportType.uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .background(
                            when (log.intensity) {
                                "Elite" -> SportsNeon.copy(alpha = 0.2f)
                                "High" -> ElectricBlue.copy(alpha = 0.2f)
                                else -> Color.DarkGray
                            },
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = log.intensity.uppercase(),
                        color = if (log.intensity == "Elite") SportsNeon else if (log.intensity == "High") ElectricBlue else Color.White,
                        fontSize = 7.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "${log.durationMin} min | ${log.distanceKm} km | ${log.caloriesBurned} cal",
                color = TechSilver,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 2.dp)
            )

            if (log.notes.isNotEmpty()) {
                Text(
                    text = log.notes,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete log",
                tint = Color(0xFFFC5A5A),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun DossierArticleItem(
    title: String,
    author: String,
    summary: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .background(CharcoalGray, RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ElectricBlue,
                modifier = Modifier.size(24.dp)
            )

            Column {
                Text(
                    text = author.uppercase(),
                    color = SportsNeon,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = TechSilver,
                    modifier = Modifier.padding(top = 4.dp),
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
private fun StatsCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Box(
        modifier = modifier
            .background(CharcoalGray, RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(alpha = 0.04f), RoundedCornerShape(12.dp))
            .padding(12.dp)
            .testTag(testTag)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    color = TechSilver,
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.sp
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ElectricBlue,
                    modifier = Modifier.size(12.dp)
                )
            }
            Text(
                text = value,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
private fun DynamicTractionRecommendation(workouts: List<LoggedWorkoutEntity>) {
    // Determine dynamic recommendations based on database logs
    val runningCount = workouts.count { it.sportType == "Running" }
    val hikeCount = workouts.count { it.sportType == "Hiking" }
    val fbCount = workouts.count { it.sportType == "Football" }

    var recoTitle = "BOOST SUSPENSION ADVICE"
    var recoDesc = "Ready for telemetry logs. Log athletic activities to receive compound recommendations."
    var suggestFootwear = "EQT CARBON 4D"

    when {
        runningCount >= hikeCount && runningCount >= fbCount && runningCount > 0 -> {
            recoTitle = "BOOST COMPOUND SUSPENSION ENGINE"
            recoDesc = "Heavy urban run schedule detected. We advise EQT CARBON 4D containing Futurecraft lattice architecture for premium knee mitigation and cobblestone absorption."
            suggestFootwear = "EQT CARBON 4D"
        }
        hikeCount > runningCount && hikeCount >= fbCount -> {
            recoTitle = "THERMAL PROTECTION ADVICE"
            recoDesc = "Alpine outdoor altitude hours accumulated. We recommend Monolith Alpine Shell system with active thermoregulation properties for harsh insulation."
            suggestFootwear = "MONOLITH ALPINE SHELL"
        }
        fbCount > runningCount && fbCount > hikeCount -> {
            recoTitle = "HIGH-VELOCITY ROTATIONAL TRACTION"
            recoDesc = "Match pitch sprint logs registered. Kaiser Elite footwear equipped with aerospace carbon traction spikes delivers direct pivot acceleration."
            suggestFootwear = "KAISER ELITE FG"
        }
        workouts.isNotEmpty() -> {
            recoTitle = "HYBRID ACTIVE PERFORMANCE"
            recoDesc = "Varied multi-discipline metabolic stressors registered. Prime R1 Hybrid Integrated Chassis ensures absolute flexibility across any metropolitan landscape."
            suggestFootwear = "PRIME R1 INTEGRATED CHASSIS"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFF0F1A2B), RoundedCornerShape(14.dp))
            .border(1.dp, ElectricBlue.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Biotech,
                    contentDescription = null,
                    tint = SportsNeon,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = recoTitle,
                    color = SportsNeon,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
            Text(
                text = recoDesc,
                style = MaterialTheme.typography.bodySmall,
                color = IceWhite,
                modifier = Modifier.padding(top = 4.dp),
                lineHeight = 15.sp
            )

            if (workouts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "RECOMMENDED HARDWARE: ",
                        fontSize = 8.sp,
                        color = TechSilver,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = suggestFootwear,
                        fontSize = 9.sp,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
