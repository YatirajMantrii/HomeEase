package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CompareArrows
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Handshake
import androidx.compose.material.icons.rounded.HomeWork
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.RateReview
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material.icons.rounded.WorkOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.CatalogData
import com.example.data.model.ServiceCategory
import com.example.ui.components.LocalModeNoticeBanner
import com.example.ui.components.PressablePrimaryButton
import com.example.ui.components.ProfessionalSummaryCard
import com.example.ui.components.StaggeredEnter
import com.example.ui.theme.DeepForest
import com.example.ui.theme.MutedOlive
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SoftSand
import com.example.ui.theme.SoftSandLight
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.WarmIvory
import com.example.ui.viewmodel.HomeEaseUiState

@Composable
fun HomeScreen(
    state: HomeEaseUiState,
    onSearchQueryChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    onLocalityClick: () -> Unit,
    onExploreServices: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onViewProProfile: (String) -> Unit,
    onToggleShortlist: (String) -> Unit,
    onRecentlyMovedClick: () -> Unit,
    onBecomeProClick: () -> Unit,
    onLoginClick: () -> Unit,
    onRequestQuoteClick: () -> Unit,
    onAboutClick: () -> Unit,
    onContactClick: () -> Unit,
    onViewAllPros: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_float")
    val orbOffset by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb_float"
    )

    val filteredPros = state.professionals.filter { pro ->
        state.selectedLocality.startsWith("Pune") ||
            pro.serviceAreasCsv.contains(state.selectedLocality, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Hero Section
        item {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                val isWide = maxWidth >= 700.dp
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = DeepForest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 1100.dp)
                        .align(Alignment.Center)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // Subtle animated organic accent circles
                        Box(
                            modifier = Modifier
                                .size(190.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 45.dp, y = (-30 + orbOffset).dp)
                                .clip(CircleShape)
                                .background(SageGreen.copy(alpha = 0.22f))
                        )
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .align(Alignment.BottomStart)
                                .offset(x = (-35).dp, y = (30 - orbOffset).dp)
                                .clip(CircleShape)
                                .background(SoftSand.copy(alpha = 0.14f))
                        )

                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Top navigation pill bar inside hero for quick desktop/mobile discovery
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    onClick = onLocalityClick,
                                    shape = RoundedCornerShape(50),
                                    color = Color(0xFF31443D),
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            Icons.Rounded.LocationOn,
                                            contentDescription = "Pune location",
                                            tint = SoftSand,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = state.selectedLocality,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = WarmIvory
                                        )
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    TextButton(onClick = onBecomeProClick) {
                                        Text(
                                            text = "Become a Pro",
                                            color = SoftSand,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                    if (state.currentUser == null) {
                                        TextButton(onClick = onLoginClick) {
                                            Text(
                                                text = "Sign In",
                                                color = WarmIvory,
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                        }
                                    }
                                }
                            }

                            if (isWide) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1.1f),
                                        verticalArrangement = Arrangement.spacedBy(14.dp)
                                    ) {
                                        HeroTextAndSearch(
                                            query = state.searchQuery,
                                            onQueryChange = onSearchQueryChange,
                                            onSearchSubmit = onSearchSubmit,
                                            onExploreServices = onExploreServices,
                                            onRequestQuoteClick = onRequestQuoteClick
                                        )
                                    }
                                    HeroLifestyleImage(
                                        modifier = Modifier
                                            .weight(0.9f)
                                            .height(270.dp)
                                    )
                                }
                            } else {
                                HeroTextAndSearch(
                                    query = state.searchQuery,
                                    onQueryChange = onSearchQueryChange,
                                    onSearchSubmit = onSearchSubmit,
                                    onExploreServices = onExploreServices,
                                    onRequestQuoteClick = onRequestQuoteClick
                                )
                                HeroLifestyleImage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(190.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Local mode transparency banner
        item {
            LocalModeNoticeBanner(
                modifier = Modifier.padding(horizontal = 16.dp),
                compact = true
            )
        }

        // 2. Popular Categories (All 9)
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Popular Categories in Pune",
                            style = MaterialTheme.typography.headlineMedium,
                            color = DeepForest
                        )
                        Text(
                            text = "Transparent scope & local quotations across 9 essentials",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedOlive
                        )
                    }
                    TextButton(onClick = onExploreServices) {
                        Text("See all", color = DeepForest)
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(CatalogData.categories, key = { it.id }) { category ->
                        CategoryShowcaseCard(
                            category = category,
                            onExplore = { onCategoryClick(category.id) }
                        )
                    }
                }
            }
        }

        // 3. Recently Moved? Relocation Spotlight
        item {
            StaggeredEnter(index = 1) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable(onClick = onRecentlyMovedClick)
                        .testTag("recently_moved_banner_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    border = BorderStroke(1.dp, SubtleBorder)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(155.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_recently_moved_banner),
                                contentDescription = "Recently moved home entryway",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, DeepForest.copy(alpha = 0.72f))
                                        )
                                    )
                            )
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = SoftSand,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = "NEW TO PUNE OR SHIFTED FLATS?",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = DeepForest,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                            Text(
                                text = "Recently Moved? Settle in Effortlessly.",
                                style = MaterialTheme.typography.headlineSmall,
                                color = PureWhite,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(14.dp)
                            )
                        }
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Combine pre-move deep cleaning, painting, plumbing valve checks, fan/inverter setup, and curtain carpentry in one coordinated request.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MutedOlive
                            )
                            PressablePrimaryButton(
                                text = "Explore Move-In Bundle",
                                onClick = onRecentlyMovedClick,
                                icon = Icons.Rounded.HomeWork,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        // 4. How HomeEase Works (3 Steps)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "How HomeEase Works",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DeepForest
                )
                val steps = listOf(
                    Triple("1. Choose your service", "Pick from 9 home essentials, share a brief description, and attach photos.", Icons.Rounded.TaskAlt),
                    Triple("2. Compare professionals & quotes", "Review transparent ₹ quotations, completion time, included scope, and reviews side by side.", Icons.Rounded.CompareArrows),
                    Triple("3. Book the pro you trust", "Confirm your preferred Pune slot and track progress from booking to completion.", Icons.Rounded.Handshake)
                )
                steps.forEachIndexed { idx, (title, body, icon) ->
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = PureWhite,
                        border = BorderStroke(1.dp, SubtleBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (idx == 1) SoftSandLight else SageGreenContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(icon, contentDescription = null, tint = DeepForest)
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(title, style = MaterialTheme.typography.titleMedium, color = DeepForest)
                                Text(body, style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                            }
                        }
                    }
                }
            }
        }

        // 5. Why HomeEase? (6 Highlights)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Why HomeEase?",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DeepForest
                )
                val whyItems = listOf(
                    Triple("Multiple Quotations", "Compare itemized estimates before committing.", Icons.Rounded.CompareArrows),
                    Triple("Reviewed Local Profiles", "View real experience, areas served, and portfolio work.", Icons.Rounded.Shield),
                    Triple("Transparent Pricing", "Clear included scope and any extra material notes.", Icons.Rounded.Payments),
                    Triple("Easy Booking", "Pick dates and upload room photos right from your phone.", Icons.Rounded.CalendarMonth),
                    Triple("Customer Reviews", "Read honest feedback from Pune households.", Icons.Rounded.RateReview),
                    Triple("Calm Management", "Track quotes, home profile, and service history in one place.", Icons.Rounded.Dashboard)
                )
                whyItems.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        pair.forEach { (title, desc, icon) ->
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(18.dp),
                                color = PureWhite,
                                border = BorderStroke(1.dp, SubtleBorder)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(icon, contentDescription = null, tint = SageGreen, modifier = Modifier.size(22.dp))
                                    Text(title, style = MaterialTheme.typography.titleSmall, color = DeepForest)
                                    Text(desc, style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 6. Featured Professionals
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Featured Local Professionals",
                        style = MaterialTheme.typography.headlineMedium,
                        color = DeepForest
                    )
                    Text(
                        text = "Serving ${state.selectedLocality}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedOlive
                    )
                }
                TextButton(onClick = onViewAllPros) {
                    Text("View all", color = DeepForest)
                }
            }
        }

        items(filteredPros.take(4), key = { it.id }) { pro ->
            ProfessionalSummaryCard(
                pro = pro,
                isShortlisted = pro.id in state.shortlistedIds,
                onToggleShortlist = { onToggleShortlist(pro.id) },
                onViewProfile = { onViewProProfile(pro.id) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // 7. Become a Pro & Footer Links
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(22.dp),
                color = SageGreenContainer,
                border = BorderStroke(1.dp, SageGreen.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Rounded.WorkOutline, contentDescription = null, tint = DeepForest)
                        Text(
                            text = "Are you a skilled home professional in Pune?",
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepForest
                        )
                    }
                    Text(
                        text = "Receive local quotation requests, showcase your portfolio, and manage your bookings in one calm workspace.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeepForest
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        PressablePrimaryButton(
                            text = "Become a Professional",
                            onClick = onBecomeProClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(onClick = onAboutClick) {
                            Text("About Us", color = DeepForest)
                        }
                        TextButton(onClick = onContactClick) {
                            Text("Contact Pune Support", color = DeepForest)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroTextAndSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    onExploreServices: () -> Unit,
    onRequestQuoteClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Your Home Deserves Better Care.",
            style = MaterialTheme.typography.displayMedium,
            color = WarmIvory
        )
        Text(
            text = "Find trusted local professionals, compare quotations and book home services without the hassle.",
            style = MaterialTheme.typography.bodyMedium,
            color = SoftSandLight
        )

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    "What does your home need today?",
                    color = MutedOlive
                )
            },
            leadingIcon = {
                Icon(Icons.Rounded.Search, contentDescription = "Search", tint = DeepForest)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearchSubmit() }),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = PureWhite,
                unfocusedContainerColor = PureWhite,
                focusedBorderColor = SoftSand,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = DeepForest,
                unfocusedTextColor = DeepForest
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("hero_search_input")
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PressablePrimaryButton(
                text = "Explore Services",
                onClick = onExploreServices,
                icon = Icons.AutoMirrored.Rounded.ArrowForward,
                containerColor = SoftSand,
                contentColor = DeepForest,
                modifier = Modifier
                    .weight(1f)
                    .testTag("hero_explore_cta")
            )
            OutlinedButton(
                onClick = onRequestQuoteClick,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, SoftSand.copy(alpha = 0.65f)),
                modifier = Modifier
                    .weight(1f)
                    .minimumInteractiveComponentSize()
            ) {
                Text(
                    text = "Request Quote",
                    color = WarmIvory,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun HeroLifestyleImage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_hero_home_banner),
            contentDescription = "Sunlit modern Pune home living room",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Surface(
            shape = RoundedCornerShape(50),
            color = DeepForest.copy(alpha = 0.85f),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = "Trusted Pune Craftsmanship",
                style = MaterialTheme.typography.labelMedium,
                color = WarmIvory,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun CategoryShowcaseCard(
    category: ServiceCategory,
    onExplore: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(230.dp)
            .clickable(onClick = onExplore)
            .testTag("category_card_${category.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SubtleBorder)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
            ) {
                Image(
                    painter = painterResource(id = category.imageRes),
                    contentDescription = category.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, DeepForest.copy(alpha = 0.5f))
                            )
                        )
                )
                Surface(
                    shape = CircleShape,
                    color = WarmIvory,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = null,
                            tint = DeepForest,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(50),
                    color = SoftSandLight,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "From ₹${category.startingPriceInr}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = DeepForest,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepForest,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = category.shortTagline,
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedOlive,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.height(36.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Explore",
                        style = MaterialTheme.typography.labelLarge,
                        color = SageGreen
                    )
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        tint = SageGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
