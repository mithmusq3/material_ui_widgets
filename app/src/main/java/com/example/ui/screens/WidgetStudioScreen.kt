package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RoundedCorner
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CustomWidgetType
import com.example.model.PaletteOption
import com.example.model.WidgetCategory
import com.example.model.WidgetConfig
import com.example.model.WidgetFont
import com.example.model.WidgetShape
import com.example.ui.components.PixelWidgetCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetStudioScreen(
    initialConfig: WidgetConfig,
    onBack: () -> Unit,
    onSaveConfig: (WidgetConfig) -> Unit,
    onPinToHome: (WidgetConfig) -> Unit,
    onOpenWallpaperSimulator: (WidgetConfig) -> Unit,
    onFetchDeviceBattery: () -> Pair<Int, Boolean>,
    modifier: Modifier = Modifier
) {
    var config by remember { mutableStateOf(initialConfig) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Widget Studio",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = config.title,
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_studio_back")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onSaveConfig(config) },
                        modifier = Modifier.testTag("btn_studio_save")
                    ) {
                        Icon(Icons.Default.Bookmark, contentDescription = "Save Preset", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 6.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onOpenWallpaperSimulator(config) },
                        modifier = Modifier.weight(1f).testTag("btn_studio_test_wp"),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(Icons.Default.Wallpaper, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Wallpaper")
                    }

                    Button(
                        onClick = { onPinToHome(config) },
                        modifier = Modifier.weight(1.3f).testTag("btn_studio_pin_home"),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Add to Home")
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Live Widget Preview Container
            item {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("studio_preview_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Live Interactive Preview",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Text(
                                text = "${config.shape.displayName} • ${config.palette.displayName}",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val cardModifier = when (config.category) {
                                WidgetCategory.WEATHER -> Modifier.fillMaxWidth().height(90.dp)
                                WidgetCategory.MUSIC -> Modifier.fillMaxWidth().height(140.dp)
                                else -> Modifier.size(190.dp)
                            }
                            PixelWidgetCard(
                                config = config,
                                modifier = cardModifier
                            )
                        }
                    }
                }
            }

            // Section 1: Dynamic Material You Color Palettes
            item {
                StudioSection(
                    icon = Icons.Default.Palette,
                    title = "Material You Color Palette",
                    subtitle = "Dynamic tones inspired by Android 12-16 Monet color engine"
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(PaletteOption.values()) { palette ->
                            val isSelected = config.palette == palette
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = palette.composeContainer,
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(3.dp, MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier
                                    .size(width = 110.dp, height = 72.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable { config = config.copy(palette = palette) }
                                    .testTag("palette_chip_${palette.name}")
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .background(palette.composePrimary)
                                        )
                                        if (isSelected) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                tint = palette.composePrimary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = palette.displayName,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = palette.composeOnContainer
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section 2: Shape & Geometry
            item {
                StudioSection(
                    icon = Icons.Default.RoundedCorner,
                    title = "Shape & Curvature",
                    subtitle = "Pixel signature organic shapes and container radii"
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(WidgetShape.values()) { shape ->
                            FilterChip(
                                selected = config.shape == shape,
                                onClick = { config = config.copy(shape = shape) },
                                label = { Text(shape.displayName) },
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.testTag("shape_chip_${shape.name}")
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Corner Radius: ${config.cornerRadiusDp} dp",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                    )
                    Slider(
                        value = config.cornerRadiusDp.toFloat(),
                        onValueChange = { config = config.copy(cornerRadiusDp = it.toInt()) },
                        valueRange = 12f..48f,
                        steps = 5,
                        modifier = Modifier.testTag("slider_corner_radius")
                    )

                    Text(
                        text = "Container Opacity: ${(config.backgroundOpacity * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                    )
                    Slider(
                        value = config.backgroundOpacity,
                        onValueChange = { config = config.copy(backgroundOpacity = it) },
                        valueRange = 0.5f..1.0f,
                        modifier = Modifier.testTag("slider_opacity")
                    )
                }
            }

            // Section 3: Typography Font
            item {
                StudioSection(
                    icon = Icons.Default.TextFields,
                    title = "Typography Style",
                    subtitle = "Font pairing for numbers and labels"
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WidgetFont.values().forEach { font ->
                            FilterChip(
                                selected = config.font == font,
                                onClick = { config = config.copy(font = font) },
                                label = { Text(font.displayName) },
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.weight(1f).testTag("font_chip_${font.name}")
                            )
                        }
                    }
                }
            }

            // Section 4: Content & Interactive Controls
            item {
                StudioSection(
                    icon = Icons.Default.Tune,
                    title = "Widget Content Controls",
                    subtitle = "Customize numbers, toggles, and live data"
                ) {
                    when (config.category) {
                        WidgetCategory.CLOCKS -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("24-Hour Format")
                                Switch(
                                    checked = config.is24Hour,
                                    onCheckedChange = { config = config.copy(is24Hour = it) },
                                    modifier = Modifier.testTag("switch_24h")
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Show Seconds Dot")
                                Switch(
                                    checked = config.showSeconds,
                                    onCheckedChange = { config = config.copy(showSeconds = it) },
                                    modifier = Modifier.testTag("switch_seconds")
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Show Date Badge")
                                Switch(
                                    checked = config.showDate,
                                    onCheckedChange = { config = config.copy(showDate = it) },
                                    modifier = Modifier.testTag("switch_date")
                                )
                            }
                        }

                        WidgetCategory.WEATHER -> {
                            OutlinedTextField(
                                value = config.customCity,
                                onValueChange = { config = config.copy(customCity = it) },
                                label = { Text("Location / City") },
                                singleLine = true,
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Temperature Unit")
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    FilterChip(
                                        selected = config.tempUnit == "°C",
                                        onClick = { config = config.copy(tempUnit = "°C") },
                                        label = { Text("Celsius (°C)") }
                                    )
                                    FilterChip(
                                        selected = config.tempUnit == "°F",
                                        onClick = { config = config.copy(tempUnit = "°F") },
                                        label = { Text("Fahrenheit (°F)") }
                                    )
                                }
                            }
                        }

                        WidgetCategory.BATTERY -> {
                            Button(
                                onClick = {
                                    val (lvl, chg) = onFetchDeviceBattery()
                                    config = config.copy(batteryPercent = lvl, isCharging = chg)
                                },
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Sync Real Device Battery Level")
                            }

                            Spacer(Modifier.height(10.dp))
                            Text("Simulate Battery: ${config.batteryPercent}%")
                            Slider(
                                value = config.batteryPercent.toFloat(),
                                onValueChange = { config = config.copy(batteryPercent = it.toInt()) },
                                valueRange = 5f..100f
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Charging State")
                                Switch(
                                    checked = config.isCharging,
                                    onCheckedChange = { config = config.copy(isCharging = it) }
                                )
                            }
                        }

                        WidgetCategory.MUSIC -> {
                            OutlinedTextField(
                                value = config.songTitle,
                                onValueChange = { config = config.copy(songTitle = it) },
                                label = { Text("Song Title") },
                                singleLine = true,
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = config.artistName,
                                onValueChange = { config = config.copy(artistName = it) },
                                label = { Text("Artist & Album") },
                                singleLine = true,
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        WidgetCategory.QUOTES -> {
                            OutlinedTextField(
                                value = config.quoteText,
                                onValueChange = { config = config.copy(quoteText = it) },
                                label = { Text("Quote / Note Text") },
                                maxLines = 3,
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = config.quoteAuthor,
                                onValueChange = { config = config.copy(quoteAuthor = it) },
                                label = { Text("Author / Signature") },
                                singleLine = true,
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        WidgetCategory.WEBPAGE -> {
                            OutlinedTextField(
                                value = config.webUrl,
                                onValueChange = { config = config.copy(webUrl = it) },
                                label = { Text("Webpage URL") },
                                singleLine = true,
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = config.webTitle,
                                onValueChange = { config = config.copy(webTitle = it) },
                                label = { Text("Webpage Title") },
                                singleLine = true,
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = config.webSubtitle,
                                onValueChange = { config = config.copy(webSubtitle = it) },
                                label = { Text("Snippet / Tagline") },
                                singleLine = true,
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        WidgetCategory.TRACKER -> {
                            if (config.customType == CustomWidgetType.EVENT_COUNTDOWN) {
                                OutlinedTextField(
                                    value = config.countdownEvent,
                                    onValueChange = { config = config.copy(countdownEvent = it) },
                                    label = { Text("Event Name") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(18.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(Modifier.height(8.dp))
                                Text("Days Remaining: ${config.countdownDays}")
                                Slider(
                                    value = config.countdownDays.toFloat(),
                                    onValueChange = { config = config.copy(countdownDays = it.toInt()) },
                                    valueRange = 1f..100f
                                )
                            } else {
                                OutlinedTextField(
                                    value = config.counterName,
                                    onValueChange = { config = config.copy(counterName = it) },
                                    label = { Text("Counter / Goal Name") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(18.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(Modifier.height(8.dp))
                                Text("Current Count: ${config.counterCurrent} (Target: ${config.counterTarget})")
                                Slider(
                                    value = config.counterCurrent.toFloat(),
                                    onValueChange = { config = config.copy(counterCurrent = it.toInt()) },
                                    valueRange = 0f..maxOf(config.counterTarget.toFloat(), 20f)
                                )
                            }
                        }

                        WidgetCategory.CHECKLIST -> {
                            OutlinedTextField(
                                value = config.checklistTitle,
                                onValueChange = { config = config.copy(checklistTitle = it) },
                                label = { Text("Checklist Header") },
                                singleLine = true,
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            config.checklistItems.forEachIndexed { i, item ->
                                OutlinedTextField(
                                    value = item,
                                    onValueChange = { newTxt ->
                                        val mutable = config.checklistItems.toMutableList()
                                        if (i < mutable.size) {
                                            mutable[i] = newTxt
                                            config = config.copy(checklistItems = mutable)
                                        }
                                    },
                                    label = { Text("Item ${i + 1}") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(18.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 6.dp)
                                )
                            }
                        }

                        WidgetCategory.FINANCE -> {
                            OutlinedTextField(
                                value = config.tickerSymbol,
                                onValueChange = { config = config.copy(tickerSymbol = it) },
                                label = { Text("Symbol / Asset") },
                                singleLine = true,
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = config.tickerPrice,
                                onValueChange = { config = config.copy(tickerPrice = it) },
                                label = { Text("Current Price") },
                                singleLine = true,
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = config.tickerChange,
                                onValueChange = { config = config.copy(tickerChange = it) },
                                label = { Text("Change %") },
                                singleLine = true,
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        else -> {}
                    }
                }
            }

            item {
                Spacer(Modifier.height(60.dp))
            }
        }
    }
}

@Composable
fun StudioSection(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            content()
        }
    }
}
