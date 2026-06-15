package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProductSystem
import com.example.data.model.SampleData
import com.example.ui.AdidasEliteViewModel
import com.example.ui.components.ProductVectorRenderer
import com.example.ui.theme.CharcoalGray
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MatteBlack
import com.example.ui.theme.SportsNeon
import com.example.ui.theme.TechSilver

@Composable
fun CollectionsScreen(
    viewModel: AdidasEliteViewModel,
    modifier: Modifier = Modifier
) {
    val selectedCollection by viewModel.selectedCollection.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MatteBlack)
            .statusBarsPadding()
            .padding(bottom = 76.dp) // Offset for custom navigation bars
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
                    text = "CATALOGUE & CHASSIS FILES",
                    style = MaterialTheme.typography.labelSmall,
                    color = SportsNeon,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "ENGINEERED SYSTEMS",
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
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter Active",
                    tint = SportsNeon,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // COLLECTION CHIPS SELECTOR (Horizontal Scroll)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val colls = listOf("All") + SampleData.COLLECTIONS_LIST
            colls.forEach { coll ->
                val isSelected = selectedCollection == coll
                Box(
                    modifier = Modifier
                        .background(
                            if (isSelected) ElectricBlue else CharcoalGray,
                            RoundedCornerShape(10.dp)
                        )
                        .border(
                            1.dp,
                            if (isSelected) SportsNeon else Color.White.copy(alpha = 0.05f),
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { viewModel.selectCollectionFilter(coll) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("collection_chip_$coll")
                ) {
                    Text(
                        text = coll.uppercase(),
                        color = if (isSelected) Color.White else TechSilver,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // CATEGORY CHIPS SELECTOR (Tabs-like rows)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val categories = listOf("All", "Footwear", "Apparel")
            categories.forEach { cat ->
                val isSelected = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (isSelected) Color.White.copy(alpha = 0.08f) else CharcoalGray,
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            if (isSelected) ElectricBlue else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { viewModel.selectCategoryFilter(cat) }
                        .padding(vertical = 10.dp)
                        .testTag("category_chip_$cat"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cat.uppercase(),
                        color = if (isSelected) ElectricBlue else TechSilver,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // PRODUCTS VERTICAL GRID
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "NO HIGH-PERFORMANCE SCHEMATICS FOUND",
                        color = TechSilver,
                        style = MaterialTheme.typography.labelMedium,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Adjust filters to locate sports hardware systems",
                        color = Color.White.copy(alpha = 0.4f),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredProducts) { product ->
                    ProductGridCard(
                        product = product,
                        onClick = { viewModel.selectProduct(product) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductGridCard(
    product: ProductSystem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(CharcoalGray, RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.04f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(16.dp))
            .testTag("product_card_${product.id}")
    ) {
        Column {
            // Silhouette vector rendering container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(Color.Black)
            ) {
                ProductVectorRenderer(
                    silhouetteId = product.vectorSilhouetteId,
                    angleDegrees = 30f,
                    visualMode = "Solid",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                )

                // Top metric badge (weight)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${product.metrics.weightGrams}g",
                        color = SportsNeon,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Member-only tag
                if (product.isMemberOnly) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(ElectricBlue, RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "MEMBER",
                            color = Color.White,
                            fontSize = 7.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Information Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = product.subName.substringAfter("Ref. ").uppercase(),
                    color = ElectricBlue,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 1.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "€${product.price.toInt()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Co2,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "${product.sustainabilityCarbonKg}kg",
                            color = Color(0xFF81C784),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Divider(
                    color = Color.White.copy(alpha = 0.05f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DIAGNOSE",
                        color = SportsNeon,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Details",
                        tint = SportsNeon,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}
