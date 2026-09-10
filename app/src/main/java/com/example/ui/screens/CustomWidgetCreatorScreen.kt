package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WidgetRepository
import com.example.model.CustomWidgetType
import com.example.model.PaletteOption
import com.example.model.WidgetCategory
import com.example.model.WidgetConfig
import com.example.model.WidgetFont
import com.example.model.WidgetShape
import com.example.ui.components.HowToPinDialog
import com.example.ui.components.PixelWidgetCard
import com.example.widget.WidgetPinHelper
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomWidgetCreatorScreen(
    repository: WidgetRepository,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showPinDialog by remember { mutableStateOf(false) }

    // Type selection
    var selectedType by remember { mutableStateOf(CustomWidgetType.WEBPAGE_BOOKMARK) }

    // Visual Style State
    var selectedPalette by remember { mutableStateOf(PaletteOption.SYSTEM_MONET) }
    var selectedShape by remember { mutableStateOf(WidgetShape.SQUIRCLE) }
    var selectedFont by remember { mutableStateOf(WidgetFont.PIXEL_SANS) }
    var cornerRadius by remember { mutableFloatStateOf(28f) }

    // Webpage state
    var webUrl by remember { mutableStateOf("https://news.ycombinator.com") }
    var webTitle by remember { mutableStateOf("Hacker News") }
    var webSubtitle by remember { mutableStateOf("Top tech stories & startup discussions") }

    // Counter state
    var counterName by remember { mutableStateOf("Water Glasses") }
    var counterCurrent by remember { mutableIntStateOf(5) }
    var counterTarget by remember { mutableIntStateOf(8) }

    // Checklist state
    var checklistTitle by remember { mutableStateOf("Daily Focus Tasks") }
    var item1 by remember { mutableStateOf("Review Pixel Material You design") }
    var item2 by remember { mutableStateOf("Complete 30m workout") }
    var item3 by remember { mutableStateOf("Team sync at 10 AM") }

    // Financial Ticker state
    var tickerSymbol by remember { mutableStateOf("BTC / USD") }
    var tickerPrice by remember { mutableStateOf("$64,320") }
    var tickerChange by remember { mutableStateOf("+4.8%") }
    var tickerPositive by remember { mutableStateOf(true) }

    // Countdown state
    var countdownEvent by remember { mutableStateOf("Google I/O 2026") }
    var countdownDays by remember { mutableIntStateOf(14) }

    // Built Widget Config for Preview
    val previewConfig = remember(
        selectedType, selectedPalette, selectedShape, selectedFont, cornerRadius,
        webUrl, webTitle, webSubtitle,
        counterName, counterCurrent, counterTarget,
        checklistTitle, item1, item2, item3,
        tickerSymbol, tickerPrice, tickerChange, tickerPositive,
        countdownEvent, countdownDays
    ) {
        val category = when (selectedType) {
            CustomWidgetType.WEBPAGE_BOOKMARK -> WidgetCategory.WEBPAGE
            CustomWidgetType.COUNTER_TRACKER, CustomWidgetType.EVENT_COUNTDOWN -> WidgetCategory.TRACKER
            CustomWidgetType.QUICK_CHECKLIST -> WidgetCategory.CHECKLIST
            CustomWidgetType.FINANCIAL_TICKER -> WidgetCategory.FINANCE
            CustomWidgetType.PIXEL_CLOCK -> WidgetCategory.CLOCKS
            CustomWidgetType.PIXEL_WEATHER -> WidgetCategory.WEATHER
        }
        WidgetConfig(
            id = "custom_" + UUID.randomUUID().toString().take(8),
            presetId = "custom_user_widget",
            title = when (selectedType) {
                CustomWidgetType.WEBPAGE_BOOKMARK -> webTitle
                CustomWidgetType.COUNTER_TRACKER -> counterName
                CustomWidgetType.QUICK_CHECKLIST -> checklistTitle
                CustomWidgetType.FINANCIAL_TICKER -> tickerSymbol
                CustomWidgetType.EVENT_COUNTDOWN -> countdownEvent
                else -> "Custom Widget"
            },
            category = category,
            customType = selectedType,
            palette = selectedPalette,
            shape = selectedShape,
            font = selectedFont,
            cornerRadiusDp = cornerRadius.toInt(),
            webUrl = webUrl,
            webTitle = webTitle,
            webSubtitle = webSubtitle,
            counterName = counterName,
            counterCurrent = counterCurrent,
            counterTarget = counterTarget,
            checklistTitle = checklistTitle,
            checklistItems = listOf(item1, item2, item3).filter { it.isNotBlank() },
            checklistChecked = listOf(true, false, false),
            tickerSymbol = tickerSymbol,
            tickerPrice = tickerPrice,
            tickerChange = tickerChange,
            tickerIsPositive = tickerPositive,
            countdownEvent = countdownEvent,
            countdownDays = countdownDays,
            isUserCreated = true
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Widget Creator", fontWeight = FontWeight.Bold)
                        Text(
                            "Design your own custom widget",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            repository.saveCustomWidget(previewConfig)
                            Toast.makeText(context, "Saved to My Widgets!", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("save_custom_widget_btn")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Save Widget", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            repository.saveCustomWidget(previewConfig)
                            val pinned = WidgetPinHelper.pinWidget(
                                context = context,
                                providerClassName = "com.example.widget.PixelCustomAppWidgetProvider"
                            )
                            if (pinned) {
                                Toast.makeText(context, "Adding widget to home screen...", Toast.LENGTH_LONG).show()
                            } else {
                                showPinDialog = true
                            }
                        },
                        modifier = Modifier
                            .weight(1.2f)
                            .height(52.dp)
                            .testTag("pin_custom_widget_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.PushPin, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Pin to Home", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Live Interactive Widget Preview Canvas
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "LIVE PREVIEW",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = selectedType.displayName,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        PixelWidgetCard(
                            config = previewConfig,
                            modifier = Modifier
                                .fillMaxWidth(0.92f)
                                .height(160.dp)
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Changes reflect live as you edit below",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 2. Widget Type Selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "1. Choose Widget Concept",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomWidgetType.values().forEach { type ->
                        val isSelected = type == selectedType
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedType = type },
                            label = { Text(type.displayName) },
                            leadingIcon = {
                                val icon = when (type) {
                                    CustomWidgetType.WEBPAGE_BOOKMARK -> Icons.Default.Language
                                    CustomWidgetType.COUNTER_TRACKER -> Icons.Default.Add
                                    CustomWidgetType.QUICK_CHECKLIST -> Icons.Default.CheckCircle
                                    CustomWidgetType.FINANCIAL_TICKER -> Icons.Default.TrendingUp
                                    CustomWidgetType.EVENT_COUNTDOWN -> Icons.Default.Event
                                    CustomWidgetType.PIXEL_CLOCK -> Icons.Default.Schedule
                                    CustomWidgetType.PIXEL_WEATHER -> Icons.Default.Schedule
                                }
                                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
                Text(
                    selectedType.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // 3. Content Configuration Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "2. Configure Content",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    when (selectedType) {
                        CustomWidgetType.WEBPAGE_BOOKMARK -> {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    "Webpage URL",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                OutlinedTextField(
                                    value = webUrl,
                                    onValueChange = { webUrl = it },
                                    label = { Text("https://example.com") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    leadingIcon = {
                                        Icon(Icons.Default.Language, contentDescription = null)
                                    }
                                )

                                // Quick presets
                                Text(
                                    "Popular Sites:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    listOf(
                                        "Hacker News" to "https://news.ycombinator.com",
                                        "GitHub Pulse" to "https://github.com/trending",
                                        "Reddit Tech" to "https://reddit.com/r/technology",
                                        "Google News" to "https://news.google.com",
                                        "Product Hunt" to "https://producthunt.com"
                                    ).forEach { (name, url) ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            modifier = Modifier.clickable {
                                                webUrl = url
                                                webTitle = name
                                            }
                                        ) {
                                            Text(
                                                name,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                    }
                                }

                                OutlinedTextField(
                                    value = webTitle,
                                    onValueChange = { webTitle = it },
                                    label = { Text("Widget Title") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = webSubtitle,
                                    onValueChange = { webSubtitle = it },
                                    label = { Text("Short Description / Tagline") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                OutlinedButton(
                                    onClick = {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)).apply {
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            }
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Invalid URL: $webUrl", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text("Test Open URL in Browser")
                                }
                            }
                        }

                        CustomWidgetType.COUNTER_TRACKER -> {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = counterName,
                                    onValueChange = { counterName = it },
                                    label = { Text("Habit / Goal Name") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedTextField(
                                        value = counterCurrent.toString(),
                                        onValueChange = { counterCurrent = it.toIntOrNull() ?: counterCurrent },
                                        label = { Text("Current Value") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )

                                    OutlinedTextField(
                                        value = counterTarget.toString(),
                                        onValueChange = { counterTarget = it.toIntOrNull() ?: counterTarget },
                                        label = { Text("Target Goal") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                }

                                // Quick presets
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    listOf(
                                        "Water (8 Cups)" to (8 to "Glasses of Water"),
                                        "Daily Workout" to (1 to "Workout Completed"),
                                        "Pages Read (25)" to (25 to "Pages Read"),
                                        "Coffee Limit (2)" to (2 to "Cups of Coffee")
                                    ).forEach { (label, data) ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            modifier = Modifier.clickable {
                                                counterTarget = data.first
                                                counterName = data.second
                                            }
                                        ) {
                                            Text(
                                                label,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        CustomWidgetType.QUICK_CHECKLIST -> {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = checklistTitle,
                                    onValueChange = { checklistTitle = it },
                                    label = { Text("Checklist Title") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = item1,
                                    onValueChange = { item1 = it },
                                    label = { Text("Task 1") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = item2,
                                    onValueChange = { item2 = it },
                                    label = { Text("Task 2") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = item3,
                                    onValueChange = { item3 = it },
                                    label = { Text("Task 3") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                            }
                        }

                        CustomWidgetType.FINANCIAL_TICKER -> {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = tickerSymbol,
                                    onValueChange = { tickerSymbol = it },
                                    label = { Text("Symbol / Asset") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedTextField(
                                        value = tickerPrice,
                                        onValueChange = { tickerPrice = it },
                                        label = { Text("Current Price") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )

                                    OutlinedTextField(
                                        value = tickerChange,
                                        onValueChange = { tickerChange = it },
                                        label = { Text("24h Change %") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    FilterChip(
                                        selected = tickerPositive,
                                        onClick = { tickerPositive = true },
                                        label = { Text("Bullish Green (+)") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    FilterChip(
                                        selected = !tickerPositive,
                                        onClick = { tickerPositive = false },
                                        label = { Text("Bearish Red (-)") },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        CustomWidgetType.EVENT_COUNTDOWN -> {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = countdownEvent,
                                    onValueChange = { countdownEvent = it },
                                    label = { Text("Milestone / Event Name") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                Text(
                                    "Days Remaining: $countdownDays days",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Slider(
                                    value = countdownDays.toFloat(),
                                    onValueChange = { countdownDays = it.toInt() },
                                    valueRange = 1f..100f
                                )
                            }
                        }

                        else -> {
                            Text(
                                "Pixel standard widgets use system real-time clock and location sensors automatically.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // 4. Material You Styling Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "3. Material You Styling",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Palette selector
                    Text(
                        "Color Palette (${selectedPalette.displayName})",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PaletteOption.values().forEach { palette ->
                            val isSelected = palette == selectedPalette
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(palette.composeContainer)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) palette.composePrimary else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedPalette = palette },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = palette.composePrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Shape selector
                    Text(
                        "Shape Geometry",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WidgetShape.values().forEach { shape ->
                            FilterChip(
                                selected = shape == selectedShape,
                                onClick = { selectedShape = shape },
                                label = { Text(shape.displayName) }
                            )
                        }
                    }

                    // Font selector
                    Text(
                        "Typography",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WidgetFont.values().forEach { font ->
                            FilterChip(
                                selected = font == selectedFont,
                                onClick = { selectedFont = font },
                                label = { Text(font.displayName) }
                            )
                        }
                    }

                    // Corner radius
                    Text(
                        "Corner Rounding: ${cornerRadius.toInt()}dp",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Slider(
                        value = cornerRadius,
                        onValueChange = { cornerRadius = it },
                        valueRange = 12f..48f
                    )
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }

    if (showPinDialog) {
        HowToPinDialog(
            widgetTitle = previewConfig.title,
            onDismiss = { showPinDialog = false },
            onPinDirectly = {
                WidgetPinHelper.pinWidget(
                    context = context,
                    providerClassName = "com.example.widget.PixelCustomAppWidgetProvider"
                )
            }
        )
    }
}
