package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.RequestQuote
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.CatalogData
import com.example.ui.components.HomeEaseSubTopBar
import com.example.ui.components.PressablePrimaryButton
import com.example.ui.components.ProfessionalSummaryCard
import com.example.ui.components.StaggeredEnter
import com.example.ui.theme.DeepForest
import com.example.ui.theme.MutedOlive
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SoftSandLight
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.WarmIvory
import com.example.ui.viewmodel.HomeEaseUiState

@Composable
fun ExploreServicesScreen(
    state: HomeEaseUiState,
    onSearchQueryChange: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onViewProProfile: (String) -> Unit,
    onToggleShortlist: (String) -> Unit,
    onRequestQuoteClick: (String) -> Unit,
    onViewAllPros: () -> Unit
) {
    val q = state.searchQuery.trim()
    val matchingCategories = CatalogData.categories.filter { cat ->
        q.isBlank() ||
            cat.name.contains(q, ignoreCase = true) ||
            cat.shortTagline.contains(q, ignoreCase = true) ||
            cat.commonTasks.any { it.contains(q, ignoreCase = true) }
    }
    val matchingPros = if (q.isBlank()) emptyList() else {
        state.professionals.filter { pro ->
            pro.name.contains(q, ignoreCase = true) ||
                pro.businessName.contains(q, ignoreCase = true) ||
                pro.skillsCsv.contains(q, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Explore Home Services",
                    style = MaterialTheme.typography.headlineLarge,
                    color = DeepForest
                )
                Text(
                    text = "Serving ${state.selectedLocality} • Compare multiple local quotes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedOlive
                )
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Search painting, plumber, AC, carpenter...", color = MutedOlive) },
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Search", tint = DeepForest) },
                    trailingIcon = {
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(Icons.Rounded.Clear, contentDescription = "Clear search", tint = MutedOlive)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = PureWhite,
                        unfocusedContainerColor = PureWhite,
                        focusedBorderColor = DeepForest,
                        unfocusedBorderColor = SubtleBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("explore_search_input")
                )
            }
        }

        if (matchingPros.isNotEmpty()) {
            item {
                Text(
                    text = "Matching Professionals (${matchingPros.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = DeepForest
                )
            }
            items(matchingPros, key = { it.id }) { pro ->
                ProfessionalSummaryCard(
                    pro = pro,
                    isShortlisted = pro.id in state.shortlistedIds,
                    onToggleShortlist = { onToggleShortlist(pro.id) },
                    onViewProfile = { onViewProProfile(pro.id) }
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Service Categories (${matchingCategories.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = DeepForest
                )
                OutlinedButton(
                    onClick = onViewAllPros,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text("All Professionals", color = DeepForest)
                }
            }
        }

        if (matchingCategories.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = PureWhite,
                    border = BorderStroke(1.dp, SubtleBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("No matching categories", style = MaterialTheme.typography.titleMedium, color = DeepForest)
                        Text(
                            "You can still describe any custom home requirement directly in Request a Quote.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedOlive
                        )
                        PressablePrimaryButton(
                            text = "Request Custom Quote",
                            onClick = { onRequestQuoteClick("painting") }
                        )
                    }
                }
            }
        } else {
            itemsIndexed(matchingCategories, key = { _, c -> c.id }) { index, category ->
                StaggeredEnter(index = index) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCategoryClick(category.id) },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        border = BorderStroke(1.dp, SubtleBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(74.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(SageGreenContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = category.imageRes),
                                    contentDescription = category.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Surface(
                                    shape = CircleShape,
                                    color = WarmIvory.copy(alpha = 0.92f),
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(category.icon, contentDescription = null, tint = DeepForest, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = DeepForest
                                )
                                Text(
                                    text = category.shortTagline,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedOlive
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(shape = RoundedCornerShape(50), color = SoftSandLight) {
                                        Text(
                                            text = "From ₹${category.startingPriceInr}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = DeepForest,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                    Text(
                                        text = category.typicalDuration,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SageGreen
                                    )
                                }
                            }
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = "Explore ${category.name}",
                                tint = DeepForest
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceDetailScreen(
    categoryId: String,
    state: HomeEaseUiState,
    onBack: () -> Unit,
    onRequestQuote: (String) -> Unit,
    onViewProsInCategory: (String) -> Unit,
    onViewProProfile: (String) -> Unit,
    onToggleShortlist: (String) -> Unit
) {
    val category = CatalogData.categoryById(categoryId)
    val categoryPros = state.professionals.filter { it.categoryId == category.id }

    Scaffold(
        containerColor = WarmIvory,
        topBar = {
            HomeEaseSubTopBar(title = category.name, onBack = onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    border = BorderStroke(1.dp, SubtleBorder)
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = category.imageRes),
                            contentDescription = category.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                        )
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SageGreenContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(category.icon, contentDescription = null, tint = DeepForest)
                                }
                                Column {
                                    Text(category.name, style = MaterialTheme.typography.headlineSmall, color = DeepForest)
                                    Text(
                                        "Starts at ₹${category.startingPriceInr} • Typical time: ${category.typicalDuration}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SageGreen
                                    )
                                }
                            }
                            Text(
                                text = category.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MutedOlive
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                PressablePrimaryButton(
                                    text = "Request a Quote",
                                    onClick = { onRequestQuote(category.id) },
                                    icon = Icons.Rounded.RequestQuote,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("service_detail_request_quote")
                                )
                            }
                        }
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
                        Text("What Professionals Typically Include", style = MaterialTheme.typography.titleMedium, color = DeepForest)
                        category.includedHighlights.forEach { item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = SageGreen, modifier = Modifier.size(18.dp))
                                Text(item, style = MaterialTheme.typography.bodyMedium, color = DeepForest)
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text("Common Tasks Requested in Pune", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                        category.commonTasks.forEach { task ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Rounded.Schedule, contentDescription = null, tint = MutedOlive, modifier = Modifier.size(16.dp))
                                Text(task, style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${category.name} Specialists (${categoryPros.size})",
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepForest
                    )
                    OutlinedButton(onClick = { onViewProsInCategory(category.id) }) {
                        Text("Filter list", color = DeepForest)
                    }
                }
            }

            items(categoryPros, key = { it.id }) { pro ->
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
