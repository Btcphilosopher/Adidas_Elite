package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AdidasEliteViewModel
import com.example.ui.MainTab
import com.example.ui.theme.CharcoalGray
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MatteBlack
import com.example.ui.theme.SportsNeon
import com.example.ui.theme.TechSilver
import androidx.compose.foundation.clickable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainAppContainer(
    viewModel: AdidasEliteViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()

    val totalCartCount = cartItems.sumOf { it.quantity }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MatteBlack)
    ) {
        // CONTENT COMPOSABLE SWITCHER
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentTab) {
                MainTab.HOME -> HomeScreen(viewModel = viewModel)
                MainTab.COLLECTIONS -> CollectionsScreen(viewModel = viewModel)
                MainTab.TRAINING -> TrainingScreen(viewModel = viewModel)
                MainTab.FITTING -> FittingScreen(viewModel = viewModel)
                MainTab.BOUTIQUE -> BoutiqueScreen(viewModel = viewModel)
                MainTab.MEMBERSHIP -> MembershipScreen(viewModel = viewModel)
            }
        }

        // FLOATING PILOT NAVIGATION SYSTEM BAR (Bottom Dock)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                .background(Color.Black.copy(alpha = 0.85f), RoundedCornerShape(18.dp))
                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(18.dp))
                .height(68.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationItem(
                    tab = MainTab.HOME,
                    currentTab = currentTab,
                    icon = Icons.Default.Layers,
                    label = "LAB",
                    onClick = { viewModel.navigateToTab(MainTab.HOME) },
                    testTag = "nav_tab_home"
                )

                NavigationItem(
                    tab = MainTab.COLLECTIONS,
                    currentTab = currentTab,
                    icon = Icons.Default.GridView,
                    label = "KITS",
                    onClick = { viewModel.navigateToTab(MainTab.COLLECTIONS) },
                    testTag = "nav_tab_collections"
                )

                NavigationItem(
                    tab = MainTab.TRAINING,
                    currentTab = currentTab,
                    icon = Icons.Default.Speed,
                    label = "BIO",
                    onClick = { viewModel.navigateToTab(MainTab.TRAINING) },
                    testTag = "nav_tab_training"
                )

                NavigationItem(
                    tab = MainTab.FITTING,
                    currentTab = currentTab,
                    icon = Icons.Default.Fitbit,
                    label = "FIT",
                    onClick = { viewModel.navigateToTab(MainTab.FITTING) },
                    testTag = "nav_tab_fitting"
                )

                NavigationItem(
                    tab = MainTab.BOUTIQUE,
                    currentTab = currentTab,
                    icon = Icons.Default.LocalMall,
                    label = "STORE",
                    onClick = { viewModel.navigateToTab(MainTab.BOUTIQUE) },
                    badgeCount = totalCartCount,
                    testTag = "nav_tab_boutique"
                )

                NavigationItem(
                    tab = MainTab.MEMBERSHIP,
                    currentTab = currentTab,
                    icon = Icons.Default.Key,
                    label = "ELITE",
                    onClick = { viewModel.navigateToTab(MainTab.MEMBERSHIP) },
                    testTag = "nav_tab_membership"
                )
            }
        }

        // FULL SCREEN OVERLAY: PRODUCT DETAIL COCKPIT DIAGNOSTICS report
        AnimatedVisibility(
            visible = selectedProduct != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            selectedProduct?.let { product ->
                ProductDetailScreen(
                    product = product,
                    viewModel = viewModel,
                    onBack = { viewModel.selectProduct(null) }
                )
            }
        }
    }
}

@Composable
fun RowScope.NavigationItem(
    tab: MainTab,
    currentTab: MainTab,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    badgeCount: Int = 0,
    testTag: String = ""
) {
    val isSelected = currentTab == tab
    val iconColor = if (isSelected) SportsNeon else TechSilver
    val textFontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Transparent)
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Box {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )

                // Render dynamic cart counting badge if badgeCount > 0
                if (badgeCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 8.dp, y = (-6).dp)
                            .background(SportsNeon, CircleShape)
                            .size(15.dp)
                            .border(1.dp, Color.Black, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = badgeCount.toString(),
                            color = Color.Black,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = label,
                color = if (isSelected) Color.White else TechSilver,
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = textFontWeight
            )

            if (isSelected) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .background(SportsNeon, RoundedCornerShape(1.dp))
                        .width(14.dp)
                        .height(2.dp)
                )
            }
        }
    }
}
