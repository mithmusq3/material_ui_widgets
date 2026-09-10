package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.data.WidgetPresets
import com.example.model.WidgetConfig

class PixelClockAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val config = WidgetPresets.allPresets.find { it.id == "clock_scallop" }?.defaultConfig
            ?: WidgetConfig()

        for (id in appWidgetIds) {
            updateClockWidget(context, appWidgetManager, id, config)
        }
    }

    companion object {
        fun updateClockWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int, config: WidgetConfig) {
            val bitmap = PixelWidgetRenderer.renderClockWidget(context, config, 512, 512)
            val views = RemoteViews(context.packageName, R.layout.widget_pixel_clock_layout)
            views.setImageViewBitmap(R.id.widget_image, bitmap)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                context, widgetId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }
}

class PixelWeatherAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val config = WidgetPresets.allPresets.find { it.id == "weather_pill_glance" }?.defaultConfig
            ?: WidgetConfig()

        for (id in appWidgetIds) {
            val bitmap = PixelWidgetRenderer.renderWeatherWidget(context, config, 800, 260)
            val views = RemoteViews(context.packageName, R.layout.widget_pixel_weather_layout)
            views.setImageViewBitmap(R.id.widget_image, bitmap)

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(id, views)
        }
    }
}

class PixelBatteryAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
        val batteryLevel = batteryManager?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: 84
        val isCharging = batteryManager?.isCharging == true

        val baseConfig = WidgetPresets.allPresets.find { it.id == "battery_ring_status" }?.defaultConfig
            ?: WidgetConfig()
        val config = baseConfig.copy(batteryPercent = batteryLevel, isCharging = isCharging)

        for (id in appWidgetIds) {
            val bitmap = PixelWidgetRenderer.renderBatteryWidget(context, config, 512, 512)
            val views = RemoteViews(context.packageName, R.layout.widget_pixel_battery_layout)
            views.setImageViewBitmap(R.id.widget_image, bitmap)

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(id, views)
        }
    }
}

class PixelMusicAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val config = WidgetPresets.allPresets.find { it.id == "music_pill_player" }?.defaultConfig
            ?: WidgetConfig()

        for (id in appWidgetIds) {
            val bitmap = PixelWidgetRenderer.renderMusicWidget(context, config, 800, 340)
            val views = RemoteViews(context.packageName, R.layout.widget_pixel_music_layout)
            views.setImageViewBitmap(R.id.widget_image, bitmap)

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(id, views)
        }
    }
}

class PixelQuoteAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val config = WidgetPresets.allPresets.find { it.id == "quote_squircle_card" }?.defaultConfig
            ?: WidgetConfig()

        for (id in appWidgetIds) {
            val bitmap = PixelWidgetRenderer.renderQuoteWidget(context, config, 600, 360)
            val views = RemoteViews(context.packageName, R.layout.widget_pixel_quote_layout)
            views.setImageViewBitmap(R.id.widget_image, bitmap)

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(id, views)
        }
    }
}

class PixelCustomAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val repo = com.example.data.WidgetRepository(context)
        val customList = repo.customWidgets.value
        val config = customList.firstOrNull() ?: WidgetPresets.allPresets.firstOrNull { it.id == "webpage_hackernews" }?.defaultConfig ?: WidgetConfig()

        for (id in appWidgetIds) {
            val bitmap = PixelWidgetRenderer.renderCustomWidget(context, config, 600, 360)
            val views = RemoteViews(context.packageName, R.layout.widget_pixel_custom_layout)
            views.setImageViewBitmap(R.id.widget_custom_image, bitmap)

            val intent = if (config.customType == com.example.model.CustomWidgetType.WEBPAGE_BOOKMARK) {
                try {
                    Intent(Intent.ACTION_VIEW, android.net.Uri.parse(config.webUrl)).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                } catch (e: Exception) {
                    Intent(context, MainActivity::class.java)
                }
            } else {
                Intent(context, MainActivity::class.java)
            }

            val pendingIntent = PendingIntent.getActivity(
                context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_custom_container, pendingIntent)

            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
