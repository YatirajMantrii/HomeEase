package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CompareArrows
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.WorkOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.local.QuotationEntity
import com.example.data.model.CatalogData
import com.example.ui.components.HomeEaseSubTopBar
import com.example.ui.components.PressablePrimaryButton
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
import com.example.ui.theme.WarmCoralContainer
import com.example.ui.theme.WarmIvory
import com.example.ui.viewmodel.HomeEaseUiState
import com.example.ui.viewmodel.HomeEaseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RequestQuoteScreen(
    initialCategoryId: String,
    initialBundle: Boolean,
    preferredProId: String,
    state: HomeEaseUiState,
    viewModel: HomeEaseViewModel,
    onBack: () -> Unit,
    onRequestSubmitted: (requestId: String) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    var selectedCategoryId by remember { mutableStateOf(initialCategoryId.ifBlank { "painting" }) }
    var isRelocationBundle by remember { mutableStateOf(initialBundle) }
    var problemTitle by remember {
        mutableStateOf(
            if (initialBundle) "Move-In Home Readiness Bundle" else ""
        )
    }
    var problemDescription by remember {
        mutableStateOf(
            if (initialBundle) "Recently moved in Pune. Looking for coordinated cleaning, wall touch-up, plumbing valve check, electrical fan/inverter mounting, and curtain setup." else ""
        )
    }
    val selectedPhotos = remember { mutableStateListOf<Uri>() }
    val defaultArea = if (state.selectedLocality.startsWith("Pune")) {
        state.homeProfile?.locality ?: "Kothrud"
    } else state.selectedLocality
    var locality by remember { mutableStateOf(defaultArea) }
    var address by remember {
        mutableStateOf(
            if (state.homeProfile != null) "${state.homeProfile.homeType}, ${state.homeProfile.locality}" else ""
        )
    }
    var preferredDate by remember { mutableStateOf("28 Nov 2026 • Morning") }
    var validationError by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null && !selectedPhotos.contains(uri)) {
                selectedPhotos.add(uri)
            }
        }
    )

    val stepTitles = listOf(
        "1. Select Service Category",
        "2. Describe the Problem",
        "3. Reference Photos (Optional)",
        "4. Pune Address & Preferred Date",
        "5. Review & Submit Request"
    )

    Scaffold(
        containerColor = WarmIvory,
        topBar = {
            HomeEaseSubTopBar(
                title = "Request a Quote",
                onBack = {
                    if (step > 1) step-- else onBack()
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stepTitles[step - 1], style = MaterialTheme.typography.titleMedium, color = DeepForest)
                    Text("Step $step of 5", style = MaterialTheme.typography.labelMedium, color = SageGreen)
                }
                LinearProgressIndicator(
                    progress = { step / 5f },
                    color = DeepForest,
                    trackColor = SoftSandLight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(50))
                )
            }

            if (validationError != null) {
                Surface(
                    color = WarmCoralContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = validationError!!,
                        color = WarmCoral,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            AnimatedContent(
                targetState = step,
                modifier = Modifier.weight(1f),
                label = "quote_wizard_step"
            ) { currentStep ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when (currentStep) {
                        1 -> {
                            item {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = SoftSandLight,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("Recently Moved Bundle?", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                                            Text("Tag this request as a move-in home readiness package", style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                                        }
                                        Switch(
                                            checked = isRelocationBundle,
                                            onCheckedChange = { isRelocationBundle = it }
                                        )
                                    }
                                }
                            }
                            items(CatalogData.categories, key = { it.id }) { cat ->
                                val active = cat.id == selectedCategoryId
                                Surface(
                                    onClick = { selectedCategoryId = cat.id },
                                    shape = RoundedCornerShape(18.dp),
                                    color = if (active) DeepForest else PureWhite,
                                    border = BorderStroke(1.dp, if (active) DeepForest else SubtleBorder),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("wizard_cat_${cat.id}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            cat.icon,
                                            contentDescription = null,
                                            tint = if (active) SoftSand else DeepForest
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                cat.name,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = if (active) PureWhite else DeepForest
                                            )
                                            Text(
                                                cat.shortTagline,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (active) SoftSandLight else MutedOlive
                                            )
                                        }
                                        if (active) {
                                            Icon(Icons.Rounded.CheckCircle, contentDescription = "Selected", tint = SoftSand)
                                        }
                                    }
                                }
                            }
                        }

                        2 -> {
                            val cat = CatalogData.categoryById(selectedCategoryId)
                            item {
                                Text("Quick Task Suggestions", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                                Spacer(Modifier.height(6.dp))
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    cat.commonTasks.forEach { task ->
                                        FilterChip(
                                            selected = problemTitle == task,
                                            onClick = { problemTitle = task },
                                            label = { Text(task) }
                                        )
                                    }
                                }
                            }
                            item {
                                OutlinedTextField(
                                    value = problemTitle,
                                    onValueChange = { problemTitle = it },
                                    label = { Text("Short Title (e.g., 2 BHK Living Room Repaint)") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("quote_title_input")
                                )
                            }
                            item {
                                OutlinedTextField(
                                    value = problemDescription,
                                    onValueChange = { problemDescription = it },
                                    label = { Text("Describe the work, room size, or issue details") },
                                    minLines = 4,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("quote_desc_input")
                                )
                            }
                        }

                        3 -> {
                            item {
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = PureWhite,
                                    border = BorderStroke(1.dp, SubtleBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            Icons.Rounded.AddPhotoAlternate,
                                            contentDescription = null,
                                            tint = SageGreen,
                                            modifier = Modifier.size(44.dp)
                                        )
                                        Text(
                                            text = "Attach Reference Photographs",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = DeepForest
                                        )
                                        Text(
                                            text = "Adding photos of the wall, tap, AC unit, or furniture helps Pune pros send more accurate quotations.",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MutedOlive
                                        )
                                        PressablePrimaryButton(
                                            text = "Choose Photo from Gallery",
                                            onClick = {
                                                photoPickerLauncher.launch(
                                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                )
                                            },
                                            icon = Icons.Rounded.AddPhotoAlternate,
                                            modifier = Modifier.testTag("pick_reference_photo_button")
                                        )
                                    }
                                }
                            }
                            if (selectedPhotos.isNotEmpty()) {
                                item {
                                    Text("Selected Photos (${selectedPhotos.size})", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                                    Spacer(Modifier.height(8.dp))
                                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        items(selectedPhotos) { uri ->
                                            Box(
                                                modifier = Modifier
                                                    .size(110.dp)
                                                    .clip(RoundedCornerShape(16.dp))
                                            ) {
                                                AsyncImage(
                                                    model = uri,
                                                    contentDescription = "Reference photo",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                                IconButton(
                                                    onClick = { selectedPhotos.remove(uri) },
                                                    modifier = Modifier
                                                        .align(Alignment.TopEnd)
                                                        .padding(4.dp)
                                                        .size(26.dp)
                                                        .clip(CircleShape)
                                                        .background(DeepForest.copy(alpha = 0.8f))
                                                ) {
                                                    Icon(Icons.Rounded.Close, contentDescription = "Remove photo", tint = PureWhite, modifier = Modifier.size(14.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        4 -> {
                            item {
                                Text("Pune Locality", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                                Spacer(Modifier.height(6.dp))
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CatalogData.puneLocalities.drop(1).forEach { area ->
                                        FilterChip(
                                            selected = locality == area,
                                            onClick = { locality = area },
                                            label = { Text(area) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = DeepForest,
                                                selectedLabelColor = PureWhite
                                            )
                                        )
                                    }
                                }
                            }
                            item {
                                OutlinedTextField(
                                    value = address,
                                    onValueChange = { address = it },
                                    label = { Text("Flat / Society / Street Address in $locality") },
                                    leadingIcon = { Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = SageGreen) },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("quote_address_input")
                                )
                            }
                            item {
                                Text("Preferred Service Date", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                                Spacer(Modifier.height(6.dp))
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(
                                        "Tomorrow • Morning",
                                        "28 Nov 2026 • Morning",
                                        "30 Nov 2026 • Afternoon",
                                        "This Weekend"
                                    ).forEach { slot ->
                                        FilterChip(
                                            selected = preferredDate == slot,
                                            onClick = { preferredDate = slot },
                                            label = { Text(slot) }
                                        )
                                    }
                                }
                                Spacer(Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = { showDatePicker = true },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Rounded.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Pick Specific Calendar Date ($preferredDate)")
                                }
                            }
                        }

                        5 -> {
                            val cat = CatalogData.categoryById(selectedCategoryId)
                            item {
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = PureWhite,
                                    border = BorderStroke(1.dp, SubtleBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(18.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text("Request Summary", style = MaterialTheme.typography.headlineSmall, color = DeepForest)
                                        HorizontalDivider(color = SubtleBorder)
                                        SummaryRow("Service Category", if (isRelocationBundle) "${cat.name} (Move-In Bundle)" else cat.name)
                                        SummaryRow("Title", problemTitle)
                                        SummaryRow("Description", problemDescription)
                                        SummaryRow("Pune Area", locality)
                                        SummaryRow("Address", address)
                                        SummaryRow("Preferred Date", preferredDate)
                                        SummaryRow("Photos Attached", "${selectedPhotos.size} image(s)")
                                    }
                                }
                            }
                            item {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = SageGreenContainer,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(Icons.Rounded.Info, contentDescription = null, tint = DeepForest)
                                        Text(
                                            text = "After submitting, your request appears in Incoming Requests in the Professional Dashboard so professionals can send transparent quotations.",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = DeepForest
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Surface(
                color = PureWhite,
                shadowElevation = 6.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (step > 1) {
                        OutlinedButton(
                            onClick = {
                                validationError = null
                                step--
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(0.4f)
                        ) {
                            Text("Back", color = DeepForest)
                        }
                    }
                    PressablePrimaryButton(
                        text = if (step < 5) "Continue" else "Submit Service Request",
                        onClick = {
                            validationError = null
                            when (step) {
                                2 -> {
                                    if (problemTitle.isBlank() || problemDescription.isBlank()) {
                                        validationError = "Please enter a short title and description of the work needed."
                                    } else step++
                                }
                                4 -> {
                                    if (address.isBlank() || preferredDate.isBlank()) {
                                        validationError = "Please enter your Pune address and preferred date."
                                    } else step++
                                }
                                5 -> {
                                    viewModel.submitServiceRequest(
                                        categoryId = selectedCategoryId,
                                        problemTitle = problemTitle,
                                        problemDescription = problemDescription,
                                        photoUris = selectedPhotos.map { it.toString() },
                                        address = address,
                                        locality = locality,
                                        preferredDate = preferredDate,
                                        isRelocationBundle = isRelocationBundle,
                                        preferredProId = preferredProId.ifBlank { null }
                                    ) { req ->
                                        onRequestSubmitted(req.id)
                                    }
                                }
                                else -> step++
                            }
                        },
                        modifier = Modifier
                            .weight(if (step > 1) 0.6f else 1f)
                            .testTag("wizard_next_submit_button")
                    )
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val fmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                            preferredDate = "${fmt.format(Date(millis))} • Morning"
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MutedOlive)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = DeepForest)
    }
}

@Composable
fun CompareQuotationsScreen(
    requestId: String,
    viewModel: HomeEaseViewModel,
    onBack: () -> Unit,
    onViewProProfile: (String) -> Unit,
    onSwitchToProDashboard: () -> Unit,
    onBookingConfirmed: (bookingId: String) -> Unit
) {
    val request by viewModel.observeRequest(requestId).collectAsStateWithLifecycle(initialValue = null)
    val quotations by viewModel.observeQuotationsForRequest(requestId).collectAsStateWithLifecycle(initialValue = emptyList())

    val lowestPrice = quotations.minOfOrNull { it.priceInr }
    val highestRated = quotations.maxOfOrNull { it.professionalRating }

    Scaffold(
        containerColor = WarmIvory,
        topBar = {
            HomeEaseSubTopBar(title = "Compare Quotations", onBack = onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            request?.let { req ->
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = DeepForest,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(req.categoryName, style = MaterialTheme.typography.labelMedium, color = SoftSand)
                            Text(req.problemTitle, style = MaterialTheme.typography.headlineSmall, color = WarmIvory)
                            Text(
                                "${req.locality} • ${req.preferredDate} • Status: ${req.status.replace("_", " ")}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SoftSandLight
                            )
                        }
                    }
                }
            }

            if (quotations.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = PureWhite,
                        border = BorderStroke(1.dp, SubtleBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(22.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Rounded.CompareArrows, contentDescription = null, tint = SageGreen, modifier = Modifier.size(40.dp))
                            Text("Awaiting Quotations", style = MaterialTheme.typography.titleLarge, color = DeepForest)
                            Text(
                                text = "Your request is live locally. To test submitting a professional quotation right away, switch to the Professional Dashboard and quote this request!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MutedOlive
                            )
                            PressablePrimaryButton(
                                text = "Open Professional Dashboard",
                                onClick = onSwitchToProDashboard,
                                icon = Icons.Rounded.WorkOutline
                            )
                        }
                    }
                }
            } else {
                item {
                    Text(
                        text = "Side-by-Side Comparison (${quotations.size} quotes)",
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepForest
                    )
                    Text(
                        text = "Swipe horizontally to compare prices, timelines, included work, and additional charges.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedOlive
                    )
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(quotations, key = { it.id }) { quote ->
                            QuotationComparisonCard(
                                quote = quote,
                                isBestValue = quote.priceInr == lowestPrice,
                                isTopRated = quote.professionalRating == highestRated,
                                onViewPro = { onViewProProfile(quote.professionalId) },
                                onConfirmBooking = {
                                    viewModel.confirmBooking(quote.id) { booking ->
                                        onBookingConfirmed(booking.id)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuotationComparisonCard(
    quote: QuotationEntity,
    isBestValue: Boolean,
    isTopRated: Boolean,
    onViewPro: () -> Unit,
    onConfirmBooking: () -> Unit
) {
    Card(
        modifier = Modifier.width(300.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = BorderStroke(if (isBestValue) 2.dp else 1.dp, if (isBestValue) DeepForest else SubtleBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (isBestValue) {
                    Surface(shape = RoundedCornerShape(50), color = SageGreenContainer) {
                        Text(
                            "Lowest Quote",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = DeepForest,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
                if (isTopRated) {
                    Surface(shape = RoundedCornerShape(50), color = SoftSandLight) {
                        Text(
                            "Top Rated",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = DeepForest,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.clickable(onClick = onViewPro),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(quote.professionalName, style = MaterialTheme.typography.titleLarge, color = DeepForest)
                Text(quote.professionalBusinessName, style = MaterialTheme.typography.bodySmall, color = MutedOlive)
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = WarmIvory,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Quotation", style = MaterialTheme.typography.labelSmall, color = MutedOlive)
                        Text("₹${quote.priceInr}", style = MaterialTheme.typography.headlineMedium, color = DeepForest)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Est. Completion", style = MaterialTheme.typography.labelSmall, color = MutedOlive)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Rounded.Schedule, contentDescription = null, tint = SageGreen, modifier = Modifier.size(14.dp))
                            Text(quote.estimatedDuration, style = MaterialTheme.typography.labelLarge, color = DeepForest)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Rounded.Star, contentDescription = null, tint = AmberGold, modifier = Modifier.size(16.dp))
                    Text("${quote.professionalRating} (${quote.professionalReviewCount} reviews)", style = MaterialTheme.typography.labelMedium, color = DeepForest)
                }
                Text("${quote.professionalExperienceYears} yrs exp", style = MaterialTheme.typography.labelMedium, color = MutedOlive)
            }

            HorizontalDivider(color = SubtleBorder)

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Included Services", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                quote.includedServicesCsv.split("|").filter { it.isNotBlank() }.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = SageGreen, modifier = Modifier.size(15.dp))
                        Text(item, style = MaterialTheme.typography.bodySmall, color = DeepForest)
                    }
                }
            }

            Surface(shape = RoundedCornerShape(12.dp), color = SoftSandLight.copy(alpha = 0.6f)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Additional Charges Note", style = MaterialTheme.typography.labelSmall, color = DeepForest)
                    Text(quote.additionalChargesNote, style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                }
            }

            val isAccepted = quote.status == "ACCEPTED"
            PressablePrimaryButton(
                text = if (isAccepted) "Booked" else "Select & Confirm Booking",
                enabled = quote.status == "PENDING",
                onClick = onConfirmBooking,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("confirm_quote_${quote.id}")
            )
        }
    }
}

@Composable
fun BookingConfirmationScreen(
    bookingId: String,
    viewModel: HomeEaseViewModel,
    onViewBookings: () -> Unit,
    onGoHome: () -> Unit
) {
    val booking by viewModel.observeBooking(bookingId).collectAsStateWithLifecycle(initialValue = null)
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "confirm_pop"
    )

    Scaffold(containerColor = WarmIvory) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(SageGreenContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.CheckCircle,
                    contentDescription = "Booking confirmed",
                    tint = DeepForest,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text("Booking Confirmed!", style = MaterialTheme.typography.headlineLarge, color = DeepForest)
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Your trusted Pune professional is scheduled.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedOlive
            )
            Spacer(Modifier.height(20.dp))

            booking?.let { b ->
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = PureWhite,
                    border = BorderStroke(1.dp, SubtleBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SummaryRow("Booking Reference", b.id)
                        SummaryRow("Service", "${b.serviceTitle} (${b.categoryName})")
                        SummaryRow("Professional", b.professionalName)
                        SummaryRow("Scheduled Date", b.scheduledDate)
                        SummaryRow("Pune Address", "${b.address}, ${b.locality}")
                        SummaryRow("Agreed Quotation", "₹${b.agreedPriceInr}")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            PressablePrimaryButton(
                text = "View My Bookings",
                onClick = onViewBookings,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("confirm_view_bookings")
            )
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = onGoHome,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Home", color = DeepForest)
            }
        }
    }
}
