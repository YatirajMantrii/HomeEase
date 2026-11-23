package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.local.ProfessionalEntity
import com.example.data.model.CatalogData
import com.example.ui.theme.AmberGold
import com.example.ui.theme.DeepForest
import com.example.ui.theme.MutedOlive
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SoftSand
import com.example.ui.theme.SoftSandLight
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.WarmIvory
import kotlinx.coroutines.delay

@Composable
fun HomeEaseLogoMark(
    modifier: Modifier = Modifier,
    darkSurface: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (darkSurface) WarmIvory else DeepForest),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Home,
                contentDescription = "HomeEase logo",
                tint = if (darkSurface) DeepForest else WarmIvory,
                modifier = Modifier.size(19.dp)
            )
            Icon(
                imageVector = Icons.Rounded.Spa,
                contentDescription = null,
                tint = SageGreen,
                modifier = Modifier
                    .size(10.dp)
                    .align(Alignment.BottomEnd)
                    .padding(1.dp)
            )
        }
        Text(
            text = "HomeEase",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = if (darkSurface) WarmIvory else DeepForest
        )
    }
}

@Composable
fun HomeEaseSplashOverlay(
    visible: Boolean,
    onSkip: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(200)),
        exit = fadeOut(tween(420))
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "splash_pulse")
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.94f,
            targetValue = 1.04f,
            animationSpec = infiniteRepeatable(
                animation = tween(900, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "logo_scale"
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(DeepForest, Color(0xFF1D2B26))
                    )
                )
                .clickable(onClick = onSkip),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(32.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .scale(scale)
                        .clip(RoundedCornerShape(24.dp))
                        .background(WarmIvory),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        contentDescription = "HomeEase emblem",
                        tint = DeepForest,
                        modifier = Modifier.size(46.dp)
                    )
                    Icon(
                        imageVector = Icons.Rounded.Spa,
                        contentDescription = null,
                        tint = SageGreen,
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.BottomEnd)
                            .padding(6.dp)
                    )
                }
                Text(
                    text = "HomeEase",
                    style = MaterialTheme.typography.displayMedium,
                    color = WarmIvory
                )
                Text(
                    text = "Finding reliable home services should feel effortless.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SoftSand
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeEaseTopBar(
    selectedLocality: String,
    unreadCount: Int,
    onLocalityClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onMenuClick: () -> Unit,
    onLogoClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = WarmIvory.copy(alpha = 0.96f),
            titleContentColor = DeepForest
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HomeEaseLogoMark(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onLogoClick)
                        .padding(vertical = 4.dp)
                )
                Surface(
                    onClick = onLocalityClick,
                    shape = RoundedCornerShape(50),
                    color = PureWhite,
                    border = BorderStroke(1.dp, SubtleBorder),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("location_selector_chip")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = "Selected Pune locality",
                            tint = SageGreen,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = if (selectedLocality.startsWith("Pune")) "Pune" else selectedLocality,
                            style = MaterialTheme.typography.labelMedium,
                            color = DeepForest,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MutedOlive,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        },
        actions = {
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier.testTag("notifications_button")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadCount > 0) {
                            Badge(containerColor = DeepForest, contentColor = PureWhite) {
                                Text(unreadCount.toString())
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Notifications,
                        contentDescription = "Notifications",
                        tint = DeepForest
                    )
                }
            }
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.testTag("menu_drawer_button")
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Open navigation menu",
                    tint = DeepForest
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeEaseSubTopBar(
    title: String,
    onBack: () -> Unit,
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = WarmIvory,
            titleContentColor = DeepForest
        ),
        navigationIcon = {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = DeepForest
                )
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = { actions() }
    )
}

@Composable
fun PressablePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    containerColor: Color = DeepForest,
    contentColor: Color = PureWhite
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "btn_press"
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp),
        modifier = modifier
            .scale(scale)
            .minimumInteractiveComponentSize()
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun StaggeredEnter(
    index: Int,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay((index.coerceAtMost(8) * 45L))
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(260)) + slideInVertically(tween(260)) { it / 8 }
    ) {
        content()
    }
}

@Composable
fun LocalModeNoticeBanner(
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = SoftSandLight,
        border = BorderStroke(1.dp, SoftSand)
    ) {
        Row(
            modifier = Modifier.padding(if (compact) 12.dp else 14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Rounded.Info,
                contentDescription = "Local mode notice",
                tint = DeepForest,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = if (compact) {
                    "Local On-Device Mode: Data saves on this device. Cloud sync & live external pro dispatch require backend setup."
                } else {
                    "Local On-Device Mode: Accounts, requests, quotations, bookings, and home profiles persist locally in SQLite. Cloud sync, remote Google/Supabase authentication, and live external push dispatch require backend configuration."
                },
                style = MaterialTheme.typography.bodySmall,
                color = DeepForest
            )
        }
    }
}

@Composable
fun ProfessionalSummaryCard(
    pro: ProfessionalEntity,
    isShortlisted: Boolean,
    onToggleShortlist: () -> Unit,
    onViewProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val category = CatalogData.categoryById(pro.categoryId)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onViewProfile),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SubtleBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(SageGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = pro.name.split(" ").take(2).joinToString("") { it.take(1) },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = DeepForest
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pro.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = DeepForest,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${pro.businessName} • ${category.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedOlive,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(
                    onClick = onToggleShortlist,
                    modifier = Modifier.testTag("shortlist_${pro.id}")
                ) {
                    Icon(
                        imageVector = if (isShortlisted) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = if (isShortlisted) "Remove from shortlist" else "Save to shortlist",
                        tint = if (isShortlisted) Color(0xFFC45B4B) else MutedOlive
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = SoftSandLight
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Star,
                            contentDescription = null,
                            tint = AmberGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${pro.rating} (${pro.reviewCount})",
                            style = MaterialTheme.typography.labelMedium,
                            color = DeepForest
                        )
                    }
                }
                Text(
                    text = "${pro.experienceYears} yrs exp",
                    style = MaterialTheme.typography.labelMedium,
                    color = MutedOlive
                )
                Text(
                    text = "• ${pro.completedJobs} jobs",
                    style = MaterialTheme.typography.labelMedium,
                    color = MutedOlive
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Areas: ${pro.serviceAreasCsv}",
                style = MaterialTheme.typography.bodySmall,
                color = MutedOlive,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (pro.pricingModel == "Quote-Based") "Quote-based pricing" else "Starts at ₹${pro.startingPriceInr}",
                        style = MaterialTheme.typography.titleSmall,
                        color = DeepForest
                    )
                    Text(
                        text = pro.responseTimeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = SageGreen
                    )
                }
                Surface(
                    onClick = onViewProfile,
                    shape = RoundedCornerShape(12.dp),
                    color = SageGreenContainer,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("view_profile_${pro.id}")
                ) {
                    Text(
                        text = "View Profile",
                        style = MaterialTheme.typography.labelLarge,
                        color = DeepForest,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalityPickerSheet(
    selectedLocality: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = WarmIvory
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Pune Locality",
                    style = MaterialTheme.typography.headlineSmall,
                    color = DeepForest
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close", tint = DeepForest)
                }
            }
            Text(
                text = "Discover professionals serving your neighbourhood in Pune.",
                style = MaterialTheme.typography.bodySmall,
                color = MutedOlive
            )
            Spacer(Modifier.height(4.dp))
            CatalogData.puneLocalities.forEach { area ->
                val active = area == selectedLocality
                Surface(
                    onClick = {
                        onSelect(area)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(14.dp),
                    color = if (active) DeepForest else PureWhite,
                    border = BorderStroke(1.dp, if (active) DeepForest else SubtleBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                Icons.Rounded.LocationOn,
                                contentDescription = null,
                                tint = if (active) SoftSand else SageGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = area,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (active) PureWhite else DeepForest
                            )
                        }
                        if (active) {
                            Text(
                                text = "Active",
                                style = MaterialTheme.typography.labelMedium,
                                color = SoftSand
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
