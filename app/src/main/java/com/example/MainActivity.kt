package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.WidgetPresets
import com.example.data.WidgetRepository
import com.example.model.WidgetCategory
import com.example.model.WidgetConfig
import com.example.model.WidgetPreset
import com.example.ui.components.HowToPinDialog
import com.example.ui.screens.CustomWidgetCreatorScreen
import com.example.ui.screens.HomescreenSimulatorScreen
import com.example.ui.screens.MyWidgetsScreen
import com.example.ui.screens.WidgetCatalogScreen
import com.example.ui.screens.WidgetStudioScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.widget.WidgetPinHelper

enum class NavDestination(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    CATALOG("Explore", Icons.Default.Widgets),
    CREATE("Create", Icons.Default.AddCircle),
    SIMULATOR("Wallpaper", Icons.Default.Wallpaper),
    MY_WIDGETS("Saved", Icons.Default.Bookmark)
}

class MainActivity : ComponentActivity() {

    private lateinit var repository: WidgetRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        repository = WidgetRepository(this)

        setContent {
            MyApplicationTheme {
                MainAppContent(repository = repository)
            }
        }
    }

    @Composable
    private fun MainAppContent(repository: WidgetRepository) {
        var currentTab by remember { mutableStateOf(NavDestination.CATALOG) }
        var activeStudioConfig by remember { mutableStateOf<WidgetConfig?>(null) }
        var simulatorConfig by remember { mutableStateOf<WidgetConfig?>(null) }
        var showPinDialogFor by remember { mutableStateOf<WidgetConfig?>(null) }

        val customWidgets by repository.customWidgets.collectAsState()
        val favorites by repository.favorites.collectAsState()

        val resolveProviderClass: (WidgetConfig) -> String = { conf ->
            when {
                conf.isUserCreated ||
                conf.category == WidgetCategory.WEBPAGE ||
                conf.category == WidgetCategory.TRACKER ||
                conf.category == WidgetCategory.CHECKLIST ||
                conf.category == WidgetCategory.FINANCE -> "com.example.widget.PixelCustomAppWidgetProvider"
                conf.presetId in listOf("weather_pill_glance", "weather_card_detailed") -> "com.example.widget.PixelWeatherAppWidgetProvider"
                conf.presetId in listOf("battery_ring_status", "battery_dual_capsule", "system_quick_glance") -> "com.example.widget.PixelBatteryAppWidgetProvider"
                conf.presetId in listOf("music_pill_player", "music_compact_capsule") -> "com.example.widget.PixelMusicAppWidgetProvider"
                conf.presetId == "quote_squircle_card" -> "com.example.widget.PixelQuoteAppWidgetProvider"
                else -> "com.example.widget.PixelClockAppWidgetProvider"
            }
        }

        val onPinRequested: (WidgetConfig, String?) -> Unit = { config, providerClass ->
            val targetClass = providerClass ?: resolveProviderClass(config)
            val pinned = WidgetPinHelper.pinWidget(this@MainActivity, targetClass)
            if (pinned) {
                Toast.makeText(this@MainActivity, "Adding '${config.title}' to home screen...", Toast.LENGTH_SHORT).show()
            } else {
                showPinDialogFor = config
            }
        }

        if (activeStudioConfig != null) {
            WidgetStudioScreen(
                initialConfig = activeStudioConfig!!,
                onBack = { activeStudioConfig = null },
                onSaveConfig = { saved ->
                    repository.saveCustomWidget(saved)
                    Toast.makeText(this@MainActivity, "Widget saved to My Presets!", Toast.LENGTH_SHORT).show()
                    activeStudioConfig = null
                },
                onPinToHome = { conf ->
                    onPinRequested(conf, null)
                },
                onOpenWallpaperSimulator = { conf ->
                    simulatorConfig = conf
                    activeStudioConfig = null
                    currentTab = NavDestination.SIMULATOR
                },
                onFetchDeviceBattery = { repository.getDeviceBatteryInfo() }
            )
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .testTag("bottom_nav_bar")
                    ) {
                        NavDestination.values().forEach { dest ->
                            NavigationBarItem(
                                selected = currentTab == dest,
                                onClick = { currentTab = dest },
                                icon = { Icon(dest.icon, contentDescription = dest.label) },
                                label = { Text(dest.label) },
                                modifier = Modifier.testTag("nav_item_${dest.name}")
                            )
                        }
                    }
                }
            ) { innerPadding ->
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_transition",
                    modifier = Modifier.padding(innerPadding)
                ) { targetDestination ->
                    when (targetDestination) {
                        NavDestination.CATALOG -> {
                            WidgetCatalogScreen(
                                onSelectPreset = { preset ->
                                    activeStudioConfig = preset.defaultConfig
                                },
                                onPinWidget = { preset ->
                                    onPinRequested(preset.defaultConfig, preset.providerClass)
                                },
                                onOpenWallpaperPreview = { conf ->
                                    simulatorConfig = conf
                                    currentTab = NavDestination.SIMULATOR
                                },
                                favorites = favorites,
                                onToggleFavorite = { repository.toggleFavorite(it) },
                                onOpenCustomCreator = { currentTab = NavDestination.CREATE }
                            )
                        }

                        NavDestination.CREATE -> {
                            CustomWidgetCreatorScreen(
                                repository = repository,
                                onNavigateBack = { currentTab = NavDestination.CATALOG }
                            )
                        }

                        NavDestination.SIMULATOR -> {
                            val conf = simulatorConfig ?: WidgetPresets.allPresets.first().defaultConfig
                            HomescreenSimulatorScreen(
                                initialConfig = conf,
                                onBack = { currentTab = NavDestination.CATALOG },
                                onOpenStudio = { editConf ->
                                    activeStudioConfig = editConf
                                },
                                onPinWidget = { pinConf ->
                                    onPinRequested(pinConf, null)
                                }
                            )
                        }

                        NavDestination.MY_WIDGETS -> {
                            MyWidgetsScreen(
                                customWidgets = customWidgets,
                                favoriteIds = favorites,
                                onSelectConfig = { conf ->
                                    activeStudioConfig = conf
                                },
                                onPinConfig = { conf ->
                                    onPinRequested(conf, null)
                                },
                                onDeleteConfig = { widgetId ->
                                    repository.deleteCustomWidget(widgetId)
                                    Toast.makeText(this@MainActivity, "Widget preset deleted", Toast.LENGTH_SHORT).show()
                                },
                                onNavigateToCatalog = { currentTab = NavDestination.CATALOG },
                                onCreateCustomWidget = { currentTab = NavDestination.CREATE }
                            )
                        }
                    }
                }
            }
        }

        showPinDialogFor?.let { conf ->
            HowToPinDialog(
                widgetTitle = conf.title,
                onDismiss = { showPinDialogFor = null },
                onPinDirectly = {
                    val targetClass = resolveProviderClass(conf)
                    WidgetPinHelper.pinWidget(this@MainActivity, targetClass)
                }
            )
        }
    }
}
