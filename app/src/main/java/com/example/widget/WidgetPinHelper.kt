package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.example.MainActivity

object WidgetPinHelper {

    fun isPinningSupported(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            appWidgetManager.isRequestPinAppWidgetSupported
        } else {
            false
        }
    }

    fun pinWidget(context: Context, providerClassName: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            if (appWidgetManager.isRequestPinAppWidgetSupported) {
                try {
                    val componentName = ComponentName(context, Class.forName(providerClassName))
                    val successIntent = Intent(context, MainActivity::class.java).apply {
                        action = "com.example.WIDGET_PINNED"
                    }
                    val successPendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        successIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    appWidgetManager.requestPinAppWidget(componentName, null, successPendingIntent)
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return false
    }
}
