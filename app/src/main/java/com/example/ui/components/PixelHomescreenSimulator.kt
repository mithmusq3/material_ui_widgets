package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WidgetPresets
import com.example.model.PixelWallpaper
import com.example.model.WidgetConfig

@Composable
fun PixelHomescreenSimulator(
    widgetConfig: WidgetConfig,
    onApplyWallpaperPalette: ((com.example.model.PaletteOption) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedWallpaper by remember { mutableStateOf(WidgetPresets.sampleWallpapers.first()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Wallpaper Switcher Carousel
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Wallpaper,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Pixel Wallpaper Preview",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            // Quick apply palette button
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.clickable {
                    onApplyWallpaperPalette?.invoke(selectedWallpaper.paletteTag)
                }
            ) {
                Text(
                    text = "Match Widget Colors",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Wallpaper thumbnails
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(WidgetPresets.sampleWallpapers) { wallpaper ->
                val isSelected = wallpaper.id == selectedWallpaper.id
                Box(
                    modifier = Modifier
                        .size(width = 64.dp, height = 44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.linearGradient(wallpaper.gradientColors))
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedWallpaper = wallpaper }
                        .testTag("wallpaper_thumb_${wallpaper.id}"),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = wallpaper.name.split(" ").first(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.4f))
                            .fillMaxWidth()
                            .padding(vertical = 1.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Phone Screen Frame with bezel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(440.dp)
                .shadow(16.dp, RoundedCornerShape(32.dp))
                .clip(RoundedCornerShape(32.dp))
                .background(Brush.verticalGradient(selectedWallpaper.gradientColors))
                .border(2.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(32.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top status bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "9:30",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "5G",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.8f))
                        )
                        Box(
                            modifier = Modifier
                                .width(18.dp)
                                .height(9.dp)
                                .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(2.dp))
                                .padding(1.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .fillMaxSize()
                                    .background(Color.White)
                            )
                        }
                    }
                }

                // Pixel At a Glance widget top text
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = "Tuesday, Sep 9",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color.White.copy(alpha = 0.95f),
                            fontWeight = FontWeight.SemiBold,
                            shadow = androidx.compose.ui.graphics.Shadow(Color.Black.copy(alpha = 0.4f), blurRadius = 4f)
                        )
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Mic,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "22°C • Partly Cloudy",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                shadow = androidx.compose.ui.graphics.Shadow(Color.Black.copy(alpha = 0.4f), blurRadius = 4f)
                            )
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // The Featured Custom Widget Centerpiece!
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    val widgetModifier = when (widgetConfig.sizeGrid) {
                        "2x2" -> Modifier.size(190.dp)
                        "4x1" -> Modifier.fillMaxWidth().height(86.dp)
                        "4x2" -> Modifier.fillMaxWidth().height(150.dp)
                        else -> Modifier.fillMaxWidth().height(120.dp)
                    }

                    PixelWidgetCard(
                        config = widgetConfig,
                        modifier = widgetModifier
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Bottom App Icons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    LauncherIcon(Icons.Default.Phone, Color(0xFF4CAF50))
                    LauncherIcon(Icons.Default.ChatBubble, Color(0xFF2196F3))
                    LauncherIcon(Icons.Default.Language, Color(0xFFFF9800))
                    LauncherIcon(Icons.Default.CameraAlt, Color(0xFFE91E63))
                }

                Spacer(Modifier.height(10.dp))

                // Pixel Search Bar Pill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color.White.copy(alpha = 0.25f))
                        .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(22.dp))
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "G",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "Search apps & web",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f))
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

private val WidgetConfig.sizeGrid: String
    get() = when (presetId) {
        "weather_pill_glance", "battery_dual_capsule", "music_compact_capsule" -> "4x1"
        "music_pill_player", "weather_card_detailed" -> "4x2"
        else -> "2x2"
    }

@Composable
private fun LauncherIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
    }
}
