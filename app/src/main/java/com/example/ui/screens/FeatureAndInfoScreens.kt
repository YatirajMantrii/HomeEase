package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.CatalogData
import com.example.ui.components.HomeEaseLogoMark
import com.example.ui.components.HomeEaseSubTopBar
import com.example.ui.components.LocalModeNoticeBanner
import com.example.ui.components.PressablePrimaryButton
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeProfileScreen(
    state: HomeEaseUiState,
    viewModel: HomeEaseViewModel,
    onBack: () -> Unit
) {
    val existing = state.homeProfile
    var homeType by remember(existing) { mutableStateOf(existing?.homeType ?: "2 BHK Apartment") }
    var sqFtText by remember(existing) { mutableStateOf((existing?.approxSqFt ?: 1050).toString()) }
    var locality by remember(existing) { mutableStateOf(existing?.locality ?: "Kothrud") }
    var preferredSlot by remember(existing) { mutableStateOf(existing?.preferredSlot ?: CatalogData.preferredSlots.first()) }
    var notes by remember(existing) { mutableStateOf(existing?.notes ?: "") }

    Scaffold(
        containerColor = WarmIvory,
        topBar = { HomeEaseSubTopBar(title = "My Home Profile", onBack = onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = DeepForest,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Home, contentDescription = null, tint = SoftSand, modifier = Modifier.size(34.dp))
                        Column {
                            Text("Your Pune Property Details", style = MaterialTheme.typography.titleLarge, color = WarmIvory)
                            Text(
                                "Pre-fills service requests and helps professionals send accurate quotations.",
                                style = MaterialTheme.typography.bodySmall,
                                color = SoftSandLight
                            )
                        }
                    }
                }
            }

            item {
                Text("Home Type", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                Spacer(Modifier.height(6.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CatalogData.homeTypes.forEach { type ->
                        FilterChip(
                            selected = homeType == type,
                            onClick = { homeType = type },
                            label = { Text(type) },
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
                    value = sqFtText,
                    onValueChange = { sqFtText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Approximate Carpet Area (sq. ft.)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text("Pune Locality", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                Spacer(Modifier.height(6.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CatalogData.puneLocalities.drop(1).forEach { area ->
                        FilterChip(
                            selected = locality == area,
                            onClick = { locality = area },
                            label = { Text(area) }
                        )
                    }
                }
            }

            item {
                Text("Preferred Service Window", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                Spacer(Modifier.height(6.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CatalogData.preferredSlots.forEach { slot ->
                        FilterChip(
                            selected = preferredSlot == slot,
                            onClick = { preferredSlot = slot },
                            label = { Text(slot) }
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Home Preferences & Society Notes") },
                    minLines = 3,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                PressablePrimaryButton(
                    text = "Save Home Profile",
                    onClick = {
                        viewModel.saveHomeProfile(
                            homeType = homeType,
                            approxSqFt = sqFtText.toIntOrNull() ?: 1000,
                            locality = locality,
                            preferredSlot = preferredSlot,
                            notes = notes
                        )
                        onBack()
                    },
                    icon = Icons.Rounded.Save,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_home_profile_button")
                )
            }
        }
    }
}

@Composable
fun RecentlyMovedScreen(
    onBack: () -> Unit,
    onStartBundleQuote: (String) -> Unit,
    onExploreCategory: (String) -> Unit
) {
    Scaffold(
        containerColor = WarmIvory,
        topBar = { HomeEaseSubTopBar(title = "Recently Moved?", onBack = onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = DeepForest)
                ) {
                    Column {
                        Box(modifier = Modifier.fillMaxWidth().height(175.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.img_recently_moved_banner),
                                contentDescription = "Move in Pune home banner",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Brush.verticalGradient(listOf(Color.Transparent, DeepForest)))
                            )
                            Text(
                                text = "New to Pune or Just Shifted Flats?",
                                style = MaterialTheme.typography.headlineMedium,
                                color = WarmIvory,
                                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
                            )
                        }
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Settling into a new home shouldn't mean calling five separate shops. Request coordinated quotes for essential move-in setup.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SoftSandLight
                            )
                            PressablePrimaryButton(
                                text = "Request Move-In Bundle Quote",
                                onClick = { onStartBundleQuote("cleaning") },
                                containerColor = SoftSand,
                                contentColor = DeepForest,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            item {
                Text("Included Move-In Essentials", style = MaterialTheme.typography.titleLarge, color = DeepForest)
            }

            items(CatalogData.relocationBundleItems) { item ->
                Surface(
                    onClick = { onExploreCategory(item.categoryId) },
                    shape = RoundedCornerShape(18.dp),
                    color = PureWhite,
                    border = BorderStroke(1.dp, SubtleBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = SageGreen)
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(item.title, style = MaterialTheme.typography.titleSmall, color = DeepForest)
                                Surface(shape = RoundedCornerShape(50), color = SoftSandLight) {
                                    Text(
                                        item.estimatedSavingNote,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = DeepForest,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                            Text(item.summary, style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuthScreen(
    initialTab: String,
    state: HomeEaseUiState,
    viewModel: HomeEaseViewModel,
    onBack: () -> Unit,
    onAuthenticated: (isPro: Boolean) -> Unit
) {
    var selectedTab by remember {
        mutableIntStateOf(
            when (initialTab) {
                "signup" -> 1
                "pro" -> 2
                else -> 0
            }
        )
    }

    // Login fields
    var loginEmail by remember { mutableStateOf("aarav@pune.homeease.local") }
    var loginPassword by remember { mutableStateOf("homeease123") }

    // Customer SignUp fields
    var custName by remember { mutableStateOf("") }
    var custEmail by remember { mutableStateOf("") }
    var custPhone by remember { mutableStateOf("") }
    var custLocality by remember { mutableStateOf("Kothrud") }
    var custPassword by remember { mutableStateOf("") }

    // Become a Pro fields
    var proName by remember { mutableStateOf("") }
    var proEmail by remember { mutableStateOf("") }
    var proPhone by remember { mutableStateOf("") }
    var proPassword by remember { mutableStateOf("") }
    var proBusiness by remember { mutableStateOf("") }
    var proCategory by remember { mutableStateOf("painting") }
    var proExp by remember { mutableStateOf("5") }
    var proPrice by remember { mutableStateOf("499") }
    var proBio by remember { mutableStateOf("") }
    val proAreas = remember { mutableStateListOf("Kothrud", "Baner") }

    Scaffold(
        containerColor = WarmIvory,
        topBar = { HomeEaseSubTopBar(title = "HomeEase Account", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(selectedTabIndex = selectedTab, containerColor = WarmIvory, contentColor = DeepForest) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0; viewModel.clearAuthError() }, text = { Text("Login") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1; viewModel.clearAuthError() }, text = { Text("Sign Up") })
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2; viewModel.clearAuthError() }, text = { Text("Become a Pro") })
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { LocalModeNoticeBanner() }

                state.authError?.let { err ->
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF9E5E2),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(err, color = WarmCoral, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(12.dp))
                        }
                    }
                }

                when (selectedTab) {
                    0 -> {
                        item {
                            Text("Sign in to your Pune HomeEase account", style = MaterialTheme.typography.titleLarge, color = DeepForest)
                        }
                        item {
                            OutlinedTextField(
                                value = loginEmail,
                                onValueChange = { loginEmail = it },
                                label = { Text("Email") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = loginPassword,
                                onValueChange = { loginPassword = it },
                                label = { Text("Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        item {
                            PressablePrimaryButton(
                                text = "Sign In",
                                onClick = {
                                    viewModel.signIn(loginEmail, loginPassword) { user ->
                                        onAuthenticated(user.role == "PROFESSIONAL")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        item {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = PureWhite,
                                border = BorderStroke(1.dp, SubtleBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("Quick Demo Role Switch", style = MaterialTheme.typography.titleSmall, color = DeepForest)
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        OutlinedButton(
                                            onClick = {
                                                viewModel.switchDemoAccount(isPro = false) { onAuthenticated(false) }
                                            },
                                            modifier = Modifier.weight(1f)
                                        ) { Text("Aarav (Customer)", color = DeepForest) }
                                        OutlinedButton(
                                            onClick = {
                                                viewModel.switchDemoAccount(isPro = true) { onAuthenticated(true) }
                                            },
                                            modifier = Modifier.weight(1f)
                                        ) { Text("Rohan (Pro)", color = DeepForest) }
                                    }
                                    if (state.currentUser != null) {
                                        OutlinedButton(
                                            onClick = { viewModel.signOut() },
                                            modifier = Modifier.fillMaxWidth()
                                        ) { Text("Sign Out (${state.currentUser.name})", color = WarmCoral) }
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        item { Text("Create Customer Account", style = MaterialTheme.typography.titleLarge, color = DeepForest) }
                        item { OutlinedTextField(value = custName, onValueChange = { custName = it }, label = { Text("Full Name") }, singleLine = true, modifier = Modifier.fillMaxWidth()) }
                        item { OutlinedTextField(value = custEmail, onValueChange = { custEmail = it }, label = { Text("Email Address") }, singleLine = true, modifier = Modifier.fillMaxWidth()) }
                        item { OutlinedTextField(value = custPhone, onValueChange = { custPhone = it }, label = { Text("Phone Number (+91)") }, singleLine = true, modifier = Modifier.fillMaxWidth()) }
                        item {
                            Text("Primary Pune Locality", style = MaterialTheme.typography.labelLarge, color = DeepForest)
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                CatalogData.puneLocalities.drop(1).forEach { area ->
                                    FilterChip(selected = custLocality == area, onClick = { custLocality = area }, label = { Text(area) })
                                }
                            }
                        }
                        item {
                            OutlinedTextField(
                                value = custPassword,
                                onValueChange = { custPassword = it },
                                label = { Text("Create Password (6+ chars)") },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        item {
                            PressablePrimaryButton(
                                text = "Create Customer Account",
                                onClick = {
                                    viewModel.signUpCustomer(custName, custEmail, custPhone, custLocality, custPassword) {
                                        onAuthenticated(false)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    2 -> {
                        item { Text("Join HomeEase as a Pune Professional", style = MaterialTheme.typography.titleLarge, color = DeepForest) }
                        item { OutlinedTextField(value = proName, onValueChange = { proName = it }, label = { Text("Your Full Name") }, singleLine = true, modifier = Modifier.fillMaxWidth()) }
                        item { OutlinedTextField(value = proBusiness, onValueChange = { proBusiness = it }, label = { Text("Business / Trade Name") }, singleLine = true, modifier = Modifier.fillMaxWidth()) }
                        item { OutlinedTextField(value = proEmail, onValueChange = { proEmail = it }, label = { Text("Email Address") }, singleLine = true, modifier = Modifier.fillMaxWidth()) }
                        item { OutlinedTextField(value = proPhone, onValueChange = { proPhone = it }, label = { Text("Phone Number") }, singleLine = true, modifier = Modifier.fillMaxWidth()) }
                        item {
                            Text("Service Category", style = MaterialTheme.typography.labelLarge, color = DeepForest)
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                CatalogData.categories.forEach { cat ->
                                    FilterChip(selected = proCategory == cat.id, onClick = { proCategory = cat.id }, label = { Text(cat.name) })
                                }
                            }
                        }
                        item {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = proExp,
                                    onValueChange = { proExp = it.filter { c -> c.isDigit() } },
                                    label = { Text("Years Exp.") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = proPrice,
                                    onValueChange = { proPrice = it.filter { c -> c.isDigit() } },
                                    label = { Text("Starting ₹") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        item {
                            Text("Pune Service Areas", style = MaterialTheme.typography.labelLarge, color = DeepForest)
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                CatalogData.puneLocalities.drop(1).forEach { area ->
                                    val selected = area in proAreas
                                    FilterChip(
                                        selected = selected,
                                        onClick = { if (selected) proAreas.remove(area) else proAreas.add(area) },
                                        label = { Text(area) }
                                    )
                                }
                            }
                        }
                        item { OutlinedTextField(value = proBio, onValueChange = { proBio = it }, label = { Text("Short Bio & Craftsmanship Focus") }, minLines = 2, modifier = Modifier.fillMaxWidth()) }
                        item {
                            OutlinedTextField(
                                value = proPassword,
                                onValueChange = { proPassword = it },
                                label = { Text("Password (6+ chars)") },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        item {
                            PressablePrimaryButton(
                                text = "Launch Professional Profile",
                                onClick = {
                                    viewModel.registerProfessional(
                                        name = proName,
                                        email = proEmail,
                                        phone = proPhone,
                                        password = proPassword,
                                        businessName = proBusiness,
                                        categoryId = proCategory,
                                        experienceYears = proExp.toIntOrNull() ?: 3,
                                        startingPriceInr = proPrice.toIntOrNull() ?: 499,
                                        serviceAreas = proAreas.toList(),
                                        bio = proBio
                                    ) {
                                        onAuthenticated(true)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AboutUsScreen(onBack: () -> Unit, onExploreServices: () -> Unit) {
    Scaffold(
        containerColor = WarmIvory,
        topBar = { HomeEaseSubTopBar(title = "About HomeEase", onBack = onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = DeepForest,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        HomeEaseLogoMark(darkSurface = true)
                        Text(
                            text = "Finding reliable home services should feel effortless.",
                            style = MaterialTheme.typography.headlineSmall,
                            color = WarmIvory
                        )
                        Text(
                            text = "Built for Pune homeowners, families, and newcomers, HomeEase replaces endless phone calls and surprise quotes with calm, transparent side-by-side comparisons.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SoftSandLight
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
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Our Craft-First Philosophy", style = MaterialTheme.typography.titleLarge, color = DeepForest)
                        Text(
                            "• Respect for your time & space: Clear completion estimates and tidy handovers.\n• Honest quotations: Every quote lists included services and any optional extra charges upfront.\n• Local Pune roots: Focused on Kothrud, Baner, Koregaon Park, Viman Nagar, Wakad, Aundh, and nearby neighborhoods.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MutedOlive
                        )
                        PressablePrimaryButton(text = "Explore Services", onClick = onExploreServices, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
fun ContactUsScreen(
    viewModel: HomeEaseViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf("Booking Support") }
    var message by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = WarmIvory,
        topBar = { HomeEaseSubTopBar(title = "Contact Pune Support", onBack = onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PureWhite,
                    border = BorderStroke(1.dp, SubtleBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("HomeEase Pune Helpdesk", style = MaterialTheme.typography.titleLarge, color = DeepForest)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = SageGreen, modifier = Modifier.size(18.dp))
                            Text("Lane 5, Bhandarkar Road, Deccan Gymkhana, Pune 411004", style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Rounded.Phone, contentDescription = null, tint = SageGreen, modifier = Modifier.size(18.dp))
                            Text("+91 20 4852 1900 (Mon–Sun, 8 AM – 8 PM)", style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Rounded.Email, contentDescription = null, tint = SageGreen, modifier = Modifier.size(18.dp))
                            Text("care@homeease-pune.local", style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                        }
                    }
                }
            }

            if (submitted) {
                item {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = SageGreenContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Rounded.Spa, contentDescription = null, tint = DeepForest)
                            Text("Thank you for reaching out!", style = MaterialTheme.typography.titleMedium, color = DeepForest)
                            Text("Your message has been logged locally. Remote cloud ticketing requires backend setup.", style = MaterialTheme.typography.bodySmall, color = DeepForest)
                        }
                    }
                }
            } else {
                item { OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Your Name") }, singleLine = true, modifier = Modifier.fillMaxWidth()) }
                item { OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email Address") }, singleLine = true, modifier = Modifier.fillMaxWidth()) }
                item { OutlinedTextField(value = topic, onValueChange = { topic = it }, label = { Text("Topic") }, singleLine = true, modifier = Modifier.fillMaxWidth()) }
                item { OutlinedTextField(value = message, onValueChange = { message = it }, label = { Text("How can we help?") }, minLines = 4, modifier = Modifier.fillMaxWidth()) }
                item {
                    PressablePrimaryButton(
                        text = "Send Inquiry",
                        onClick = {
                            viewModel.submitContactInquiry(name, email, topic, message) {
                                submitted = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
