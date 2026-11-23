package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContactSupport
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.HomeWork
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.RequestQuote
import androidx.compose.material.icons.rounded.WorkOutline
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.components.HomeEaseLogoMark
import com.example.ui.components.HomeEaseSplashOverlay
import com.example.ui.components.HomeEaseTopBar
import com.example.ui.components.LocalityPickerSheet
import com.example.ui.navigation.HomeEaseRoutes
import com.example.ui.screens.AboutUsScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.BookingConfirmationScreen
import com.example.ui.screens.CompareQuotationsScreen
import com.example.ui.screens.ContactUsScreen
import com.example.ui.screens.CustomerDashboardScreen
import com.example.ui.screens.ExploreServicesScreen
import com.example.ui.screens.HomeProfileScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyBookingsScreen
import com.example.ui.screens.ProfessionalDashboardScreen
import com.example.ui.screens.ProfessionalListingScreen
import com.example.ui.screens.ProfessionalProfileScreen
import com.example.ui.screens.RecentlyMovedScreen
import com.example.ui.screens.RequestQuoteScreen
import com.example.ui.screens.ServiceDetailScreen
import com.example.ui.theme.DeepForest
import com.example.ui.theme.HomeEaseTheme
import com.example.ui.theme.MutedOlive
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SageGreenContainer
import com.example.ui.theme.SoftSandLight
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.WarmIvory
import com.example.ui.viewmodel.HomeEaseViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeEaseTheme {
                HomeEaseApp()
            }
        }
    }
}

private data class TopLevelTab(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeEaseApp(
    viewModel: HomeEaseViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: HomeEaseRoutes.HOME

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showLocalitySheet by remember { mutableStateOf(false) }
    var showNotificationsSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    val dashboardRoute = if (state.isProfessionalMode) {
        HomeEaseRoutes.PRO_DASHBOARD
    } else {
        HomeEaseRoutes.CUSTOMER_DASHBOARD
    }

    val topLevelTabs = remember(dashboardRoute, state.isProfessionalMode) {
        listOf(
            TopLevelTab(HomeEaseRoutes.HOME, "Home", Icons.Rounded.Home, Icons.Outlined.Home, "nav_home"),
            TopLevelTab(HomeEaseRoutes.EXPLORE, "Explore", Icons.Rounded.Explore, Icons.Outlined.Explore, "nav_explore"),
            TopLevelTab(HomeEaseRoutes.BOOKINGS, "Bookings", Icons.Rounded.CalendarMonth, Icons.Outlined.CalendarMonth, "nav_bookings"),
            TopLevelTab(
                dashboardRoute,
                if (state.isProfessionalMode) "Pro Hub" else "Dashboard",
                Icons.Rounded.Dashboard,
                Icons.Outlined.Dashboard,
                "nav_dashboard"
            )
        )
    }

    val isTopLevelRoute = currentRoute in setOf(
        HomeEaseRoutes.HOME,
        HomeEaseRoutes.EXPLORE,
        HomeEaseRoutes.BOOKINGS,
        HomeEaseRoutes.CUSTOMER_DASHBOARD,
        HomeEaseRoutes.PRO_DASHBOARD
    )

    fun navigateTopLevel(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = WarmIvory,
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    HomeEaseLogoMark(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = state.currentUser?.let { "${it.name} (${it.role.lowercase().replaceFirstChar { c -> c.uppercase() }})" }
                            ?: "Guest • Pune",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedOlive,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    HorizontalDivider(color = SubtleBorder)
                    Spacer(Modifier.height(6.dp))

                    DrawerLink("Home", Icons.Rounded.Home) {
                        scope.launch { drawerState.close() }
                        navigateTopLevel(HomeEaseRoutes.HOME)
                    }
                    DrawerLink("Explore Services", Icons.Rounded.Explore) {
                        scope.launch { drawerState.close() }
                        navigateTopLevel(HomeEaseRoutes.EXPLORE)
                    }
                    DrawerLink("Local Professionals", Icons.Rounded.Groups) {
                        scope.launch { drawerState.close() }
                        navController.navigate(HomeEaseRoutes.proListing("all"))
                    }
                    DrawerLink("Request a Quote", Icons.Rounded.RequestQuote) {
                        scope.launch { drawerState.close() }
                        navController.navigate(HomeEaseRoutes.requestQuote())
                    }
                    DrawerLink("My Bookings & Quotes", Icons.Rounded.CalendarMonth) {
                        scope.launch { drawerState.close() }
                        navigateTopLevel(HomeEaseRoutes.BOOKINGS)
                    }
                    DrawerLink("Customer Dashboard", Icons.Rounded.Dashboard) {
                        scope.launch { drawerState.close() }
                        navController.navigate(HomeEaseRoutes.CUSTOMER_DASHBOARD)
                    }
                    DrawerLink("Professional Dashboard", Icons.Rounded.WorkOutline) {
                        scope.launch { drawerState.close() }
                        navController.navigate(HomeEaseRoutes.PRO_DASHBOARD)
                    }
                    DrawerLink("My Home Profile", Icons.Rounded.Home) {
                        scope.launch { drawerState.close() }
                        navController.navigate(HomeEaseRoutes.HOME_PROFILE)
                    }
                    DrawerLink("Recently Moved?", Icons.Rounded.HomeWork) {
                        scope.launch { drawerState.close() }
                        navController.navigate(HomeEaseRoutes.RECENTLY_MOVED)
                    }

                    HorizontalDivider(color = SubtleBorder, modifier = Modifier.padding(vertical = 6.dp))

                    DrawerLink("Login / Sign Up", Icons.Rounded.Login) {
                        scope.launch { drawerState.close() }
                        navController.navigate(HomeEaseRoutes.auth("login"))
                    }
                    DrawerLink("Become a Professional", Icons.Rounded.WorkOutline) {
                        scope.launch { drawerState.close() }
                        navController.navigate(HomeEaseRoutes.auth("pro"))
                    }
                    DrawerLink("About Us", Icons.Rounded.Info) {
                        scope.launch { drawerState.close() }
                        navController.navigate(HomeEaseRoutes.ABOUT)
                    }
                    DrawerLink("Contact Pune Support", Icons.Rounded.ContactSupport) {
                        scope.launch { drawerState.close() }
                        navController.navigate(HomeEaseRoutes.CONTACT)
                    }
                }
            }
        }
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useRail = maxWidth >= 680.dp

            Scaffold(
                containerColor = WarmIvory,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {
                    if (isTopLevelRoute) {
                        HomeEaseTopBar(
                            selectedLocality = state.selectedLocality,
                            unreadCount = state.unreadNotificationCount,
                            onLocalityClick = { showLocalitySheet = true },
                            onNotificationsClick = { showNotificationsSheet = true },
                            onMenuClick = { scope.launch { drawerState.open() } },
                            onLogoClick = { navigateTopLevel(HomeEaseRoutes.HOME) }
                        )
                    }
                },
                bottomBar = {
                    if (isTopLevelRoute && !useRail) {
                        NavigationBar(
                            containerColor = PureWhite,
                            tonalElevation = 4.dp,
                            modifier = Modifier.navigationBarsPadding()
                        ) {
                            topLevelTabs.forEach { tab ->
                                val selected = currentRoute == tab.route
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { navigateTopLevel(tab.route) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = tab.label
                                        )
                                    },
                                    label = { Text(tab.label, style = MaterialTheme.typography.labelSmall) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = DeepForest,
                                        selectedTextColor = DeepForest,
                                        indicatorColor = SageGreenContainer,
                                        unselectedIconColor = MutedOlive,
                                        unselectedTextColor = MutedOlive
                                    ),
                                    modifier = Modifier.testTag(tab.testTag)
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (isTopLevelRoute && useRail) {
                        NavigationRail(containerColor = PureWhite) {
                            Spacer(Modifier.height(12.dp))
                            topLevelTabs.forEach { tab ->
                                val selected = currentRoute == tab.route
                                NavigationRailItem(
                                    selected = selected,
                                    onClick = { navigateTopLevel(tab.route) },
                                    icon = {
                                        Icon(
                                            if (selected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = tab.label
                                        )
                                    },
                                    label = { Text(tab.label) }
                                )
                            }
                        }
                    }

                    NavHost(
                        navController = navController,
                        startDestination = HomeEaseRoutes.HOME,
                        enterTransition = { fadeIn() + slideInHorizontally { it / 16 } },
                        exitTransition = { fadeOut() },
                        popEnterTransition = { fadeIn() },
                        popExitTransition = { fadeOut() + slideOutHorizontally { it / 16 } },
                        modifier = Modifier.weight(1f)
                    ) {
                        composable(HomeEaseRoutes.HOME) {
                            HomeScreen(
                                state = state,
                                onSearchQueryChange = viewModel::updateSearchQuery,
                                onSearchSubmit = { navigateTopLevel(HomeEaseRoutes.EXPLORE) },
                                onLocalityClick = { showLocalitySheet = true },
                                onExploreServices = { navigateTopLevel(HomeEaseRoutes.EXPLORE) },
                                onCategoryClick = { catId -> navController.navigate(HomeEaseRoutes.serviceDetail(catId)) },
                                onViewProProfile = { proId -> navController.navigate(HomeEaseRoutes.proProfile(proId)) },
                                onToggleShortlist = viewModel::toggleShortlist,
                                onRecentlyMovedClick = { navController.navigate(HomeEaseRoutes.RECENTLY_MOVED) },
                                onBecomeProClick = { navController.navigate(HomeEaseRoutes.auth("pro")) },
                                onLoginClick = { navController.navigate(HomeEaseRoutes.auth("login")) },
                                onRequestQuoteClick = { navController.navigate(HomeEaseRoutes.requestQuote()) },
                                onAboutClick = { navController.navigate(HomeEaseRoutes.ABOUT) },
                                onContactClick = { navController.navigate(HomeEaseRoutes.CONTACT) },
                                onViewAllPros = { navController.navigate(HomeEaseRoutes.proListing("all")) }
                            )
                        }

                        composable(HomeEaseRoutes.EXPLORE) {
                            ExploreServicesScreen(
                                state = state,
                                onSearchQueryChange = viewModel::updateSearchQuery,
                                onCategoryClick = { catId -> navController.navigate(HomeEaseRoutes.serviceDetail(catId)) },
                                onViewProProfile = { proId -> navController.navigate(HomeEaseRoutes.proProfile(proId)) },
                                onToggleShortlist = viewModel::toggleShortlist,
                                onRequestQuoteClick = { catId -> navController.navigate(HomeEaseRoutes.requestQuote(catId)) },
                                onViewAllPros = { navController.navigate(HomeEaseRoutes.proListing("all")) }
                            )
                        }

                        composable(
                            route = HomeEaseRoutes.SERVICE_DETAIL,
                            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
                        ) { entry ->
                            val catId = entry.arguments?.getString("categoryId") ?: "painting"
                            ServiceDetailScreen(
                                categoryId = catId,
                                state = state,
                                onBack = { navController.popBackStack() },
                                onRequestQuote = { id -> navController.navigate(HomeEaseRoutes.requestQuote(id)) },
                                onViewProsInCategory = { id -> navController.navigate(HomeEaseRoutes.proListing(id)) },
                                onViewProProfile = { proId -> navController.navigate(HomeEaseRoutes.proProfile(proId)) },
                                onToggleShortlist = viewModel::toggleShortlist
                            )
                        }

                        composable(
                            route = HomeEaseRoutes.PRO_LISTING,
                            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
                        ) { entry ->
                            val catId = entry.arguments?.getString("categoryId") ?: "all"
                            ProfessionalListingScreen(
                                initialCategoryId = catId,
                                state = state,
                                onBack = { navController.popBackStack() },
                                onViewProProfile = { proId -> navController.navigate(HomeEaseRoutes.proProfile(proId)) },
                                onToggleShortlist = viewModel::toggleShortlist
                            )
                        }

                        composable(
                            route = HomeEaseRoutes.PRO_PROFILE,
                            arguments = listOf(navArgument("proId") { type = NavType.StringType })
                        ) { entry ->
                            val proId = entry.arguments?.getString("proId") ?: ""
                            ProfessionalProfileScreen(
                                proId = proId,
                                viewModel = viewModel,
                                state = state,
                                onBack = { navController.popBackStack() },
                                onRequestQuoteForPro = { catId, pId ->
                                    navController.navigate(HomeEaseRoutes.requestQuote(catId, false, pId))
                                }
                            )
                        }

                        composable(
                            route = HomeEaseRoutes.REQUEST_QUOTE,
                            arguments = listOf(
                                navArgument("categoryId") { type = NavType.StringType; defaultValue = "painting" },
                                navArgument("bundle") { type = NavType.BoolType; defaultValue = false },
                                navArgument("proId") { type = NavType.StringType; defaultValue = "" }
                            )
                        ) { entry ->
                            val catId = entry.arguments?.getString("categoryId") ?: "painting"
                            val bundle = entry.arguments?.getBoolean("bundle") ?: false
                            val proId = entry.arguments?.getString("proId") ?: ""
                            RequestQuoteScreen(
                                initialCategoryId = catId,
                                initialBundle = bundle,
                                preferredProId = proId,
                                state = state,
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onRequestSubmitted = { reqId ->
                                    navController.navigate(HomeEaseRoutes.compareQuotes(reqId)) {
                                        popUpTo(HomeEaseRoutes.HOME)
                                    }
                                }
                            )
                        }

                        composable(
                            route = HomeEaseRoutes.COMPARE_QUOTES,
                            arguments = listOf(navArgument("requestId") { type = NavType.StringType })
                        ) { entry ->
                            val reqId = entry.arguments?.getString("requestId") ?: ""
                            CompareQuotationsScreen(
                                requestId = reqId,
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onViewProProfile = { proId -> navController.navigate(HomeEaseRoutes.proProfile(proId)) },
                                onSwitchToProDashboard = {
                                    viewModel.switchDemoAccount(isPro = true) {
                                        navController.navigate(HomeEaseRoutes.PRO_DASHBOARD)
                                    }
                                },
                                onBookingConfirmed = { bookingId ->
                                    navController.navigate(HomeEaseRoutes.bookingConfirm(bookingId))
                                }
                            )
                        }

                        composable(
                            route = HomeEaseRoutes.BOOKING_CONFIRM,
                            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
                        ) { entry ->
                            val bookingId = entry.arguments?.getString("bookingId") ?: ""
                            BookingConfirmationScreen(
                                bookingId = bookingId,
                                viewModel = viewModel,
                                onViewBookings = {
                                    navController.navigate(HomeEaseRoutes.BOOKINGS) {
                                        popUpTo(HomeEaseRoutes.HOME)
                                    }
                                },
                                onGoHome = {
                                    navController.navigate(HomeEaseRoutes.HOME) {
                                        popUpTo(HomeEaseRoutes.HOME) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(HomeEaseRoutes.BOOKINGS) {
                            MyBookingsScreen(
                                state = state,
                                viewModel = viewModel,
                                onRequestQuoteClick = { navController.navigate(HomeEaseRoutes.requestQuote()) },
                                onCompareQuotesClick = { reqId -> navController.navigate(HomeEaseRoutes.compareQuotes(reqId)) },
                                onViewProProfile = { proId -> navController.navigate(HomeEaseRoutes.proProfile(proId)) }
                            )
                        }

                        composable(HomeEaseRoutes.CUSTOMER_DASHBOARD) {
                            CustomerDashboardScreen(
                                state = state,
                                viewModel = viewModel,
                                onExploreServices = { navigateTopLevel(HomeEaseRoutes.EXPLORE) },
                                onCategoryClick = { catId -> navController.navigate(HomeEaseRoutes.serviceDetail(catId)) },
                                onViewProProfile = { proId -> navController.navigate(HomeEaseRoutes.proProfile(proId)) },
                                onOpenBookings = { navigateTopLevel(HomeEaseRoutes.BOOKINGS) },
                                onOpenCompareQuotes = { reqId -> navController.navigate(HomeEaseRoutes.compareQuotes(reqId)) },
                                onOpenHomeProfile = { navController.navigate(HomeEaseRoutes.HOME_PROFILE) },
                                onOpenRecentlyMoved = { navController.navigate(HomeEaseRoutes.RECENTLY_MOVED) },
                                onOpenNotifications = { showNotificationsSheet = true },
                                onOpenAuth = { navController.navigate(HomeEaseRoutes.auth("login")) },
                                onSwitchToProDemo = {
                                    viewModel.switchDemoAccount(isPro = true) {
                                        navigateTopLevel(HomeEaseRoutes.PRO_DASHBOARD)
                                    }
                                }
                            )
                        }

                        composable(HomeEaseRoutes.PRO_DASHBOARD) {
                            ProfessionalDashboardScreen(
                                state = state,
                                viewModel = viewModel,
                                onBecomePro = { navController.navigate(HomeEaseRoutes.auth("pro")) },
                                onSwitchToCustomerDemo = {
                                    viewModel.switchDemoAccount(isPro = false) {
                                        navigateTopLevel(HomeEaseRoutes.CUSTOMER_DASHBOARD)
                                    }
                                }
                            )
                        }

                        composable(HomeEaseRoutes.HOME_PROFILE) {
                            HomeProfileScreen(
                                state = state,
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(HomeEaseRoutes.RECENTLY_MOVED) {
                            RecentlyMovedScreen(
                                onBack = { navController.popBackStack() },
                                onStartBundleQuote = { catId ->
                                    navController.navigate(HomeEaseRoutes.requestQuote(catId, bundle = true))
                                },
                                onExploreCategory = { catId ->
                                    navController.navigate(HomeEaseRoutes.serviceDetail(catId))
                                }
                            )
                        }

                        composable(
                            route = HomeEaseRoutes.AUTH,
                            arguments = listOf(navArgument("tab") { type = NavType.StringType; defaultValue = "login" })
                        ) { entry ->
                            val tab = entry.arguments?.getString("tab") ?: "login"
                            AuthScreen(
                                initialTab = tab,
                                state = state,
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onAuthenticated = { isPro ->
                                    navController.navigate(
                                        if (isPro) HomeEaseRoutes.PRO_DASHBOARD else HomeEaseRoutes.CUSTOMER_DASHBOARD
                                    ) {
                                        popUpTo(HomeEaseRoutes.HOME)
                                    }
                                }
                            )
                        }

                        composable(HomeEaseRoutes.ABOUT) {
                            AboutUsScreen(
                                onBack = { navController.popBackStack() },
                                onExploreServices = { navigateTopLevel(HomeEaseRoutes.EXPLORE) }
                            )
                        }

                        composable(HomeEaseRoutes.CONTACT) {
                            ContactUsScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showLocalitySheet) {
        LocalityPickerSheet(
            selectedLocality = state.selectedLocality,
            onSelect = viewModel::selectLocality,
            onDismiss = { showLocalitySheet = false }
        )
    }

    if (showNotificationsSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.markNotificationsRead()
                showNotificationsSheet = false
            },
            containerColor = WarmIvory
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Rounded.Notifications, contentDescription = null, tint = DeepForest)
                        Text("Notifications", style = MaterialTheme.typography.headlineSmall, color = DeepForest)
                    }
                    Row {
                        TextButton(onClick = { viewModel.markNotificationsRead() }) {
                            Text("Mark read", color = DeepForest)
                        }
                        IconButton(onClick = { showNotificationsSheet = false }) {
                            Icon(Icons.Rounded.Close, contentDescription = "Close", tint = DeepForest)
                        }
                    }
                }
                if (state.notifications.isEmpty()) {
                    Text("You're all caught up.", style = MaterialTheme.typography.bodyMedium, color = MutedOlive)
                } else {
                    state.notifications.forEach { notif ->
                        Surface(
                            onClick = {
                                viewModel.markNotificationsRead()
                                showNotificationsSheet = false
                                notif.relatedRoute?.let { route ->
                                    navController.navigate(route)
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            color = if (notif.isRead) PureWhite else SoftSandLight,
                            border = BorderStroke(1.dp, SubtleBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(notif.title, style = MaterialTheme.typography.titleSmall, color = DeepForest)
                                Text(notif.message, style = MaterialTheme.typography.bodySmall, color = MutedOlive)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    HomeEaseSplashOverlay(
        visible = state.isInitialLoading,
        onSkip = viewModel::dismissSplashEarly
    )
}

@Composable
private fun DrawerLink(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(label, style = MaterialTheme.typography.labelLarge) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = null) },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = WarmIvory,
            unselectedIconColor = DeepForest,
            unselectedTextColor = DeepForest
        )
    )
}
