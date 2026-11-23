package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CompareArrows
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.BookingEntity
import com.example.ui.components.PressablePrimaryButton
import com.example.ui.theme.AmberGold
import com.example.ui.theme.DeepForest
import com.example.ui.theme.MutedOlive
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SoftSandLight
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.WarmCoral
import com.example.ui.theme.WarmIvory
import com.example.ui.viewmodel.HomeEaseUiState
import com.example.ui.viewmodel.HomeEaseViewModel

@Composable
fun MyBookingsScreen(
    state: HomeEaseUiState,
    viewModel: HomeEaseViewModel,
    onRequestQuoteClick: () -> Unit,
    onCompareQuotesClick: (String) -> Unit,
    onViewProProfile: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var reviewTarget by remember { mutableStateOf<BookingEntity?>(null) }

    val activeBookings = state.customerBookings.filter { it.status == "CONFIRMED" || it.status == "IN_PROGRESS" }
    val historyBookings = state.customerBookings.filter { it.status == "COMPLETED" || it.status == "CANCELLED" }
    val openRequests = state.customerRequests.filter { it.status != "BOOKED" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("My Bookings & Quotes", style = MaterialTheme.typography.headlineMedium, color = DeepForest)
                Text("Track active slots, quotes, and home history", style = MaterialTheme.typography.bodySmall, color = MutedOlive)
            }
            Surface(
                onClick = onRequestQuoteClick,
                shape = RoundedCornerShape(12.dp),
                color = DeepForest,
                modifier = Modifier.testTag("bookings_new_quote_cta")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = null, tint = PureWhite, modifier = Modifier.size(16.dp))
                    Text("New Quote", style = MaterialTheme.typography.labelMedium, color = PureWhite)
                }
            }
        }

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = WarmIvory,
            contentColor = DeepForest
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Active (${activeBookings.size})") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Quote Requests (${openRequests.size})") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("History (${historyBookings.size})") }
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    if (activeBookings.isEmpty()) {
                        item {
                            EmptyBookingsCard(
                                title = "No active bookings yet",
                                body = "Compare quotations on your service requests or request a new service quote.",
                                ctaText = "Request a Quote",
                                onCta = onRequestQuoteClick
                            )
                        }
                    } else {
                        items(activeBookings, key = { it.id }) { booking ->
                            BookingItemCard(
                                booking = booking,
                                onViewPro = { onViewProProfile(booking.professionalId) },
                                onCancel = {
                                    viewModel.updateBookingStatus(
                                        booking.id,
                                        "CANCELLED",
                                        "Cancelled by customer."
                                    )
                                },
                                onLeaveReview = null
                            )
                        }
                    }
                }

                1 -> {
                    if (openRequests.isEmpty()) {
                        item {
                            EmptyBookingsCard(
                                title = "No open quote requests",
                                body = "Describe your home need and receive transparent ₹ quotations to compare.",
                                ctaText = "Request a Quote",
                                onCta = onRequestQuoteClick
                            )
                        }
                    } else {
                        items(openRequests, key = { it.id }) { req ->
                            val quoteCount = state.allQuotations.count { it.requestId == req.id }
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = PureWhite),
                                border = BorderStroke(1.dp, SubtleBorder)
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
                                        Surface(shape = RoundedCornerShape(50), color = SageGreenContainer) {
                                            Text(
                                                req.categoryName,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = DeepForest,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                            )
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(50),
                                            color = if (quoteCount > 0) SoftSandLight else WarmIvory
                                        ) {
                                            Text(
                                                text = "$quoteCount quotation(s)",
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                                color = DeepForest,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                    Text(req.problemTitle, style = MaterialTheme.typography.titleMedium, color = DeepForest)
                                    Text(req.problemDescription, style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                                    Text("${req.locality} • ${req.preferredDate}", style = MaterialTheme.typography.labelSmall, color = SageGreen)
                                    PressablePrimaryButton(
                                        text = if (quoteCount > 0) "Compare $quoteCount Quotations" else "View Request Status",
                                        onClick = { onCompareQuotesClick(req.id) },
                                        icon = Icons.Rounded.CompareArrows,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("compare_req_${req.id}")
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    if (historyBookings.isEmpty()) {
                        item {
                            EmptyBookingsCard(
                                title = "No past service history yet",
                                body = "Completed and past bookings will be archived here for easy home maintenance reference.",
                                ctaText = "Explore Services",
                                onCta = onRequestQuoteClick
                            )
                        }
                    } else {
                        items(historyBookings, key = { it.id }) { booking ->
                            BookingItemCard(
                                booking = booking,
                                onViewPro = { onViewProProfile(booking.professionalId) },
                                onCancel = null,
                                onLeaveReview = if (booking.status == "COMPLETED" && booking.customerReviewRating == null) {
                                    { reviewTarget = booking }
                                } else null
                            )
                        }
                    }
                }
            }
        }
    }

    reviewTarget?.let { target ->
        var rating by remember { mutableIntStateOf(5) }
        var comment by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { reviewTarget = null },
            title = { Text("Review ${target.professionalName}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("How was your ${target.categoryName} service?", style = MaterialTheme.typography.bodyMedium)
                    Row {
                        (1..5).forEach { star ->
                            IconButton(onClick = { rating = star }) {
                                Icon(
                                    Icons.Rounded.Star,
                                    contentDescription = "$star stars",
                                    tint = if (star <= rating) AmberGold else SubtleBorder
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Share your experience") },
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.submitBookingReview(target, rating, comment)
                        reviewTarget = null
                    }
                ) { Text("Submit Review") }
            },
            dismissButton = {
                TextButton(onClick = { reviewTarget = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun BookingItemCard(
    booking: BookingEntity,
    onViewPro: () -> Unit,
    onCancel: (() -> Unit)?,
    onLeaveReview: (() -> Unit)?
) {
    val badgeColor = when (booking.status) {
        "CONFIRMED" -> SageGreenContainer
        "IN_PROGRESS" -> SoftSandLight
        "COMPLETED" -> Color(0xFFDCE8DF)
        else -> Color(0xFFF9E5E2)
    }
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = BorderStroke(1.dp, SubtleBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(booking.id, style = MaterialTheme.typography.labelMedium, color = MutedOlive)
                Surface(shape = RoundedCornerShape(50), color = badgeColor) {
                    Text(
                        text = booking.status.replace("_", " "),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (booking.status == "CANCELLED") WarmCoral else DeepForest,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            Text(booking.serviceTitle, style = MaterialTheme.typography.titleMedium, color = DeepForest)
            Text("Professional: ${booking.professionalName} • ${booking.categoryName}", style = MaterialTheme.typography.bodySmall, color = MutedOlive)
            Text("Schedule: ${booking.scheduledDate} • ${booking.locality}", style = MaterialTheme.typography.bodySmall, color = DeepForest)
            Text("Agreed Quote: ₹${booking.agreedPriceInr}", style = MaterialTheme.typography.titleSmall, color = DeepForest)

            Surface(shape = RoundedCornerShape(12.dp), color = WarmIvory) {
                Text(
                    text = "Update: ${booking.proStatusNote}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedOlive,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }

            if (booking.customerReviewRating != null) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Rounded.Star, contentDescription = null, tint = AmberGold, modifier = Modifier.size(16.dp))
                    Text("Your Review (${booking.customerReviewRating}/5): ${booking.customerReviewText.orEmpty()}", style = MaterialTheme.typography.bodySmall, color = DeepForest)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = onViewPro,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Pro", color = DeepForest)
                }
                if (onCancel != null) {
                    OutlinedButton(
                        onClick = onCancel,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color = WarmCoral)
                    }
                }
                if (onLeaveReview != null) {
                    PressablePrimaryButton(
                        text = "Rate Service",
                        onClick = onLeaveReview,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyBookingsCard(
    title: String,
    body: String,
    ctaText: String,
    onCta: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = PureWhite,
        border = BorderStroke(1.dp, SubtleBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Rounded.CalendarMonth, contentDescription = null, tint = SageGreen, modifier = Modifier.size(38.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, color = DeepForest)
            Text(body, style = MaterialTheme.typography.bodySmall, color = MutedOlive)
            PressablePrimaryButton(text = ctaText, onClick = onCta)
        }
    }
}
