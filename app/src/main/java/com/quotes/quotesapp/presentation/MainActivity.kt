package com.quotes.quotesapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aghajari.compose.lazyswipecards.LazySwipeCards
import com.aghajari.compose.lazyswipecards.SwipeDirection
import com.quotes.quotesapp.presentation.favorites.FavoritesScreen
import com.quotes.quotesapp.presentation.model.QuoteUiModel
import com.quotes.quotesapp.presentation.settings.SettingsViewModel
import com.quotes.quotesapp.presentation.ui.theme.DarkPrimary
import com.quotes.quotesapp.presentation.ui.theme.LightPrimary
import com.quotes.quotesapp.presentation.ui.theme.QuotesAppTheme
import com.quotes.quotesapp.presentation.viewmodel.MainViewModel
import com.quotes.quotesapp.presentation.viewmodel.QuoteAction
import com.quotes.quotesapp.presentation.viewmodel.QuoteEvent
import com.quotes.quotesapp.presentation.viewmodel.QuoteState
import dagger.hilt.android.AndroidEntryPoint

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem("home", Icons.Filled.Home, "Home")
    data object Favorites : BottomNavItem("favorites", Icons.Filled.Favorite, "Favorites")
    data object Settings : BottomNavItem("settings", Icons.Filled.Settings, "Settings")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val viewModel = hiltViewModel<MainViewModel>()

            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val event by viewModel.events.collectAsStateWithLifecycle(initialValue = null)
            val navController = rememberNavController()

            val darkMode by settingsViewModel.isDarkMode.collectAsStateWithLifecycle()

            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    LightPrimary.toArgb(), DarkPrimary.toArgb()
                ) {
                    darkMode
                },
                    navigationBarStyle = SystemBarStyle.auto(
                        LightPrimary.toArgb(), DarkPrimary.toArgb()
                    ) {
                        darkMode
                    },

                )

            LaunchedEffect(key1 = Unit) {
                viewModel.fetchQuotes()
            }

            LaunchedEffect(event) {
                when (val currentEvent = event) {
                    is QuoteEvent.Share -> {
                        val shareText = "${currentEvent.quoteText} - ${currentEvent.quoteAuthor}"
                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            startActivity(Intent.createChooser(this, "Share Quote"))
                        }
                        viewModel.onEventHandled()
                    }
                    null -> {

                    }
                }
            }

            QuotesAppTheme(darkTheme = darkMode) { // Pass darkMode state here
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AppBottomNavigation(navController = navController)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItem.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(BottomNavItem.Home.route) {
                            HomeScreen(
                                state = state,
                                viewModel = viewModel
                            )
                        }
                        composable(BottomNavItem.Favorites.route) {
                            FavoritesScreen(
                                favoritesList = state.quotes.take(10),
                                onDelete = { viewModel.onAction(QuoteAction.Delete(it)) },
                                onShare = { viewModel.onAction(QuoteAction.Share(it)) }
                            )
                        }
                        composable(BottomNavItem.Settings.route) {
                            SettingsScreen(
                                onThemeChange = settingsViewModel::setDarkMode,
                                isDarkTheme = darkMode
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }
}

@Composable
fun AppBottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Settings
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun HomeScreen(
    state: QuoteState, // Assuming QuoteState is the correct type
    viewModel: MainViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (state.isLoading) {
            CenteredCircularProgressIndicator(
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazySwipeCards(
                cardShape = RoundedCornerShape(16.dp),
                cardColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Adjust weight as needed
            ) {
                onSwiped { item, direction ->
                    when (direction) {
                        SwipeDirection.LEFT -> viewModel.onAction(
                            QuoteAction.SwipeLeft(
                                item as QuoteUiModel
                            )
                        )

                        SwipeDirection.RIGHT -> viewModel.onAction(
                            QuoteAction.SwipeRight(
                                item as QuoteUiModel
                            )
                        )
                    }
                }
                items(
                    state.quotes.toMutableList()
                ) {
                    QuoteCard(
                        quoteString = it.quote,
                        quoteAuthor = it.author,
                        onLike = { viewModel.onAction(QuoteAction.Like(it)) },
                        onShare = { viewModel.onAction(QuoteAction.Share(it)) }
                    )
                }
            }
        }
    }
}


@Composable
fun QuoteCard(
    quoteString: String,
    quoteAuthor: String,
    onLike: () -> Unit,
    onShare: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF849A3B),
                        Color(0xFFB06436)
                    )
                )
            )
    ) {
        val (text, author, heartButton, shareButton) = createRefs()

        var isLiked by remember(key1 = quoteString, key2 = quoteAuthor) {
            mutableStateOf(false)
        }

        Text(
            text = quoteString,
            modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top)
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            },
            fontSize = 19.sp
        )

        Text(
            text = quoteAuthor,
            modifier = Modifier.constrainAs(author) {
                top.linkTo(text.bottom, 16.dp)
                end.linkTo(parent.end, 16.dp)
            },
            fontSize = 20.sp
        )

        IconButton(
            onClick = {
                isLiked = !isLiked
                onLike()
            },
            modifier = Modifier.constrainAs(heartButton) {
                start.linkTo(parent.start, 8.dp)
                bottom.linkTo(parent.bottom, 8.dp)
            }
        ) {
            Icon(
                imageVector = if (!isLiked) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                contentDescription = "Heart button"
            )
        }

        IconButton(
            onClick = onShare,
            modifier = Modifier.constrainAs(shareButton) {
                end.linkTo(parent.end, 8.dp)
                bottom.linkTo(parent.bottom, 8.dp)
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Share button"
            )
        }
    }
}


@Composable
private fun CenteredCircularProgressIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
