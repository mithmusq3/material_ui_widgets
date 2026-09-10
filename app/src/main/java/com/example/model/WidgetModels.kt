package com.example.model

import androidx.compose.ui.graphics.Color

enum class WidgetCategory(val label: String, val iconName: String) {
    ALL("All", "widgets"),
    WEBPAGE("Web & Links", "language"),
    TRACKER("Habits & Counters", "add_circle"),
    CHECKLIST("Todos & Notes", "checklist"),
    FINANCE("Crypto & Stocks", "trending_up"),
    CLOCKS("Clocks", "schedule"),
    WEATHER("Weather", "wb_sunny"),
    BATTERY("Battery & System", "battery_charging_full"),
    MUSIC("Media Player", "music_note"),
    QUOTES("Quotes", "format_quote")
}

enum class CustomWidgetType(val displayName: String, val description: String) {
    WEBPAGE_BOOKMARK("Webpage Snapshot", "Pin any website, feed, or dashboard with instant web launch"),
    COUNTER_TRACKER("Habit & Goal Counter", "Interactive tally counter for water, habits, or fitness goals"),
    QUICK_CHECKLIST("Interactive Checklist", "Keep active tasks and daily priorities right on home screen"),
    FINANCIAL_TICKER("Crypto & Stock Ticker", "Track Bitcoin, Ethereum, or favorite market symbols"),
    EVENT_COUNTDOWN("Event Countdown", "Track days remaining till your milestone or celebration"),
    PIXEL_CLOCK("Custom Clock", "Pixel scalloped or jumbo digital numeral clock"),
    PIXEL_WEATHER("Custom Weather", "Pixel at-a-glance weather pill")
}

enum class WidgetShape(val displayName: String) {
    SCALLOP_FLOWER("Pixel Scallop"),
    SQUIRCLE("Pixel Squircle"),
    PILL("Material Pill"),
    ROUNDED_RECT("Rounded"),
    DUAL_PILL("Dual Capsule")
}

enum class WidgetFont(val displayName: String) {
    PIXEL_SANS("Pixel Sans"),
    EDITORIAL_SERIF("Editorial"),
    MONO_TECH("Mono Tech"),
    ROUNDED_CRAYON("Soft Rounded")
}

enum class PaletteOption(
    val displayName: String,
    val primaryColor: Long,
    val containerColor: Long,
    val onContainerColor: Long,
    val accentColor: Long,
    val surfaceColor: Long,
    val onSurfaceColor: Long
) {
    SYSTEM_MONET("Pixel Dynamic", 0xFF6750A4, 0xFFE8DEF8, 0xFF1D192B, 0xFF7D5260, 0xFFFDF7FF, 0xFF1D1B20),
    MEADOW_SAGE("Botanical Sage", 0xFF386A20, 0xFFD2E8C4, 0xFF0B2002, 0xFF42664A, 0xFFF7FAF2, 0xFF191D17),
    SUNSET_CORAL("Desert Coral", 0xFFA03F2C, 0xFFFFDAD3, 0xFF3B0903, 0xFF775651, 0xFFFFF8F6, 0xFF201A19),
    OCEAN_COBALT("Deep Cobalt", 0xFF00639B, 0xFFCDE5FF, 0xFF001D32, 0xFF4D616C, 0xFFF7F9FF, 0xFF181C20),
    LAVENDER_MIST("Lavender Lilac", 0xFF6D4DA1, 0xFFEBDCFF, 0xFF270057, 0xFF645A70, 0xFFFDF7FF, 0xFF1D1A20),
    AMBER_HONEY("Amber Golden", 0xFF835400, 0xFFFFDDB3, 0xFF2A1700, 0xFF6F5B40, 0xFFFFF8F4, 0xFF211A13),
    OBSIDIAN_DARK("Pixel Noir", 0xFFE3E2E6, 0xFF2E3133, 0xFFF1F0F4, 0xFF8DCDFF, 0xFF1A1C1E, 0xFFE3E2E6);

    val id: String get() = name

    val composePrimary get() = Color(primaryColor)
    val composeContainer get() = Color(containerColor)
    val composeOnContainer get() = Color(onContainerColor)
    val composeAccent get() = Color(accentColor)
    val composeSurface get() = Color(surfaceColor)
    val composeOnSurface get() = Color(onSurfaceColor)
}

data class WidgetConfig(
    val id: String = "default_clock",
    val presetId: String = "clock_scallop",
    val title: String = "Pixel Scallop Clock",
    val category: WidgetCategory = WidgetCategory.CLOCKS,
    val customType: CustomWidgetType = CustomWidgetType.PIXEL_CLOCK,
    val palette: PaletteOption = PaletteOption.SYSTEM_MONET,
    val shape: WidgetShape = WidgetShape.SCALLOP_FLOWER,
    val font: WidgetFont = WidgetFont.PIXEL_SANS,
    val cornerRadiusDp: Int = 32,
    val backgroundOpacity: Float = 0.95f,

    // Webpage widget specific
    val webUrl: String = "https://news.ycombinator.com",
    val webTitle: String = "Hacker News",
    val webSubtitle: String = "Top tech discussions & headlines",

    // Counter / Tracker widget specific
    val counterName: String = "Water Glasses",
    val counterCurrent: Int = 5,
    val counterTarget: Int = 8,
    val counterStep: Int = 1,

    // Checklist widget specific
    val checklistTitle: String = "Daily Priorities",
    val checklistItems: List<String> = listOf("Review Material You designs", "Workout 30 mins", "Team standup at 10 AM"),
    val checklistChecked: List<Boolean> = listOf(true, false, false),

    // Financial Ticker widget specific
    val tickerSymbol: String = "BTC / USD",
    val tickerPrice: String = "$64,320",
    val tickerChange: String = "+4.8%",
    val tickerIsPositive: Boolean = true,

    // Countdown widget specific
    val countdownEvent: String = "Google I/O 2026",
    val countdownDays: Int = 14,

    // Clock specific
    val is24Hour: Boolean = false,
    val showSeconds: Boolean = true,
    val showDate: Boolean = true,

    // Weather specific
    val tempUnit: String = "°C",
    val customCity: String = "Mountain View",
    val weatherCondition: String = "Partly Cloudy",
    val currentTemp: Int = 22,

    // Battery specific
    val batteryPercent: Int = 84,
    val isCharging: Boolean = true,
    val showBatteryPercentText: Boolean = true,

    // Music specific
    val songTitle: String = "Midnight City",
    val artistName: String = "M83 • Hurry Up, We're Dreaming",
    val isPlaying: Boolean = true,
    val playbackProgress: Float = 0.65f,

    // Quote & notes specific
    val quoteText: String = "Simplicity is the soul of modern design.",
    val quoteAuthor: String = "Pixel Studio",
    val noteSnippet: String = "Meeting at 2:30 PM • Android Design Review",

    // Status
    val isFavorite: Boolean = false,
    val isUserCreated: Boolean = false,
    val dateModified: Long = System.currentTimeMillis()
)

data class WidgetPreset(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: WidgetCategory,
    val sizeGrid: String, // "2x2", "4x1", "4x2", "2x1", "3x2"
    val description: String,
    val providerClass: String,
    val defaultConfig: WidgetConfig
)

data class PixelWallpaper(
    val id: String,
    val name: String,
    val themeSubtitle: String,
    val gradientColors: List<Color>,
    val paletteTag: PaletteOption,
    val isDark: Boolean = false
)
