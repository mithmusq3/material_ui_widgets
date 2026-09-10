package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.model.PaletteOption
import com.example.model.PixelWallpaper
import com.example.model.WidgetCategory
import com.example.model.WidgetConfig
import com.example.model.WidgetFont
import com.example.model.WidgetPreset
import com.example.model.WidgetShape

object WidgetPresets {

    val allPresets: List<WidgetPreset> = listOf(
        WidgetPreset(
            id = "clock_scallop",
            title = "Pixel Flower Clock",
            subtitle = "Iconic Android 12-16 Scalloped 4-Petal Clock",
            category = WidgetCategory.CLOCKS,
            sizeGrid = "2x2",
            description = "The signature Pixel scalloped flower analog clock with smooth dynamic Material You tones and fluid hands.",
            providerClass = "com.example.widget.PixelClockAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "clock_scallop",
                presetId = "clock_scallop",
                title = "Pixel Flower Clock",
                category = WidgetCategory.CLOCKS,
                palette = PaletteOption.SYSTEM_MONET,
                shape = WidgetShape.SCALLOP_FLOWER,
                font = WidgetFont.PIXEL_SANS,
                cornerRadiusDp = 36,
                showSeconds = true,
                showDate = true
            )
        ),
        WidgetPreset(
            id = "clock_pill_dual",
            title = "Pixel Dual Capsule",
            subtitle = "Digital Pill Clock & Date Tag",
            category = WidgetCategory.CLOCKS,
            sizeGrid = "3x2",
            description = "Modern split capsule widget featuring bold digital typography with a paired weather and date pill.",
            providerClass = "com.example.widget.PixelClockAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "clock_pill_dual",
                presetId = "clock_pill_dual",
                title = "Pixel Dual Capsule",
                category = WidgetCategory.CLOCKS,
                palette = PaletteOption.MEADOW_SAGE,
                shape = WidgetShape.PILL,
                font = WidgetFont.PIXEL_SANS,
                cornerRadiusDp = 32,
                is24Hour = false
            )
        ),
        WidgetPreset(
            id = "clock_numeral_bold",
            title = "Jumbo Stack Clock",
            subtitle = "Pixel Lockscreen Style Digital Hours",
            category = WidgetCategory.CLOCKS,
            sizeGrid = "2x2",
            description = "Stacked 2-line oversized numeral clock in a soft squircle with subtle dynamic background tint.",
            providerClass = "com.example.widget.PixelClockAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "clock_numeral_bold",
                presetId = "clock_numeral_bold",
                title = "Jumbo Stack Clock",
                category = WidgetCategory.CLOCKS,
                palette = PaletteOption.SUNSET_CORAL,
                shape = WidgetShape.SQUIRCLE,
                font = WidgetFont.MONO_TECH,
                cornerRadiusDp = 28
            )
        ),
        WidgetPreset(
            id = "weather_pill_glance",
            title = "At-a-Glance Weather",
            subtitle = "Horizontal Pill with Forecast & City",
            category = WidgetCategory.WEATHER,
            sizeGrid = "4x1",
            description = "Sleek Pixel horizontal capsule displaying current condition icon, temperature, city, and date.",
            providerClass = "com.example.widget.PixelWeatherAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "weather_pill_glance",
                presetId = "weather_pill_glance",
                title = "At-a-Glance Weather",
                category = WidgetCategory.WEATHER,
                palette = PaletteOption.OCEAN_COBALT,
                shape = WidgetShape.PILL,
                font = WidgetFont.PIXEL_SANS,
                cornerRadiusDp = 36,
                currentTemp = 24,
                tempUnit = "°C",
                weatherCondition = "Sunny",
                customCity = "San Francisco"
            )
        ),
        WidgetPreset(
            id = "weather_card_detailed",
            title = "Pixel Forecast Squircle",
            subtitle = "Multi-Hour Forecast & High/Low",
            category = WidgetCategory.WEATHER,
            sizeGrid = "4x2",
            description = "Rich weather squircle container showing 3-step forecast, humidity gauge, and condition backdrop.",
            providerClass = "com.example.widget.PixelWeatherAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "weather_card_detailed",
                presetId = "weather_card_detailed",
                title = "Pixel Forecast Squircle",
                category = WidgetCategory.WEATHER,
                palette = PaletteOption.SYSTEM_MONET,
                shape = WidgetShape.SQUIRCLE,
                font = WidgetFont.PIXEL_SANS,
                cornerRadiusDp = 28,
                currentTemp = 21,
                tempUnit = "°C",
                weatherCondition = "Partly Cloudy",
                customCity = "New York"
            )
        ),
        WidgetPreset(
            id = "battery_ring_status",
            title = "Pixel Battery Gauge",
            subtitle = "Circular Progress Ring & Health",
            category = WidgetCategory.BATTERY,
            sizeGrid = "2x2",
            description = "Signature Pixel circular battery level gauge with center bolt icon, real device percentage, and charging indicator.",
            providerClass = "com.example.widget.PixelBatteryAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "battery_ring_status",
                presetId = "battery_ring_status",
                title = "Pixel Battery Gauge",
                category = WidgetCategory.BATTERY,
                palette = PaletteOption.MEADOW_SAGE,
                shape = WidgetShape.SQUIRCLE,
                font = WidgetFont.PIXEL_SANS,
                cornerRadiusDp = 30,
                batteryPercent = 88,
                isCharging = true
            )
        ),
        WidgetPreset(
            id = "battery_dual_capsule",
            title = "Dual Device Battery Bar",
            subtitle = "Phone & Pixel Buds Status",
            category = WidgetCategory.BATTERY,
            sizeGrid = "4x1",
            description = "Two-capsule connected accessory bar showing phone battery alongside connected Bluetooth devices.",
            providerClass = "com.example.widget.PixelBatteryAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "battery_dual_capsule",
                presetId = "battery_dual_capsule",
                title = "Dual Device Battery Bar",
                category = WidgetCategory.BATTERY,
                palette = PaletteOption.OCEAN_COBALT,
                shape = WidgetShape.PILL,
                font = WidgetFont.PIXEL_SANS,
                cornerRadiusDp = 32,
                batteryPercent = 78
            )
        ),
        WidgetPreset(
            id = "music_pill_player",
            title = "Pixel Turntable Player",
            subtitle = "Rotating Vinyl Disc & Media Controls",
            category = WidgetCategory.MUSIC,
            sizeGrid = "4x2",
            description = "Material You media controller with rotating album disc, track scrubbing bar, play/pause, and track skips.",
            providerClass = "com.example.widget.PixelMusicAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "music_pill_player",
                presetId = "music_pill_player",
                title = "Pixel Turntable Player",
                category = WidgetCategory.MUSIC,
                palette = PaletteOption.LAVENDER_MIST,
                shape = WidgetShape.SQUIRCLE,
                font = WidgetFont.PIXEL_SANS,
                cornerRadiusDp = 28,
                songTitle = "Starboy",
                artistName = "The Weeknd • Daft Punk",
                isPlaying = true,
                playbackProgress = 0.42f
            )
        ),
        WidgetPreset(
            id = "music_compact_capsule",
            title = "Compact Audio Pill",
            subtitle = "Single Row Pill Player",
            category = WidgetCategory.MUSIC,
            sizeGrid = "4x1",
            description = "Minimalist media pill that seamlessly rests between home screen app rows.",
            providerClass = "com.example.widget.PixelMusicAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "music_compact_capsule",
                presetId = "music_compact_capsule",
                title = "Compact Audio Pill",
                category = WidgetCategory.MUSIC,
                palette = PaletteOption.AMBER_HONEY,
                shape = WidgetShape.PILL,
                font = WidgetFont.PIXEL_SANS,
                cornerRadiusDp = 36,
                songTitle = "Golden Hour",
                artistName = "JVKE",
                isPlaying = true
            )
        ),
        WidgetPreset(
            id = "quote_squircle_card",
            title = "Pixel Daily Inspiration",
            subtitle = "Editorial Typography Quote Card",
            category = WidgetCategory.QUOTES,
            sizeGrid = "3x2",
            description = "Inspiring daily aphorisms and customizable affirmations framed in a warm Pixel Material You squircle container.",
            providerClass = "com.example.widget.PixelQuoteAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "quote_squircle_card",
                presetId = "quote_squircle_card",
                title = "Pixel Daily Inspiration",
                category = WidgetCategory.QUOTES,
                palette = PaletteOption.AMBER_HONEY,
                shape = WidgetShape.SQUIRCLE,
                font = WidgetFont.EDITORIAL_SERIF,
                cornerRadiusDp = 28,
                quoteText = "Design is not just what it looks like. Design is how it works.",
                quoteAuthor = "Steve Jobs"
            )
        ),
        WidgetPreset(
            id = "system_quick_glance",
            title = "Pixel System Control Pod",
            subtitle = "Wi-Fi, Bluetooth & Storage Pills",
            category = WidgetCategory.BATTERY,
            sizeGrid = "2x2",
            description = "Four tactile quick-glance status chips in a unified 2x2 grid representing device connectivity and storage.",
            providerClass = "com.example.widget.PixelBatteryAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "system_quick_glance",
                presetId = "system_quick_glance",
                title = "Pixel System Control Pod",
                category = WidgetCategory.BATTERY,
                palette = PaletteOption.OBSIDIAN_DARK,
                shape = WidgetShape.SQUIRCLE,
                font = WidgetFont.PIXEL_SANS,
                cornerRadiusDp = 24
            )
        ),
        WidgetPreset(
            id = "webpage_hackernews",
            title = "Hacker News Feed",
            subtitle = "Live Webpage Snapshot & 1-Tap Launch",
            category = WidgetCategory.WEBPAGE,
            sizeGrid = "3x2",
            description = "Live snapshot of your favorite webpage, blog, or dashboard with instant web browser launcher.",
            providerClass = "com.example.widget.PixelCustomAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "webpage_hackernews",
                presetId = "webpage_hackernews",
                title = "Hacker News Feed",
                category = WidgetCategory.WEBPAGE,
                customType = com.example.model.CustomWidgetType.WEBPAGE_BOOKMARK,
                palette = PaletteOption.AMBER_HONEY,
                shape = WidgetShape.SQUIRCLE,
                font = WidgetFont.PIXEL_SANS,
                cornerRadiusDp = 28,
                webUrl = "https://news.ycombinator.com",
                webTitle = "Hacker News",
                webSubtitle = "Top tech stories & startup discussions"
            )
        ),
        WidgetPreset(
            id = "webpage_github",
            title = "GitHub Trending",
            subtitle = "Developer Pulse Web Snapshot",
            category = WidgetCategory.WEBPAGE,
            sizeGrid = "3x2",
            description = "Keep your favorite repo, documentation, or developer feed front-and-center on your home screen.",
            providerClass = "com.example.widget.PixelCustomAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "webpage_github",
                presetId = "webpage_github",
                title = "GitHub Trending",
                category = WidgetCategory.WEBPAGE,
                customType = com.example.model.CustomWidgetType.WEBPAGE_BOOKMARK,
                palette = PaletteOption.OCEAN_COBALT,
                shape = WidgetShape.ROUNDED_RECT,
                font = WidgetFont.MONO_TECH,
                cornerRadiusDp = 28,
                webUrl = "https://github.com/trending",
                webTitle = "GitHub Trending",
                webSubtitle = "Explore today's rising open-source repos"
            )
        ),
        WidgetPreset(
            id = "tracker_water",
            title = "Hydration Habit Counter",
            subtitle = "Daily Water Glasses Tracker",
            category = WidgetCategory.TRACKER,
            sizeGrid = "2x2",
            description = "Interactive tally counter for water intake, workouts, or daily habits with tap increments.",
            providerClass = "com.example.widget.PixelCustomAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "tracker_water",
                presetId = "tracker_water",
                title = "Hydration Habit Counter",
                category = WidgetCategory.TRACKER,
                customType = com.example.model.CustomWidgetType.COUNTER_TRACKER,
                palette = PaletteOption.OCEAN_COBALT,
                shape = WidgetShape.SQUIRCLE,
                font = WidgetFont.PIXEL_SANS,
                cornerRadiusDp = 30,
                counterName = "Glasses of Water",
                counterCurrent = 5,
                counterTarget = 8,
                counterStep = 1
            )
        ),
        WidgetPreset(
            id = "checklist_priorities",
            title = "Daily Focus Checklist",
            subtitle = "Home Screen Quick Tasks",
            category = WidgetCategory.CHECKLIST,
            sizeGrid = "3x2",
            description = "Keep your active priorities in view. Tap items directly to toggle completion.",
            providerClass = "com.example.widget.PixelCustomAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "checklist_priorities",
                presetId = "checklist_priorities",
                title = "Daily Focus Checklist",
                category = WidgetCategory.CHECKLIST,
                customType = com.example.model.CustomWidgetType.QUICK_CHECKLIST,
                palette = PaletteOption.MEADOW_SAGE,
                shape = WidgetShape.ROUNDED_RECT,
                font = WidgetFont.PIXEL_SANS,
                cornerRadiusDp = 26,
                checklistTitle = "Today's Priorities",
                checklistItems = listOf("Review Material You designs", "Daily 30 min cardio session", "Sync with team on launch"),
                checklistChecked = listOf(true, false, false)
            )
        ),
        WidgetPreset(
            id = "finance_crypto",
            title = "Bitcoin Market Ticker",
            subtitle = "Live Crypto & Stock Glance",
            category = WidgetCategory.FINANCE,
            sizeGrid = "2x2",
            description = "Track Bitcoin, Ethereum, or tech stocks with live change pills and price indicators.",
            providerClass = "com.example.widget.PixelCustomAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "finance_crypto",
                presetId = "finance_crypto",
                title = "Bitcoin Market Ticker",
                category = WidgetCategory.FINANCE,
                customType = com.example.model.CustomWidgetType.FINANCIAL_TICKER,
                palette = PaletteOption.SYSTEM_MONET,
                shape = WidgetShape.SQUIRCLE,
                font = WidgetFont.PIXEL_SANS,
                cornerRadiusDp = 28,
                tickerSymbol = "BTC / USD",
                tickerPrice = "$64,320",
                tickerChange = "+4.8%",
                tickerIsPositive = true
            )
        ),
        WidgetPreset(
            id = "countdown_milestone",
            title = "Event Milestone Countdown",
            subtitle = "Days Remaining Tracker",
            category = WidgetCategory.TRACKER,
            sizeGrid = "2x2",
            description = "Countdown the days until your vacation, conference, birthday, or project launch.",
            providerClass = "com.example.widget.PixelCustomAppWidgetProvider",
            defaultConfig = WidgetConfig(
                id = "countdown_milestone",
                presetId = "countdown_milestone",
                title = "Event Milestone Countdown",
                category = WidgetCategory.TRACKER,
                customType = com.example.model.CustomWidgetType.EVENT_COUNTDOWN,
                palette = PaletteOption.SUNSET_CORAL,
                shape = WidgetShape.SQUIRCLE,
                font = WidgetFont.PIXEL_SANS,
                cornerRadiusDp = 28,
                countdownEvent = "Google I/O 2026",
                countdownDays = 14
            )
        )
    )

    val sampleWallpapers: List<PixelWallpaper> = listOf(
        PixelWallpaper(
            id = "botanical_mint",
            name = "Botanical Moss",
            themeSubtitle = "Pixel 7/8 Nature Series",
            gradientColors = listOf(Color(0xFF233B27), Color(0xFF436B4D), Color(0xFF8BAE92)),
            paletteTag = PaletteOption.MEADOW_SAGE,
            isDark = true
        ),
        PixelWallpaper(
            id = "desert_rose",
            name = "Desert Dune Coral",
            themeSubtitle = "Pixel Sunset Collection",
            gradientColors = listOf(Color(0xFF42211D), Color(0xFF874E45), Color(0xFFD99B8F)),
            paletteTag = PaletteOption.SUNSET_CORAL,
            isDark = true
        ),
        PixelWallpaper(
            id = "slate_ocean",
            name = "Pacific Horizon",
            themeSubtitle = "Pixel Minimal Water",
            gradientColors = listOf(Color(0xFF132438), Color(0xFF284B6E), Color(0xFF76A0C7)),
            paletteTag = PaletteOption.OCEAN_COBALT,
            isDark = true
        ),
        PixelWallpaper(
            id = "lavender_sky",
            name = "Twilight Mist",
            themeSubtitle = "Pixel Pastel Aurora",
            gradientColors = listOf(Color(0xFF2D1F3D), Color(0xFF5B4575), Color(0xFFA68EC2)),
            paletteTag = PaletteOption.LAVENDER_MIST,
            isDark = true
        ),
        PixelWallpaper(
            id = "amber_glow",
            name = "Golden Sunflower",
            themeSubtitle = "Pixel Botanical Amber",
            gradientColors = listOf(Color(0xFF382508), Color(0xFF6B4D1A), Color(0xFFC7A15D)),
            paletteTag = PaletteOption.AMBER_HONEY,
            isDark = true
        ),
        PixelWallpaper(
            id = "obsidian_carbon",
            name = "Obsidian Noir",
            themeSubtitle = "Pure Minimal Dark",
            gradientColors = listOf(Color(0xFF111315), Color(0xFF1E2124), Color(0xFF303438)),
            paletteTag = PaletteOption.OBSIDIAN_DARK,
            isDark = true
        )
    )
}
