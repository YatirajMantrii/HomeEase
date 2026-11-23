package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.RequestQuote
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.WorkHistory
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CatalogData
import com.example.ui.components.HomeEaseSubTopBar
import com.example.ui.components.PressablePrimaryButton
import com.example.ui.components.ProfessionalSummaryCard
import com.example.ui.theme.AmberGold
import com.example.ui.theme.DeepForest
import com.example.ui.theme.MutedOlive
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SoftSandLight
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.WarmIvory
import com.example.ui.viewmodel.HomeEaseUiState
import com.example.ui.viewmodel.HomeEaseViewModel

@Composable
fun ProfessionalListingScreen(
    initialCategoryId: String,
    state: HomeEaseUiState,
    onBack: () -> Unit,
    onViewProProfile: (String) -> Unit,
    onToggleShortlist: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(initialCategoryId) }
    var sortBy by remember { mutableStateOf("Rating") }

    val filtered = state.professionals
        .filter { pro ->
            (selectedCategory == "all" || pro.categoryId == selectedCategory) &&
                (state.selectedLocality.startsWith("Pune") ||
                    pro.serviceAreasCsv.contains(state.selectedLocality, ignoreCase = true))
        }
        .let { list ->
            when (sortBy) {
                "Experience" -> list.sortedByDescending { it.experienceYears }
                "Price" -> list.sortedBy { it.startingPriceInr }
                else -> list.sortedByDescending { it.rating }
            }
        }

    Scaffold(
        containerColor = WarmIvory,
        topBar = {
            HomeEaseSubTopBar(title = "Local Professionals", onBack = onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedCategory == "all",
                            onClick = { selectedCategory = "all" },
                            label = { Text("All Services") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DeepForest,
                                selectedLabelColor = PureWhite
                            )
                        )
                    }
                    items(CatalogData.categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat.id,
                            onClick = { selectedCategory = cat.id },
                            label = { Text(cat.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DeepForest,
                                selectedLabelColor = PureWhite
                            )
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sort:", style = MaterialTheme.typography.labelMedium, color = MutedOlive)
                    listOf("Rating", "Experience", "Price").forEach { option ->
                        FilterChip(
                            selected = sortBy == option,
                            onClick = { sortBy = option },
                            label = { Text(option) }
                        )
                    }
                }
            }

            if (filtered.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = PureWhite,
                        border = BorderStroke(1.dp, SubtleBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("No professionals match this exact filter", style = MaterialTheme.typography.titleMedium, color = DeepForest)
                            Text("Try selecting \"All Services\" or broadening your Pune locality.", style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                        }
                    }
                }
            } else {
                items(filtered, key = { it.id }) { pro ->
                    ProfessionalSummaryCard(
                        pro = pro,
                        isShortlisted = pro.id in state.shortlistedIds,
                        onToggleShortlist = { onToggleShortlist(pro.id) },
                        onViewProfile = { onViewProProfile(pro.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfessionalProfileScreen(
    proId: String,
    viewModel: HomeEaseViewModel,
    state: HomeEaseUiState,
    onBack: () -> Unit,
    onRequestQuoteForPro: (categoryId: String, proId: String) -> Unit
) {
    val pro by viewModel.observeProfessional(proId).collectAsStateWithLifecycle(initialValue = null)
    val reviews by viewModel.observeReviews(proId).collectAsStateWithLifecycle(initialValue = emptyList())

    LaunchedEffect(proId) {
        viewModel.recordRecentlyViewed(proId)
    }

    val isShortlisted = proId in state.shortlistedIds

    Scaffold(
        containerColor = WarmIvory,
        topBar = {
            HomeEaseSubTopBar(
                title = pro?.name ?: "Professional Profile",
                onBack = onBack,
                actions = {
                    IconButton(onClick = { viewModel.toggleShortlist(proId) }) {
                        Icon(
                            imageVector = if (isShortlisted) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = "Toggle shortlist",
                            tint = if (isShortlisted) Color(0xFFC45B4B) else DeepForest
                        )
                    }
                }
            )
        }
    ) { padding ->
        val p = pro
        if (p == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading profile...", color = MutedOlive)
            }
        } else {
            val category = CatalogData.categoryById(p.categoryId)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = DeepForest,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(66.dp)
                                        .clip(CircleShape)
                                        .background(SoftSandLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = p.name.split(" ").take(2).joinToString("") { it.take(1) },
                                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                        color = DeepForest
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(p.name, style = MaterialTheme.typography.headlineSmall, color = WarmIvory)
                                    Text(p.businessName, style = MaterialTheme.typography.bodyMedium, color = SoftSandLight)
                                    Text(category.name, style = MaterialTheme.typography.labelMedium, color = SageGreenContainer)
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                StatPill(icon = Icons.Rounded.Star, text = "${p.rating} (${p.reviewCount})", iconTint = AmberGold)
                                StatPill(icon = Icons.Rounded.WorkHistory, text = "${p.experienceYears} yrs • ${p.completedJobs} jobs", iconTint = SoftSandLight)
                            }

                            Text(p.bio, style = MaterialTheme.typography.bodyMedium, color = WarmIvory.copy(alpha = 0.9f))

                            PressablePrimaryButton(
                                text = "Request Quote (${if (p.pricingModel == "Quote-Based") "Custom Estimate" else "From ₹${p.startingPriceInr}"})",
                                onClick = { onRequestQuoteForPro(p.categoryId, p.id) },
                                icon = Icons.Rounded.RequestQuote,
                                containerColor = SoftSandLight,
                                contentColor = DeepForest,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("pro_profile_request_quote")
                            )
                        }
                    }
                }

                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = PureWhite,
                        border = BorderStroke(1.dp, SubtleBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = SageGreen)
                                Text("Pune Service Areas", style = MaterialTheme.typography.titleMedium, color = DeepForest)
                            }
                            Text(p.serviceAreasCsv, style = MaterialTheme.typography.bodyMedium, color = MutedOlive)

                            Spacer(Modifier.height(4.dp))
                            Text("Specialties & Skills", style = MaterialTheme.typography.titleMedium, color = DeepForest)
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                p.skillsCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() }.forEach { skill ->
                                    Surface(shape = RoundedCornerShape(50), color = SageGreenContainer) {
                                        Text(
                                            text = skill,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = DeepForest,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(4.dp))
                            Text("Recent Pune Portfolio", style = MaterialTheme.typography.titleMedium, color = DeepForest)
                            p.portfolioHighlightsCsv.split("|").filter { it.isNotBlank() }.forEach { highlight ->
                                Surface(shape = RoundedCornerShape(12.dp), color = WarmIvory) {
                                    Text(
                                        text = "• $highlight",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = DeepForest,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Customer Reviews (${reviews.size})",
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepForest
                    )
                }

                if (reviews.isEmpty()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = PureWhite,
                            border = BorderStroke(1.dp, SubtleBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No written reviews yet for this newly onboarded local profile.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MutedOlive,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                } else {
                    items(reviews, key = { it.id }) { rev ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = PureWhite,
                            border = BorderStroke(1.dp, SubtleBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(rev.customerName, style = MaterialTheme.typography.titleSmall, color = DeepForest)
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Icon(Icons.Rounded.Star, contentDescription = null, tint = AmberGold, modifier = Modifier.size(16.dp))
                                        Text("${rev.rating}.0 • ${rev.dateLabel}", style = MaterialTheme.typography.labelSmall, color = MutedOlive)
                                    }
                                }
                                Text(rev.comment, style = MaterialTheme.typography.bodyMedium, color = MutedOlive)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatPill(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, iconTint: Color) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xFF31443D)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(15.dp))
            Text(text, style = MaterialTheme.typography.labelMedium, color = WarmIvory)
        }
    }
}
