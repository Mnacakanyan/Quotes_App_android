package com.quotes.quotesapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.quotes.quotesapp.presentation.bottom_navigation.AppBottomNavigation
import com.quotes.quotesapp.presentation.bottom_navigation.BottomNavItem
import com.quotes.quotesapp.presentation.favorites.FavoritesScreen
import com.quotes.quotesapp.presentation.home.HomeScreen
import com.quotes.quotesapp.presentation.settings.SettingsScreen
import com.quotes.quotesapp.presentation.settings.SettingsViewModel
import com.quotes.quotesapp.presentation.ui.theme.DarkPrimary
import com.quotes.quotesapp.presentation.ui.theme.LightPrimary
import com.quotes.quotesapp.presentation.ui.theme.QuotesAppTheme
import com.quotes.quotesapp.presentation.viewmodel.MainViewModel
import com.quotes.quotesapp.presentation.viewmodel.QuoteAction
import com.quotes.quotesapp.presentation.viewmodel.QuoteEvent
import dagger.hilt.android.AndroidEntryPoint


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
}