package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.HomeWork
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.RequestQuote
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.local.BookingEntity
import com.example.data.local.ProfessionalEntity
import com.example.data.local.ServiceRequestEntity
import com.example.data.model.CatalogData
import com.example.ui.components.LocalModeNoticeBanner
import com.example.ui.components.PressablePrimaryButton
import com.example.ui.components.ProfessionalSummaryCard
import com.example.ui.theme.AmberGold
import com.example.ui.theme.DeepForest
import com.example.ui.theme.MutedOlive
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SoftSand
import com.example.ui.theme.SoftSandLight
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.WarmCoral
import com.example.ui.theme.WarmIvory
import com.example.ui.viewmodel.HomeEaseUiState
import com.example.ui.viewmodel.HomeEaseViewModel

@Composable
fun CustomerDashboardScreen(
    state: HomeEaseUiState,
    viewModel: HomeEaseViewModel,
    onExploreServices: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onViewProProfile: (String) -> Unit,
    onOpenBookings: () -> Unit,
    onOpenCompareQuotes: (String) -> Unit,
    onOpenHomeProfile: () -> Unit,
    onOpenRecentlyMoved: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenAuth: () -> Unit,
    onSwitchToProDemo: () -> Unit
) {
    val activeBookings = state.customerBookings.filter { it.status == "CONFIRMED" || it.status == "IN_PROGRESS" }
    val pastBookings = state.customerBookings.filter { it.status == "COMPLETED" }
    val user = state.currentUser

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = DeepForest,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Namaste, ${user?.name ?: "Pune Guest"}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = WarmIvory
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = SoftSand, modifier = Modifier.size(15.dp))
                                Text(
                                    text = "${state.selectedLocality} • Customer Account",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SoftSandLight
                                )
                            }
                        }
                        Surface(
                            onClick = onOpenNotifications,
                            shape = CircleShape,
                            color = ColorPillForest
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Rounded.Notifications, contentDescription = "Notifications", tint = SoftSand, modifier = Modifier.size(16.dp))
                                Text("${state.unreadNotificationCount}", style = MaterialTheme.typography.labelMedium, color = WarmIvory)
                            }
                        }
                    }

                    // Search services bar trigger
                    Surface(
                        onClick = onExploreServices,
                        shape = RoundedCornerShape(14.dp),
                        color = PureWhite,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Rounded.Search, contentDescription = null, tint = MutedOlive)
                            Text("Search Pune home services...", style = MaterialTheme.typography.bodyMedium, color = MutedOlive)
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = onSwitchToProDemo,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, SoftSand.copy(alpha = 0.6f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Rounded.SwapHoriz, contentDescription = null, tint = SoftSand, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Pro View", color = WarmIvory, style = MaterialTheme.typography.labelMedium)
                        }
                        OutlinedButton(
                            onClick = onOpenAuth,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, SoftSand.copy(alpha = 0.6f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (user == null) "Sign In" else "Account", color = WarmIvory, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }

        item { LocalModeNoticeBanner(compact = true) }

        // My Home Profile + Recently Moved shortcuts
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    onClick = onOpenHomeProfile,
                    shape = RoundedCornerShape(18.dp),
                    color = PureWhite,
                    border = BorderStroke(1.dp, SubtleBorder),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("dashboard_home_profile_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Rounded.Home, contentDescription = null, tint = DeepForest)
                        Text("My Home Profile", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                        Text(
                            text = state.homeProfile?.let { "${it.homeType} • ${it.approxSqFt} sq.ft" } ?: "Set up home details",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedOlive
                        )
                    }
                }
                Surface(
                    onClick = onOpenRecentlyMoved,
                    shape = RoundedCornerShape(18.dp),
                    color = SageGreenContainer,
                    border = BorderStroke(1.dp, SageGreen.copy(alpha = 0.35f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Rounded.HomeWork, contentDescription = null, tint = DeepForest)
                        Text("Recently Moved?", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                        Text("5-service move-in bundle", style = MaterialTheme.typography.bodySmall, color = DeepForest)
                    }
                }
            }
        }

        // Popular Services quick chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Popular Services", style = MaterialTheme.typography.titleLarge, color = DeepForest)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(CatalogData.categories, key = { it.id }) { cat ->
                        Surface(
                            onClick = { onCategoryClick(cat.id) },
                            shape = RoundedCornerShape(14.dp),
                            color = PureWhite,
                            border = BorderStroke(1.dp, SubtleBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(cat.icon, contentDescription = null, tint = SageGreen, modifier = Modifier.size(18.dp))
                                Text(cat.name, style = MaterialTheme.typography.labelLarge, color = DeepForest)
                            }
                        }
                    }
                }
            }
        }

        // Active Bookings & Quote Requests
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Active Bookings (${activeBookings.size})", style = MaterialTheme.typography.titleLarge, color = DeepForest)
                TextButton(onClick = onOpenBookings) { Text("Manage all", color = DeepForest) }
            }
        }

        if (activeBookings.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = PureWhite,
                    border = BorderStroke(1.dp, SubtleBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "No active scheduled visits right now.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedOlive,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(activeBookings, key = { it.id }) { b ->
                Surface(
                    onClick = onOpenBookings,
                    shape = RoundedCornerShape(18.dp),
                    color = PureWhite,
                    border = BorderStroke(1.dp, SubtleBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(b.serviceTitle, style = MaterialTheme.typography.titleSmall, color = DeepForest)
                            Text("₹${b.agreedPriceInr}", style = MaterialTheme.typography.titleSmall, color = SageGreen)
                        }
                        Text("${b.professionalName} • ${b.scheduledDate}", style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                    }
                }
            }
        }

        // Open Quote Requests ready to compare
        val quotedReqs = state.customerRequests.filter { it.status != "BOOKED" }
        if (quotedReqs.isNotEmpty()) {
            item {
                Text("Open Quote Requests", style = MaterialTheme.typography.titleLarge, color = DeepForest)
            }
            items(quotedReqs.take(3), key = { it.id }) { req ->
                val count = state.allQuotations.count { it.requestId == req.id }
                Surface(
                    onClick = { onOpenCompareQuotes(req.id) },
                    shape = RoundedCornerShape(16.dp),
                    color = SoftSandLight,
                    border = BorderStroke(1.dp, SoftSand),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(req.problemTitle, style = MaterialTheme.typography.titleSmall, color = DeepForest)
                            Text("${req.categoryName} • $count quotation(s)", style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                        }
                        Text("Compare", style = MaterialTheme.typography.labelLarge, color = DeepForest)
                    }
                }
            }
        }

        // Saved Professionals (Trusted Shortlist)
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Rounded.Favorite, contentDescription = null, tint = WarmCoral, modifier = Modifier.size(18.dp))
                Text("Saved Professionals (${state.shortlistedPros.size})", style = MaterialTheme.typography.titleLarge, color = DeepForest)
            }
        }
        if (state.shortlistedPros.isEmpty()) {
            item {
                Text(
                    "Tap the heart icon on any professional to keep a shortlist of trusted experts.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedOlive
                )
            }
        } else {
            items(state.shortlistedPros, key = { "saved_${it.id}" }) { pro ->
                ProfessionalSummaryCard(
                    pro = pro,
                    isShortlisted = true,
                    onToggleShortlist = { viewModel.toggleShortlist(pro.id) },
                    onViewProfile = { onViewProProfile(pro.id) }
                )
            }
        }

        // Recently Viewed Professionals
        if (state.recentlyViewedPros.isNotEmpty()) {
            item {
                Text("Recently Viewed Professionals", style = MaterialTheme.typography.titleLarge, color = DeepForest)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.recentlyViewedPros, key = { "recent_${it.id}" }) { pro ->
                        Surface(
                            onClick = { onViewProProfile(pro.id) },
                            shape = RoundedCornerShape(16.dp),
                            color = PureWhite,
                            border = BorderStroke(1.dp, SubtleBorder),
                            modifier = Modifier.width(190.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(pro.name, style = MaterialTheme.typography.titleSmall, color = DeepForest, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(CatalogData.categoryById(pro.categoryId).name, style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Rounded.Star, contentDescription = null, tint = AmberGold, modifier = Modifier.size(14.dp))
                                    Text("${pro.rating} • ${pro.experienceYears} yrs", style = MaterialTheme.typography.labelSmall, color = DeepForest)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Home Service History (Previous Bookings)
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Rounded.History, contentDescription = null, tint = SageGreen, modifier = Modifier.size(18.dp))
                Text("Previous Bookings (${pastBookings.size})", style = MaterialTheme.typography.titleLarge, color = DeepForest)
            }
        }
        items(pastBookings, key = { "hist_${it.id}" }) { past ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PureWhite,
                border = BorderStroke(1.dp, SubtleBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(past.serviceTitle, style = MaterialTheme.typography.titleSmall, color = DeepForest)
                        Text("${past.professionalName} • ${past.scheduledDate}", style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                    }
                    Text("₹${past.agreedPriceInr}", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                }
            }
        }
    }
}

private val ColorPillForest = androidx.compose.ui.graphics.Color(0xFF31443D)

@Composable
fun ProfessionalDashboardScreen(
    state: HomeEaseUiState,
    viewModel: HomeEaseViewModel,
    onBecomePro: () -> Unit,
    onSwitchToCustomerDemo: () -> Unit
) {
    val proProfile = state.currentProProfile
    var selectedTab by remember { mutableIntStateOf(0) }
    var quoteTargetRequest by remember { mutableStateOf<ServiceRequestEntity?>(null) }
    var statusUpdateBooking by remember { mutableStateOf<BookingEntity?>(null) }
    var showEditProfileDialog by remember { mutableStateOf(false) }

    if (proProfile == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Professional Workspace", style = MaterialTheme.typography.headlineMedium, color = DeepForest)
            Spacer(Modifier.height(8.dp))
            Text(
                "Sign in as a registered Pune service professional or create a business profile to receive requests, submit quotations, and track earnings.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedOlive
            )
            Spacer(Modifier.height(16.dp))
            PressablePrimaryButton(
                text = "Register as a Professional",
                onClick = onBecomePro,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = { viewModel.switchDemoAccount(isPro = true) },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Try Demo Pro: Rohan Deshmukh", color = DeepForest)
            }
        }
        return
    }

    val proBookings = state.allBookings.filter { it.professionalId == proProfile.id }
    val completedEarnings = proBookings.filter { it.status == "COMPLETED" }.sumOf { it.agreedPriceInr }
    val activePipeline = proBookings.filter { it.status == "CONFIRMED" || it.status == "IN_PROGRESS" }.sumOf { it.agreedPriceInr }
    val incomingRequests = state.allRequests.filter { it.status == "OPEN_FOR_QUOTES" || it.status == "QUOTES_RECEIVED" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        Surface(
            color = DeepForest,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(proProfile.businessName, style = MaterialTheme.typography.titleLarge, color = WarmIvory)
                        Text(
                            "${proProfile.name} • ${CatalogData.categoryById(proProfile.categoryId).name}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SoftSandLight
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            onClick = { showEditProfileDialog = true },
                            shape = RoundedCornerShape(50),
                            color = ColorPillForest
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Rounded.Edit, contentDescription = "Edit Business Profile", tint = SoftSand, modifier = Modifier.size(14.dp))
                                Text("Profile", style = MaterialTheme.typography.labelSmall, color = WarmIvory)
                            }
                        }
                        Surface(
                            onClick = onSwitchToCustomerDemo,
                            shape = RoundedCornerShape(50),
                            color = ColorPillForest
                        ) {
                            Text(
                                "Customer View",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoftSand,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = ColorPillForest,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Rounded.AccountBalanceWallet, contentDescription = null, tint = SoftSand, modifier = Modifier.size(15.dp))
                                Text("Completed Earnings", style = MaterialTheme.typography.labelSmall, color = SoftSandLight)
                            }
                            Text("₹$completedEarnings", style = MaterialTheme.typography.titleLarge, color = WarmIvory)
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = ColorPillForest,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Active Pipeline", style = MaterialTheme.typography.labelSmall, color = SoftSandLight)
                            Text("₹$activePipeline • ${proProfile.rating}★", style = MaterialTheme.typography.titleLarge, color = WarmIvory)
                        }
                    }
                }
            }
        }

        TabRow(selectedTabIndex = selectedTab, containerColor = WarmIvory, contentColor = DeepForest) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Requests (${incomingRequests.size})") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Bookings (${proBookings.size})") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Profile & Reviews") })
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    item { LocalModeNoticeBanner(compact = true) }
                    if (incomingRequests.isEmpty()) {
                        item {
                            Text("No open customer requests right now.", style = MaterialTheme.typography.bodyMedium, color = MutedOlive)
                        }
                    } else {
                        items(incomingRequests, key = { it.id }) { req ->
                            val alreadyQuoted = state.allQuotations.any {
                                it.requestId == req.id && it.professionalId == proProfile.id
                            }
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = PureWhite),
                                border = BorderStroke(1.dp, SubtleBorder)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Surface(shape = RoundedCornerShape(50), color = SageGreenContainer) {
                                            Text(
                                                req.categoryName,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = DeepForest,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                            )
                                        }
                                        Text("${req.locality} • ${req.preferredDate}", style = MaterialTheme.typography.labelSmall, color = MutedOlive)
                                    }
                                    Text(req.problemTitle, style = MaterialTheme.typography.titleMedium, color = DeepForest)
                                    Text("Customer: ${req.customerName}", style = MaterialTheme.typography.labelMedium, color = SageGreen)
                                    Text(req.problemDescription, style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                                    Text("Address: ${req.address}", style = MaterialTheme.typography.bodySmall, color = DeepForest)

                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        PressablePrimaryButton(
                                            text = if (alreadyQuoted) "Quote Submitted" else "Accept & Send Quote",
                                            enabled = !alreadyQuoted,
                                            onClick = { quoteTargetRequest = req },
                                            icon = Icons.Rounded.RequestQuote,
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("pro_quote_req_${req.id}")
                                        )
                                        if (!alreadyQuoted) {
                                            OutlinedButton(
                                                onClick = { viewModel.declineRequest(req.id) },
                                                shape = RoundedCornerShape(14.dp)
                                            ) {
                                                Text("Decline", color = WarmCoral)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    if (proBookings.isEmpty()) {
                        item {
                            Text("No bookings assigned yet. Submit quotations on incoming requests!", style = MaterialTheme.typography.bodyMedium, color = MutedOlive)
                        }
                    } else {
                        items(proBookings, key = { it.id }) { b ->
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = PureWhite),
                                border = BorderStroke(1.dp, SubtleBorder)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("${b.id} • ${b.status}", style = MaterialTheme.typography.labelMedium, color = SageGreen)
                                        Text("₹${b.agreedPriceInr}", style = MaterialTheme.typography.titleMedium, color = DeepForest)
                                    }
                                    Text(b.serviceTitle, style = MaterialTheme.typography.titleMedium, color = DeepForest)
                                    Text("Customer: ${b.customerName} • ${b.locality}", style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                                    Text("Slot: ${b.scheduledDate}", style = MaterialTheme.typography.bodySmall, color = DeepForest)
                                    Text("Last Update: ${b.proStatusNote}", style = MaterialTheme.typography.bodySmall, color = MutedOlive)

                                    if (b.status != "COMPLETED" && b.status != "CANCELLED") {
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            if (b.status == "CONFIRMED") {
                                                PressablePrimaryButton(
                                                    text = "Start Job",
                                                    onClick = {
                                                        viewModel.updateBookingStatus(b.id, "IN_PROGRESS", "Professional arrived and started work.")
                                                    },
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                            PressablePrimaryButton(
                                                text = "Mark Completed",
                                                onClick = {
                                                    viewModel.updateBookingStatus(b.id, "COMPLETED", "Work completed and reviewed with homeowner.")
                                                },
                                                containerColor = SageGreen,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                        OutlinedButton(
                                            onClick = { statusUpdateBooking = b },
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Send Status Note to Customer", color = DeepForest)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    item {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = PureWhite,
                            border = BorderStroke(1.dp, SubtleBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Business & Service Details", style = MaterialTheme.typography.titleMedium, color = DeepForest)
                                Text("Service Areas: ${proProfile.serviceAreasCsv}", style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                                Text("Experience: ${proProfile.experienceYears} years • ${proProfile.completedJobs} completed jobs", style = MaterialTheme.typography.bodySmall, color = DeepForest)
                                Text("Bio: ${proProfile.bio}", style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                                Text("Portfolio: ${proProfile.portfolioHighlightsCsv.replace("|", " • ")}", style = MaterialTheme.typography.bodySmall, color = DeepForest)
                                OutlinedButton(onClick = { showEditProfileDialog = true }, shape = RoundedCornerShape(12.dp)) {
                                    Text("Edit Business Profile", color = DeepForest)
                                }
                            }
                        }
                    }
                    val reviews = proBookings.filter { it.customerReviewRating != null }
                    item {
                        Text("Recent Job Reviews (${reviews.size})", style = MaterialTheme.typography.titleMedium, color = DeepForest)
                    }
                    items(reviews, key = { "pro_rev_${it.id}" }) { b ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = PureWhite,
                            border = BorderStroke(1.dp, SubtleBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("${b.customerName} • ${b.customerReviewRating}/5 ★", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                                Text(b.customerReviewText.orEmpty(), style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                            }
                        }
                    }
                }
            }
        }
    }

    // Submit Quotation Dialog
    quoteTargetRequest?.let { req ->
        var priceText by remember { mutableStateOf(proProfile.startingPriceInr.toString()) }
        var duration by remember { mutableStateOf("1 day") }
        var included by remember { mutableStateOf("Site inspection, Skilled labor, Post-work cleanup") }
        var extraNote by remember { mutableStateOf("No hidden charges; material invoice shared separately if needed.") }
        AlertDialog(
            onDismissRequest = { quoteTargetRequest = null },
            title = { Text("Send Quotation to ${req.customerName}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(req.problemTitle, style = MaterialTheme.typography.titleSmall, color = DeepForest)
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Price Quotation (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text("Estimated Completion Time") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = included,
                        onValueChange = { included = it },
                        label = { Text("Included Services (comma-separated)") },
                        minLines = 2
                    )
                    OutlinedTextField(
                        value = extraNote,
                        onValueChange = { extraNote = it },
                        label = { Text("Additional Charges Note") },
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.submitQuotation(
                            requestId = req.id,
                            priceInr = priceText.toIntOrNull() ?: 0,
                            estimatedDuration = duration,
                            includedServices = included,
                            additionalChargesNote = extraNote
                        ) {
                            quoteTargetRequest = null
                        }
                    }
                ) { Text("Send Quote") }
            },
            dismissButton = {
                TextButton(onClick = { quoteTargetRequest = null }) { Text("Cancel") }
            }
        )
    }

    // Booking Update Note Dialog
    statusUpdateBooking?.let { booking ->
        var note by remember { mutableStateOf(booking.proStatusNote) }
        AlertDialog(
            onDismissRequest = { statusUpdateBooking = null },
            title = { Text("Update Booking ${booking.id}") },
            text = {
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Message to customer") },
                    minLines = 3
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updateBookingStatus(booking.id, booking.status, note)
                        statusUpdateBooking = null
                    }
                ) { Text("Send Update") }
            },
            dismissButton = {
                TextButton(onClick = { statusUpdateBooking = null }) { Text("Cancel") }
            }
        )
    }

    // Edit Business Profile Dialog
    if (showEditProfileDialog) {
        EditProProfileDialog(
            pro = proProfile,
            onDismiss = { showEditProfileDialog = false },
            onSave = { updated ->
                viewModel.updateProfessionalProfile(updated)
                showEditProfileDialog = false
            }
        )
    }
}

@Composable
private fun EditProProfileDialog(
    pro: ProfessionalEntity,
    onDismiss: () -> Unit,
    onSave: (ProfessionalEntity) -> Unit
) {
    var businessName by remember { mutableStateOf(pro.businessName) }
    var serviceAreas by remember { mutableStateOf(pro.serviceAreasCsv) }
    var startingPrice by remember { mutableStateOf(pro.startingPriceInr.toString()) }
    var bio by remember { mutableStateOf(pro.bio) }
    var portfolio by remember { mutableStateOf(pro.portfolioHighlightsCsv) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Business Profile") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = businessName, onValueChange = { businessName = it }, label = { Text("Business Name") })
                OutlinedTextField(value = serviceAreas, onValueChange = { serviceAreas = it }, label = { Text("Pune Areas (comma-separated)") })
                OutlinedTextField(
                    value = startingPrice,
                    onValueChange = { startingPrice = it.filter { c -> c.isDigit() } },
                    label = { Text("Starting Price (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Bio") }, minLines = 2)
                OutlinedTextField(value = portfolio, onValueChange = { portfolio = it }, label = { Text("Portfolio Highlights (| separated)") }, minLines = 2)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        pro.copy(
                            businessName = businessName.ifBlank { pro.businessName },
                            serviceAreasCsv = serviceAreas.ifBlank { pro.serviceAreasCsv },
                            startingPriceInr = startingPrice.toIntOrNull() ?: pro.startingPriceInr,
                            bio = bio.ifBlank { pro.bio },
                            portfolioHighlightsCsv = portfolio
                        )
                    )
                }
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
