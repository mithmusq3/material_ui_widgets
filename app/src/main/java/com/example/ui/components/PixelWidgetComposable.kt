package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import com.example.model.CustomWidgetType
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WidgetCategory
import com.example.model.WidgetConfig
import com.example.model.WidgetFont
import com.example.model.WidgetShape
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PixelWidgetCard(
    config: WidgetConfig,
    modifier: Modifier = Modifier,
    onInteractiveClick: (() -> Unit)? = null
) {
    val palette = config.palette
    val shapeModifier = when (config.shape) {
        WidgetShape.SCALLOP_FLOWER -> Modifier.clip(PixelScallopShape(petalCount = 4, depthRatio = 0.15f))
        WidgetShape.PILL -> Modifier.clip(RoundedCornerShape(percent = 50))
        WidgetShape.SQUIRCLE -> Modifier.clip(PixelSquircleShape(0.32f))
        WidgetShape.ROUNDED_RECT -> Modifier.clip(RoundedCornerShape(config.cornerRadiusDp.dp))
        WidgetShape.DUAL_PILL -> Modifier.clip(RoundedCornerShape(config.cornerRadiusDp.dp))
    }

    val fontFam = when (config.font) {
        WidgetFont.PIXEL_SANS -> FontFamily.Default
        WidgetFont.EDITORIAL_SERIF -> FontFamily.Serif
        WidgetFont.MONO_TECH -> FontFamily.Monospace
        WidgetFont.ROUNDED_CRAYON -> FontFamily.Cursive
    }

    Surface(
        modifier = modifier
            .shadow(6.dp, when (config.shape) {
                WidgetShape.SCALLOP_FLOWER -> PixelScallopShape(petalCount = 4, depthRatio = 0.15f)
                WidgetShape.PILL -> RoundedCornerShape(percent = 50)
                WidgetShape.SQUIRCLE -> PixelSquircleShape(0.32f)
                else -> RoundedCornerShape(config.cornerRadiusDp.dp)
            })
            .then(shapeModifier)
            .clickable(enabled = onInteractiveClick != null) { onInteractiveClick?.invoke() }
            .testTag("widget_card_${config.id}"),
        color = palette.composeContainer.copy(alpha = config.backgroundOpacity),
        contentColor = palette.composeOnContainer
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                config.customType == CustomWidgetType.WEBPAGE_BOOKMARK || config.category == WidgetCategory.WEBPAGE -> {
                    WebpageWidgetContent(config = config, font = fontFam)
                }
                config.customType == CustomWidgetType.COUNTER_TRACKER -> {
                    CounterTrackerWidgetContent(config = config, font = fontFam)
                }
                config.customType == CustomWidgetType.QUICK_CHECKLIST || config.category == WidgetCategory.CHECKLIST -> {
                    ChecklistWidgetContent(config = config, font = fontFam)
                }
                config.customType == CustomWidgetType.FINANCIAL_TICKER || config.category == WidgetCategory.FINANCE -> {
                    FinancialTickerWidgetContent(config = config, font = fontFam)
                }
                config.customType == CustomWidgetType.EVENT_COUNTDOWN -> {
                    CountdownWidgetContent(config = config, font = fontFam)
                }
                config.category == WidgetCategory.CLOCKS -> {
                    if (config.presetId == "clock_scallop") {
                        AnalogClockContent(config = config, font = fontFam)
                    } else if (config.presetId == "clock_pill_dual") {
                        DualCapsuleClockContent(config = config, font = fontFam)
                    } else {
                        JumboDigitalClockContent(config = config, font = fontFam)
                    }
                }
                config.category == WidgetCategory.WEATHER -> {
                    if (config.presetId == "weather_pill_glance") {
                        WeatherPillContent(config = config, font = fontFam)
                    } else {
                        WeatherCardForecastContent(config = config, font = fontFam)
                    }
                }
                config.category == WidgetCategory.BATTERY -> {
                    if (config.presetId == "battery_dual_capsule") {
                        DualBatteryBarContent(config = config, font = fontFam)
                    } else if (config.presetId == "system_quick_glance") {
                        SystemPodContent(config = config, font = fontFam)
                    } else {
                        BatteryRingContent(config = config, font = fontFam)
                    }
                }
                config.category == WidgetCategory.MUSIC -> {
                    if (config.presetId == "music_compact_capsule") {
                        CompactMusicPillContent(config = config, font = fontFam)
                    } else {
                        TurntableMusicContent(config = config, font = fontFam)
                    }
                }
                config.category == WidgetCategory.QUOTES -> {
                    QuoteCardContent(config = config, font = fontFam)
                }
                else -> {
                    AnalogClockContent(config = config, font = fontFam)
                }
            }
        }
    }
}

@Composable
fun AnalogClockContent(config: WidgetConfig, font: FontFamily) {
    var currentTime by remember { mutableStateOf(Calendar.getInstance()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Calendar.getInstance()
            delay(1000)
        }
    }

    val hours = currentTime.get(Calendar.HOUR)
    val minutes = currentTime.get(Calendar.MINUTE)
    val seconds = currentTime.get(Calendar.SECOND)

    val hourAngle = (hours + minutes / 60f) * 30f
    val minuteAngle = (minutes + seconds / 60f) * 6f
    val secondAngle = seconds * 6f

    val primaryColor = config.palette.composePrimary
    val onContainer = config.palette.composeOnContainer
    val accentColor = config.palette.composeAccent

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val radius = minOf(size.width, size.height) / 2f

            // Dial 12 tick dots
            for (i in 0 until 12) {
                val a = Math.toRadians((i * 30).toDouble())
                val dotDist = radius * 0.72f
                val dx = cx + (dotDist * sin(a)).toFloat()
                val dy = cy - (dotDist * cos(a)).toFloat()
                drawCircle(
                    color = onContainer.copy(alpha = if (i % 3 == 0) 0.5f else 0.2f),
                    radius = if (i % 3 == 0) radius * 0.045f else radius * 0.025f,
                    center = Offset(dx, dy)
                )
            }

            // Hour Hand
            val hourRad = Math.toRadians(hourAngle.toDouble())
            val hourLen = radius * 0.45f
            val hx = cx + (hourLen * sin(hourRad)).toFloat()
            val hy = cy - (hourLen * cos(hourRad)).toFloat()
            drawLine(
                color = primaryColor,
                start = Offset(cx, cy),
                end = Offset(hx, hy),
                strokeWidth = radius * 0.08f,
                cap = StrokeCap.Round
            )

            // Minute Hand
            val minRad = Math.toRadians(minuteAngle.toDouble())
            val minLen = radius * 0.65f
            val mx = cx + (minLen * sin(minRad)).toFloat()
            val my = cy - (minLen * cos(minRad)).toFloat()
            drawLine(
                color = onContainer.copy(alpha = 0.9f),
                start = Offset(cx, cy),
                end = Offset(mx, my),
                strokeWidth = radius * 0.055f,
                cap = StrokeCap.Round
            )

            // Seconds Dot Indicator if enabled
            if (config.showSeconds) {
                val secRad = Math.toRadians(secondAngle.toDouble())
                val secDist = radius * 0.82f
                val sx = cx + (secDist * sin(secRad)).toFloat()
                val sy = cy - (secDist * cos(secRad)).toFloat()
                drawCircle(
                    color = accentColor,
                    radius = radius * 0.05f,
                    center = Offset(sx, sy)
                )
            }

            // Center Pin
            drawCircle(
                color = accentColor,
                radius = radius * 0.09f,
                center = Offset(cx, cy)
            )
            drawCircle(
                color = config.palette.composeContainer,
                radius = radius * 0.035f,
                center = Offset(cx, cy)
            )
        }

        // Date pill badge at bottom
        if (config.showDate) {
            val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
            val dateStr = dateFormat.format(Date())

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(12.dp))
                    .background(config.palette.composeSurface.copy(alpha = 0.7f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = dateStr,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = font,
                        fontWeight = FontWeight.SemiBold,
                        color = config.palette.composeOnSurface
                    )
                )
            }
        }
    }
}

@Composable
fun DualCapsuleClockContent(config: WidgetConfig, font: FontFamily) {
    var now by remember { mutableStateOf(Date()) }
    LaunchedEffect(Unit) {
        while (true) {
            now = Date()
            delay(1000)
        }
    }

    val timeFormat = SimpleDateFormat(if (config.is24Hour) "HH:mm" else "h:mm", Locale.getDefault())
    val amPmFormat = SimpleDateFormat("a", Locale.getDefault())
    val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Digital Hours Column
        Column(
            modifier = Modifier.weight(1.3f),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = timeFormat.format(now),
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontFamily = font,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-1).sp,
                        color = config.palette.composeOnContainer
                    )
                )
                if (!config.is24Hour) {
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = amPmFormat.format(now).uppercase(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = config.palette.composePrimary
                        ),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            }
            Text(
                text = dateFormat.format(now),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = font,
                    fontWeight = FontWeight.Medium,
                    color = config.palette.composeOnContainer.copy(alpha = 0.7f)
                )
            )
        }

        // Weather side pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(config.palette.composePrimary)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = "Weather",
                    tint = Color(0xFFFFD54F),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${config.currentTemp}${config.tempUnit}",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun JumboDigitalClockContent(config: WidgetConfig, font: FontFamily) {
    var now by remember { mutableStateOf(Date()) }
    LaunchedEffect(Unit) {
        while (true) {
            now = Date()
            delay(1000)
        }
    }

    val hourFormat = SimpleDateFormat(if (config.is24Hour) "HH" else "hh", Locale.getDefault())
    val minuteFormat = SimpleDateFormat("mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("EEE, d MMM", Locale.getDefault())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hourFormat.format(now),
            style = MaterialTheme.typography.displayLarge.copy(
                fontFamily = font,
                fontWeight = FontWeight.Black,
                fontSize = 52.sp,
                lineHeight = 48.sp,
                color = config.palette.composePrimary
            )
        )
        Text(
            text = minuteFormat.format(now),
            style = MaterialTheme.typography.displayLarge.copy(
                fontFamily = font,
                fontWeight = FontWeight.Black,
                fontSize = 52.sp,
                lineHeight = 48.sp,
                color = config.palette.composeOnContainer
            )
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = dateFormat.format(now),
            style = MaterialTheme.typography.labelMedium.copy(
                fontFamily = font,
                fontWeight = FontWeight.SemiBold,
                color = config.palette.composeOnContainer.copy(alpha = 0.7f)
            )
        )
    }
}

@Composable
fun WeatherPillContent(config: WidgetConfig, font: FontFamily) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(config.palette.composePrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = "${config.currentTemp}${config.tempUnit}",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = font,
                        fontWeight = FontWeight.Bold,
                        color = config.palette.composeOnContainer
                    )
                )
                Text(
                    text = config.weatherCondition,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = font,
                        fontWeight = FontWeight.Medium,
                        color = config.palette.composeOnContainer.copy(alpha = 0.7f)
                    )
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.NearMe,
                    contentDescription = null,
                    tint = config.palette.composePrimary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    text = config.customCity,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = font,
                        fontWeight = FontWeight.Bold,
                        color = config.palette.composeOnContainer
                    )
                )
            }
            Text(
                text = "H: ${config.currentTemp + 4}°  L: ${config.currentTemp - 5}°",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = config.palette.composeOnContainer.copy(alpha = 0.6f)
                )
            )
        }
    }
}

@Composable
fun WeatherCardForecastContent(config: WidgetConfig, font: FontFamily) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = config.customCity,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = font,
                        fontWeight = FontWeight.Bold,
                        color = config.palette.composeOnContainer
                    )
                )
                Text(
                    text = config.weatherCondition,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = config.palette.composeOnContainer.copy(alpha = 0.7f)
                    )
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${config.currentTemp}${config.tempUnit}",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = font,
                        fontWeight = FontWeight.Bold,
                        color = config.palette.composeOnContainer
                    )
                )
            }
        }

        // Mini 3-hour forecast pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                Triple("Now", "${config.currentTemp}°", "Sunny"),
                Triple("3 PM", "${config.currentTemp + 2}°", "Partly"),
                Triple("6 PM", "${config.currentTemp - 2}°", "Clear")
            ).forEach { (time, temp, desc) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(config.palette.composeSurface.copy(alpha = 0.6f))
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = time, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium))
                        Spacer(Modifier.height(2.dp))
                        Text(text = temp, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                        Text(text = desc, style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, color = config.palette.composePrimary))
                    }
                }
            }
        }
    }
}

@Composable
fun BatteryRingContent(config: WidgetConfig, font: FontFamily) {
    val primaryColor = config.palette.composePrimary
    val onContainer = config.palette.composeOnContainer
    val accentColor = config.palette.composeAccent

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeW = size.width * 0.12f
                val radius = (size.width - strokeW) / 2f
                val center = Offset(size.width / 2f, size.height / 2f)

                // Background track
                drawCircle(
                    color = onContainer.copy(alpha = 0.12f),
                    radius = radius,
                    center = center,
                    style = Stroke(width = strokeW, cap = StrokeCap.Round)
                )

                // Progress sweep
                val sweep = (config.batteryPercent / 100f) * 360f
                drawArc(
                    color = primaryColor,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = Offset(strokeW / 2f, strokeW / 2f),
                    size = androidx.compose.ui.geometry.Size(size.width - strokeW, size.height - strokeW),
                    style = Stroke(width = strokeW, cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (config.isCharging) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Charging",
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "${config.batteryPercent}%",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = font,
                        fontWeight = FontWeight.Black,
                        color = onContainer
                    )
                )
            }
        }

        Text(
            text = if (config.isCharging) "⚡ Charging rapidly" else "Pixel 8 Pro • 12h remaining",
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = font,
                fontWeight = FontWeight.SemiBold,
                color = onContainer.copy(alpha = 0.7f)
            ),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun DualBatteryBarContent(config: WidgetConfig, font: FontFamily) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Phone status pill
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(config.palette.composeSurface.copy(alpha = 0.6f))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Smartphone, contentDescription = null, tint = config.palette.composePrimary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("Pixel Phone", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium))
                    Text("${config.batteryPercent}%", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                }
            }
        }

        // Pixel Buds status pill
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(config.palette.composeSurface.copy(alpha = 0.6f))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Headphones, contentDescription = null, tint = config.palette.composeAccent, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("Pixel Buds Pro", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium))
                    Text("92%", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun TurntableMusicContent(config: WidgetConfig, font: FontFamily) {
    var playing by remember { mutableStateOf(config.isPlaying) }
    var progress by remember { mutableFloatStateOf(config.playbackProgress) }

    val infiniteTransition = rememberInfiniteTransition(label = "vinyl_spin")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rotating Vinyl Disc
        Box(
            modifier = Modifier
                .size(72.dp)
                .rotate(if (playing) angle else 0f)
                .clip(CircleShape)
                .background(Color(0xFF1C1B1F)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val r = size.width / 2f
                val c = Offset(r, r)
                drawCircle(Color(0xFF2C2C2C), radius = r * 0.8f, center = c, style = Stroke(1.5f))
                drawCircle(Color(0xFF383838), radius = r * 0.6f, center = c, style = Stroke(1.5f))
                drawCircle(Color(0xFF424242), radius = r * 0.4f, center = c, style = Stroke(1.5f))
            }
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(config.palette.composeAccent),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF1C1B1F)))
            }
        }

        Spacer(Modifier.width(14.dp))

        // Track Info & Controls
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = config.songTitle,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = font,
                    fontWeight = FontWeight.Bold,
                    color = config.palette.composeOnContainer
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = config.artistName,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = font,
                    color = config.palette.composeOnContainer.copy(alpha = 0.7f)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))

            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(config.palette.composeOnContainer.copy(alpha = 0.15f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.dp))
                        .background(config.palette.composePrimary)
                )
            }

            Spacer(Modifier.height(8.dp))

            // Playback controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "Previous",
                    tint = config.palette.composeOnContainer.copy(alpha = 0.8f),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { progress = maxOf(0.1f, progress - 0.2f) }
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(config.palette.composePrimary)
                        .clickable { playing = !playing },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (playing) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next",
                    tint = config.palette.composeOnContainer.copy(alpha = 0.8f),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { progress = minOf(0.9f, progress + 0.2f) }
                )
            }
        }
    }
}

@Composable
fun CompactMusicPillContent(config: WidgetConfig, font: FontFamily) {
    var isPlaying by remember { mutableStateOf(config.isPlaying) }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(config.palette.composePrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = config.songTitle,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontFamily = font,
                        fontWeight = FontWeight.Bold,
                        color = config.palette.composeOnContainer
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = config.artistName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = config.palette.composeOnContainer.copy(alpha = 0.7f)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(config.palette.composePrimary.copy(alpha = 0.15f))
                .clickable { isPlaying = !isPlaying },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play/Pause",
                tint = config.palette.composePrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun QuoteCardContent(config: WidgetConfig, font: FontFamily) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "“",
            style = MaterialTheme.typography.displayMedium.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Black,
                color = config.palette.composePrimary.copy(alpha = 0.6f),
                lineHeight = 30.sp
            )
        )
        Text(
            text = config.quoteText,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = font,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Normal,
                color = config.palette.composeOnContainer,
                lineHeight = 22.sp
            ),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "— ${config.quoteAuthor}",
            style = MaterialTheme.typography.labelMedium.copy(
                fontFamily = font,
                fontWeight = FontWeight.Bold,
                color = config.palette.composePrimary
            ),
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun SystemPodContent(config: WidgetConfig, font: FontFamily) {
    var wifiOn by remember { mutableStateOf(true) }
    var btOn by remember { mutableStateOf(true) }
    var torchOn by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SystemTile(
                icon = Icons.Default.Wifi,
                title = "Wi-Fi",
                subtitle = if (wifiOn) "Pixel_5G" else "Off",
                active = wifiOn,
                palette = config.palette,
                modifier = Modifier.weight(1f).clickable { wifiOn = !wifiOn }
            )
            SystemTile(
                icon = Icons.Default.Bluetooth,
                title = "Bluetooth",
                subtitle = if (btOn) "Pixel Buds" else "Off",
                active = btOn,
                palette = config.palette,
                modifier = Modifier.weight(1f).clickable { btOn = !btOn }
            )
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SystemTile(
                icon = Icons.Default.FlashlightOn,
                title = "Flashlight",
                subtitle = if (torchOn) "On" else "Off",
                active = torchOn,
                palette = config.palette,
                modifier = Modifier.weight(1f).clickable { torchOn = !torchOn }
            )
            SystemTile(
                icon = Icons.Default.BatteryChargingFull,
                title = "Battery",
                subtitle = "84% Eco",
                active = true,
                palette = config.palette,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SystemTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    active: Boolean,
    palette: com.example.model.PaletteOption,
    modifier: Modifier = Modifier
) {
    val bgColor = if (active) palette.composePrimary else palette.composeSurface.copy(alpha = 0.5f)
    val contentColor = if (active) Color.White else palette.composeOnContainer

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(18.dp))
            .background(bgColor)
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = title, tint = contentColor, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Column {
                Text(title, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = contentColor, fontSize = 11.sp))
                Text(subtitle, style = MaterialTheme.typography.labelSmall.copy(color = contentColor.copy(alpha = 0.8f), fontSize = 9.sp))
            }
        }
    }
}

@Composable
fun WebpageWidgetContent(config: WidgetConfig, font: FontFamily) {
    val context = LocalContext.current
    val palette = config.palette
    val domainDisplay = remember(config.webUrl) {
        try {
            val uri = Uri.parse(config.webUrl)
            uri.host?.replace("www.", "") ?: config.webUrl
        } catch (e: Exception) {
            "web.link"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top row: Domain pill and live indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = palette.composePrimary.copy(alpha = 0.15f),
                contentColor = palette.composePrimary
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Web",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = domainDisplay,
                        fontFamily = font,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            // Live status dot
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Live",
                    fontFamily = font,
                    fontSize = 10.sp,
                    color = palette.composeOnContainer.copy(alpha = 0.7f)
                )
            }
        }

        // Center: Title and Subtitle
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text(
                text = config.webTitle,
                fontFamily = font,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = palette.composeOnContainer
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = config.webSubtitle,
                fontFamily = font,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = palette.composeOnContainer.copy(alpha = 0.75f)
            )
        }

        // Bottom: 1-Tap Open in Browser button
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(config.webUrl)).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // ignore
                    }
                },
            color = palette.composePrimary,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = "Open Webpage",
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Visit Webpage",
                    fontFamily = font,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun CounterTrackerWidgetContent(config: WidgetConfig, font: FontFamily) {
    val palette = config.palette
    var count by remember(config.counterCurrent) { mutableStateOf(config.counterCurrent) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = palette.composePrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = config.counterName,
                    fontFamily = font,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = palette.composeOnContainer
                )
            }

            Text(
                text = "${(count.toFloat() / maxOf(1, config.counterTarget) * 100).toInt()}%",
                fontFamily = font,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = palette.composePrimary
            )
        }

        // Counter Center Display & Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Minus Button
            Surface(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable {
                        if (count > 0) count -= config.counterStep
                    },
                color = palette.composePrimary.copy(alpha = 0.15f),
                contentColor = palette.composePrimary,
                shape = CircleShape
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrement", modifier = Modifier.size(18.dp))
                }
            }

            // Big Count
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$count",
                    fontFamily = font,
                    fontWeight = FontWeight.Black,
                    fontSize = 34.sp,
                    color = palette.composePrimary
                )
                Text(
                    text = "Goal: ${config.counterTarget}",
                    fontFamily = font,
                    fontSize = 11.sp,
                    color = palette.composeOnContainer.copy(alpha = 0.7f)
                )
            }

            // Plus Button
            Surface(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable {
                        count += config.counterStep
                    },
                color = palette.composePrimary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Add, contentDescription = "Increment", modifier = Modifier.size(18.dp))
                }
            }
        }

        // Progress Bar
        val progress = (count.toFloat() / maxOf(1, config.counterTarget)).coerceIn(0f, 1f)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(palette.composeOnContainer.copy(alpha = 0.15f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = progress)
                    .clip(RoundedCornerShape(3.dp))
                    .background(palette.composePrimary)
            )
        }
    }
}

@Composable
fun ChecklistWidgetContent(config: WidgetConfig, font: FontFamily) {
    val palette = config.palette
    var checkedStates by remember(config.checklistChecked) {
        mutableStateOf(config.checklistChecked)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = config.checklistTitle,
                fontFamily = font,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = palette.composePrimary
            )
            val doneCount = checkedStates.count { it }
            Text(
                text = "$doneCount/${config.checklistItems.size} done",
                fontFamily = font,
                fontSize = 11.sp,
                color = palette.composeOnContainer.copy(alpha = 0.7f)
            )
        }

        // Task Items
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            config.checklistItems.take(3).forEachIndexed { index, item ->
                val isChecked = checkedStates.getOrElse(index) { false }
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            val mutable = checkedStates.toMutableList()
                            while (mutable.size <= index) mutable.add(false)
                            mutable[index] = !mutable[index]
                            checkedStates = mutable
                        },
                    color = if (isChecked) palette.composeSurface.copy(alpha = 0.4f) else palette.composeSurface.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = if (isChecked) "Checked" else "Unchecked",
                            tint = if (isChecked) palette.composePrimary else palette.composeOnContainer.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = item,
                            fontFamily = font,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = if (isChecked) palette.composeOnContainer.copy(alpha = 0.45f) else palette.composeOnContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FinancialTickerWidgetContent(config: WidgetConfig, font: FontFamily) {
    val palette = config.palette
    val isPositive = config.tickerIsPositive

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Symbol & Ticker badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = config.tickerSymbol,
                fontFamily = font,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = palette.composeOnContainer
            )

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isPositive) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                contentColor = if (isPositive) Color(0xFF2E7D32) else Color(0xFFC62828)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isPositive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = config.tickerChange,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Price
        Text(
            text = config.tickerPrice,
            fontFamily = font,
            fontWeight = FontWeight.Black,
            fontSize = 28.sp,
            color = palette.composePrimary
        )

        // Mini Sparkline Graph
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp)
        ) {
            val strokeColor = if (isPositive) Color(0xFF2E7D32) else Color(0xFFC62828)
            val points = if (isPositive) {
                listOf(0.7f, 0.6f, 0.75f, 0.45f, 0.5f, 0.3f, 0.2f, 0.15f)
            } else {
                listOf(0.2f, 0.3f, 0.25f, 0.5f, 0.4f, 0.65f, 0.7f, 0.85f)
            }

            val stepX = size.width / (points.size - 1)
            val path = androidx.compose.ui.graphics.Path()
            points.forEachIndexed { i, frac ->
                val x = i * stepX
                val y = frac * size.height
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(
                path = path,
                color = strokeColor,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
fun CountdownWidgetContent(config: WidgetConfig, font: FontFamily) {
    val palette = config.palette

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Tag
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Event,
                contentDescription = null,
                tint = palette.composePrimary,
                modifier = Modifier.size(15.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = config.countdownEvent,
                fontFamily = font,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = palette.composeOnContainer
            )
        }

        // Huge Days Number
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${config.countdownDays}",
                fontFamily = font,
                fontWeight = FontWeight.Black,
                fontSize = 42.sp,
                lineHeight = 44.sp,
                color = palette.composePrimary
            )
            Text(
                text = "DAYS REMAINING",
                fontFamily = font,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                fontSize = 10.sp,
                color = palette.composeOnContainer.copy(alpha = 0.7f)
            )
        }

        // Bottom subtitle
        Text(
            text = "Milestone Target",
            fontFamily = font,
            fontSize = 11.sp,
            color = palette.composePrimary
        )
    }
}
