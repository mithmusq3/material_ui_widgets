package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Build
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.model.PaletteOption
import com.example.model.WidgetConfig
import com.example.model.WidgetShape
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

object PixelWidgetRenderer {

    fun renderClockWidget(context: Context, config: WidgetConfig, width: Int = 512, height: Int = 512): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val cx = width / 2f
        val cy = height / 2f
        val pad = width * 0.06f

        val palette = config.palette
        val containerColor = palette.containerColor.toInt()
        val onContainerColor = palette.onContainerColor.toInt()
        val primaryColor = palette.primaryColor.toInt()
        val accentColor = palette.accentColor.toInt()

        // Background container shape
        paint.color = containerColor
        paint.style = Paint.Style.FILL

        when (config.shape) {
            WidgetShape.SCALLOP_FLOWER -> {
                drawScallop(canvas, cx, cy, (min(width, height) / 2f) - pad, 4, 0.16f, paint)
            }
            WidgetShape.PILL -> {
                val r = RectF(pad, pad, width - pad, height - pad)
                val rx = min(width, height) / 2f
                canvas.drawRoundRect(r, rx, rx, paint)
            }
            WidgetShape.SQUIRCLE, WidgetShape.ROUNDED_RECT, WidgetShape.DUAL_PILL -> {
                val r = RectF(pad, pad, width - pad, height - pad)
                val corner = width * 0.22f
                canvas.drawRoundRect(r, corner, corner, paint)
            }
        }

        // Time calculation
        val cal = Calendar.getInstance()
        val hours = cal.get(Calendar.HOUR)
        val minutes = cal.get(Calendar.MINUTE)
        val seconds = cal.get(Calendar.SECOND)

        val hourAngle = (hours + minutes / 60f) * 30f
        val minuteAngle = minutes * 6f

        // Center dial accent ring
        paint.color = onContainerColor
        paint.alpha = 30
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = width * 0.015f
        canvas.drawCircle(cx, cy, width * 0.32f, paint)

        // 12, 3, 6, 9 ticks
        paint.color = onContainerColor
        paint.alpha = 90
        paint.style = Paint.Style.FILL
        for (i in 0 until 12) {
            val a = Math.toRadians((i * 30).toDouble())
            val dotR = width * 0.32f
            val dx = cx + (dotR * sin(a)).toFloat()
            val dy = cy - (dotR * cos(a)).toFloat()
            val radius = if (i % 3 == 0) width * 0.02f else width * 0.01f
            canvas.drawCircle(dx, dy, radius, paint)
        }

        // Hour Hand
        paint.color = primaryColor
        paint.alpha = 255
        paint.strokeWidth = width * 0.05f
        paint.strokeCap = Paint.Cap.ROUND
        val hourLen = width * 0.22f
        val haRad = Math.toRadians(hourAngle.toDouble())
        val hx = cx + (hourLen * sin(haRad)).toFloat()
        val hy = cy - (hourLen * cos(haRad)).toFloat()
        canvas.drawLine(cx, cy, hx, hy, paint)

        // Minute Hand
        paint.color = onContainerColor
        paint.strokeWidth = width * 0.035f
        val minLen = width * 0.32f
        val maRad = Math.toRadians(minuteAngle.toDouble())
        val mx = cx + (minLen * sin(maRad)).toFloat()
        val my = cy - (minLen * cos(maRad)).toFloat()
        canvas.drawLine(cx, cy, mx, my, paint)

        // Center Pivot
        paint.color = accentColor
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, width * 0.045f, paint)

        // Small digital pill tag at bottom or center if requested
        if (config.showDate) {
            val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
            val dateText = dateFormat.format(Date())

            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = onContainerColor
                textSize = width * 0.065f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(dateText, cx, height - pad * 1.5f, textPaint)
        }

        return bitmap
    }

    fun renderWeatherWidget(context: Context, config: WidgetConfig, width: Int = 800, height: Int = 260): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val palette = config.palette
        val containerColor = palette.containerColor.toInt()
        val onContainerColor = palette.onContainerColor.toInt()
        val primaryColor = palette.primaryColor.toInt()
        val accentColor = palette.accentColor.toInt()

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = containerColor
            style = Paint.Style.FILL
        }

        val pad = height * 0.1f
        val rect = RectF(pad, pad, width - pad, height - pad)
        val rx = (height - 2 * pad) / 2f
        canvas.drawRoundRect(rect, rx, rx, paint)

        // Sun / Weather icon on the left
        val iconCx = pad + rx
        val iconCy = height / 2f
        val sunRadius = rx * 0.42f

        paint.color = accentColor
        canvas.drawCircle(iconCx, iconCy, sunRadius, paint)

        // Sun rays
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = rx * 0.1f
        paint.strokeCap = Paint.Cap.ROUND
        for (i in 0 until 8) {
            val a = Math.toRadians((i * 45).toDouble())
            val r1 = sunRadius * 1.35f
            val r2 = sunRadius * 1.75f
            val x1 = iconCx + (r1 * cos(a)).toFloat()
            val y1 = iconCy + (r1 * sin(a)).toFloat()
            val x2 = iconCx + (r2 * cos(a)).toFloat()
            val y2 = iconCy + (r2 * sin(a)).toFloat()
            canvas.drawLine(x1, y1, x2, y2, paint)
        }

        // Temperature text
        paint.style = Paint.Style.FILL
        paint.color = onContainerColor
        val tempPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = onContainerColor
            textSize = height * 0.42f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val tempStr = "${config.currentTemp}${config.tempUnit}"
        val tempX = iconCx + rx * 1.4f
        val tempY = height / 2f + height * 0.15f
        canvas.drawText(tempStr, tempX, tempY, tempPaint)

        // City & condition text
        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = onContainerColor
            alpha = 200
            textSize = height * 0.18f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        val textBounds = android.graphics.Rect()
        tempPaint.getTextBounds(tempStr, 0, tempStr.length, textBounds)
        val detailsX = tempX + textBounds.width() + height * 0.25f

        canvas.drawText(config.weatherCondition, detailsX, height / 2f - height * 0.05f, subPaint)
        subPaint.alpha = 150
        canvas.drawText("${config.customCity} • Today", detailsX, height / 2f + height * 0.20f, subPaint)

        return bitmap
    }

    fun renderBatteryWidget(context: Context, config: WidgetConfig, width: Int = 512, height: Int = 512): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val palette = config.palette
        val containerColor = palette.containerColor.toInt()
        val onContainerColor = palette.onContainerColor.toInt()
        val primaryColor = palette.primaryColor.toInt()
        val accentColor = palette.accentColor.toInt()

        val pad = width * 0.08f
        val r = RectF(pad, pad, width - pad, height - pad)
        val corner = width * 0.24f

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = containerColor
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(r, corner, corner, paint)

        val cx = width / 2f
        val cy = height / 2f - height * 0.05f
        val ringR = width * 0.28f

        // Progress track
        val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = onContainerColor
            alpha = 40
            style = Paint.Style.STROKE
            strokeWidth = width * 0.07f
            strokeCap = Paint.Cap.ROUND
        }
        val ringBounds = RectF(cx - ringR, cy - ringR, cx + ringR, cy + ringR)
        canvas.drawArc(ringBounds, -90f, 360f, false, trackPaint)

        // Progress value
        val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = primaryColor
            style = Paint.Style.STROKE
            strokeWidth = width * 0.07f
            strokeCap = Paint.Cap.ROUND
        }
        val sweepAngle = (config.batteryPercent / 100f) * 360f
        canvas.drawArc(ringBounds, -90f, sweepAngle, false, progressPaint)

        // Center Percentage
        val percentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = onContainerColor
            textSize = width * 0.16f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("${config.batteryPercent}%", cx, cy + width * 0.06f, percentPaint)

        // Subtitle Device name & charging status
        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = onContainerColor
            alpha = 200
            textSize = width * 0.06f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        val statusText = if (config.isCharging) "⚡ Charging • Pixel 8" else "Pixel Battery • Normal"
        canvas.drawText(statusText, cx, height - pad * 1.5f, subPaint)

        return bitmap
    }

    fun renderMusicWidget(context: Context, config: WidgetConfig, width: Int = 800, height: Int = 340): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val palette = config.palette
        val containerColor = palette.containerColor.toInt()
        val onContainerColor = palette.onContainerColor.toInt()
        val primaryColor = palette.primaryColor.toInt()
        val accentColor = palette.accentColor.toInt()

        val pad = height * 0.08f
        val rect = RectF(pad, pad, width - pad, height - pad)
        val corner = height * 0.26f

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = containerColor
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(rect, corner, corner, paint)

        // Vinyl Disc / Album on the left
        val discCx = pad + height * 0.42f
        val discCy = height / 2f
        val discR = height * 0.36f

        // Disc vinyl body
        paint.color = 0xFF1C1B1F.toInt()
        canvas.drawCircle(discCx, discCy, discR, paint)

        // Vinyl grooves
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = 0xFF333333.toInt()
        canvas.drawCircle(discCx, discCy, discR * 0.8f, paint)
        canvas.drawCircle(discCx, discCy, discR * 0.6f, paint)

        // Center Album Label
        paint.style = Paint.Style.FILL
        paint.color = accentColor
        canvas.drawCircle(discCx, discCy, discR * 0.38f, paint)
        paint.color = containerColor
        canvas.drawCircle(discCx, discCy, discR * 0.12f, paint)

        // Song Title
        val textX = discCx + discR + height * 0.25f
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = onContainerColor
            textSize = height * 0.16f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(config.songTitle, textX, height * 0.36f, titlePaint)

        // Artist Name
        val artistPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = onContainerColor
            alpha = 180
            textSize = height * 0.11f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        canvas.drawText(config.artistName, textX, height * 0.52f, artistPaint)

        // Progress bar
        val barX1 = textX
        val barX2 = width - pad * 2f
        val barY = height * 0.68f
        val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = onContainerColor
            alpha = 40
            strokeWidth = height * 0.035f
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawLine(barX1, barY, barX2, barY, barPaint)

        barPaint.color = primaryColor
        barPaint.alpha = 255
        val progressX = barX1 + (barX2 - barX1) * config.playbackProgress
        canvas.drawLine(barX1, barY, progressX, barY, barPaint)

        // Media controls (Play/Pause pill)
        val playCx = width - pad * 2f - height * 0.3f
        val playCy = height * 0.35f
        paint.color = primaryColor
        canvas.drawCircle(playCx, playCy, height * 0.22f, paint)

        // Play triangle or pause bars
        paint.color = palette.onPrimaryColor.toInt()
        val triPath = Path().apply {
            val sz = height * 0.10f
            moveTo(playCx - sz * 0.6f, playCy - sz)
            lineTo(playCx + sz, playCy)
            lineTo(playCx - sz * 0.6f, playCy + sz)
            close()
        }
        canvas.drawPath(triPath, paint)

        return bitmap
    }

    fun renderQuoteWidget(context: Context, config: WidgetConfig, width: Int = 600, height: Int = 360): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val palette = config.palette
        val containerColor = palette.containerColor.toInt()
        val onContainerColor = palette.onContainerColor.toInt()
        val primaryColor = palette.primaryColor.toInt()

        val pad = width * 0.06f
        val rect = RectF(pad, pad, width - pad, height - pad)
        val corner = width * 0.14f

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = containerColor
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(rect, corner, corner, paint)

        // Decorative quote mark
        val quoteMarkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = primaryColor
            alpha = 80
            textSize = height * 0.40f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC)
        }
        canvas.drawText("“", pad * 1.8f, pad * 2.8f + height * 0.15f, quoteMarkPaint)

        // Quote text
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = onContainerColor
            textSize = height * 0.12f
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
        }

        val words = config.quoteText.split(" ")
        var line = ""
        var lineY = height * 0.38f
        val maxLineWidth = width - pad * 4f

        for (word in words) {
            val testLine = if (line.isEmpty()) word else "$line $word"
            if (textPaint.measureText(testLine) > maxLineWidth) {
                canvas.drawText(line, pad * 2.2f, lineY, textPaint)
                line = word
                lineY += height * 0.15f
            } else {
                line = testLine
            }
        }
        if (line.isNotEmpty()) {
            canvas.drawText(line, pad * 2.2f, lineY, textPaint)
        }

        // Author
        val authorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = primaryColor
            textSize = height * 0.09f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText("— ${config.quoteAuthor}", pad * 2.2f, height - pad * 1.8f, authorPaint)

        return bitmap
    }

    fun renderCustomWidget(context: Context, config: WidgetConfig, width: Int = 600, height: Int = 360): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val pad = width * 0.05f
        val palette = config.palette
        val containerColor = palette.containerColor.toInt()
        val onContainerColor = palette.onContainerColor.toInt()
        val primaryColor = palette.primaryColor.toInt()
        val accentColor = palette.accentColor.toInt()

        // Background shape
        paint.color = containerColor
        paint.style = Paint.Style.FILL
        val bgRect = RectF(pad, pad, width - pad, height - pad)
        val corner = width * 0.08f
        canvas.drawRoundRect(bgRect, corner, corner, paint)

        when (config.customType) {
            com.example.model.CustomWidgetType.WEBPAGE_BOOKMARK -> {
                // Header badge pill: Domain / Live Web Snapshot
                val pillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = primaryColor
                    style = Paint.Style.FILL
                }
                val pillRect = RectF(pad * 1.8f, pad * 1.6f, pad * 1.8f + width * 0.46f, pad * 1.6f + height * 0.16f)
                canvas.drawRoundRect(pillRect, height * 0.08f, height * 0.08f, pillPaint)

                val pillTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = 0xFFFFFFFF.toInt()
                    textSize = height * 0.075f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                val domainDisplay = try {
                    val uri = android.net.Uri.parse(config.webUrl)
                    uri.host?.replace("www.", "") ?: "web.link"
                } catch (e: Exception) { "Webpage" }
                canvas.drawText("🌐 $domainDisplay", pad * 2.3f, pad * 1.6f + height * 0.11f, pillTextPaint)

                // Web Page Title
                val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = onContainerColor
                    textSize = height * 0.13f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                val cleanTitle = if (config.webTitle.length > 20) config.webTitle.take(20) + "..." else config.webTitle
                canvas.drawText(cleanTitle, pad * 1.8f, height * 0.48f, titlePaint)

                // Subtitle snippet
                val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = onContainerColor
                    alpha = 180
                    textSize = height * 0.075f
                    typeface = Typeface.DEFAULT
                }
                val cleanSub = if (config.webSubtitle.length > 32) config.webSubtitle.take(32) + "..." else config.webSubtitle
                canvas.drawText(cleanSub, pad * 1.8f, height * 0.62f, subPaint)

                // Action launch pill at bottom
                val actionRect = RectF(pad * 1.8f, height - pad * 1.6f - height * 0.18f, width - pad * 1.8f, height - pad * 1.6f)
                paint.color = accentColor
                canvas.drawRoundRect(actionRect, height * 0.09f, height * 0.09f, paint)

                val actionTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = 0xFFFFFFFF.toInt()
                    textSize = height * 0.075f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    textAlign = Paint.Align.CENTER
                }
                canvas.drawText("↗ Tap to open ${config.webTitle}", actionRect.centerX(), actionRect.centerY() + height * 0.026f, actionTextPaint)
            }

            com.example.model.CustomWidgetType.COUNTER_TRACKER -> {
                val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = onContainerColor
                    textSize = height * 0.11f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                canvas.drawText("🎯 ${config.counterName}", pad * 1.8f, height * 0.28f, titlePaint)

                // Big counter numbers
                val numPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = primaryColor
                    textSize = height * 0.28f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                canvas.drawText("${config.counterCurrent}", pad * 1.8f, height * 0.65f, numPaint)

                val targetPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = onContainerColor
                    alpha = 160
                    textSize = height * 0.13f
                    typeface = Typeface.DEFAULT
                }
                canvas.drawText("/ ${config.counterTarget} goal", pad * 1.8f + height * 0.35f, height * 0.65f, targetPaint)

                // Progress Bar
                val progressRectBg = RectF(pad * 1.8f, height * 0.74f, width - pad * 1.8f, height * 0.82f)
                paint.color = onContainerColor
                paint.alpha = 40
                canvas.drawRoundRect(progressRectBg, height * 0.04f, height * 0.04f, paint)

                val ratio = (config.counterCurrent.toFloat() / maxOf(1, config.counterTarget)).coerceIn(0f, 1f)
                val progressRectFg = RectF(pad * 1.8f, height * 0.74f, pad * 1.8f + (width - pad * 3.6f) * ratio, height * 0.82f)
                paint.color = primaryColor
                paint.alpha = 255
                canvas.drawRoundRect(progressRectFg, height * 0.04f, height * 0.04f, paint)
            }

            com.example.model.CustomWidgetType.QUICK_CHECKLIST -> {
                val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = primaryColor
                    textSize = height * 0.10f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                canvas.drawText("✓ ${config.checklistTitle}", pad * 1.8f, height * 0.26f, titlePaint)

                val itemPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = onContainerColor
                    textSize = height * 0.085f
                    typeface = Typeface.DEFAULT
                }

                val items = config.checklistItems.take(3)
                items.forEachIndexed { index, item ->
                    val y = height * 0.44f + (index * height * 0.18f)
                    val isChecked = config.checklistChecked.getOrNull(index) == true
                    val prefix = if (isChecked) "[✓] " else "[  ] "
                    itemPaint.alpha = if (isChecked) 130 else 230
                    val truncated = if (item.length > 28) item.take(28) + "..." else item
                    canvas.drawText(prefix + truncated, pad * 1.8f, y, itemPaint)
                }
            }

            com.example.model.CustomWidgetType.FINANCIAL_TICKER -> {
                val symbolPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = onContainerColor
                    textSize = height * 0.12f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                canvas.drawText(config.tickerSymbol, pad * 1.8f, height * 0.28f, symbolPaint)

                val pricePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = primaryColor
                    textSize = height * 0.24f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                canvas.drawText(config.tickerPrice, pad * 1.8f, height * 0.62f, pricePaint)

                // Change pill
                val changeColor = if (config.tickerIsPositive) 0xFF2E7D32.toInt() else 0xFFC62828.toInt()
                val pillRect = RectF(pad * 1.8f, height * 0.72f, pad * 1.8f + width * 0.28f, height * 0.88f)
                paint.color = changeColor
                canvas.drawRoundRect(pillRect, height * 0.08f, height * 0.08f, paint)

                val changeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = 0xFFFFFFFF.toInt()
                    textSize = height * 0.08f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    textAlign = Paint.Align.CENTER
                }
                canvas.drawText(config.tickerChange, pillRect.centerX(), pillRect.centerY() + height * 0.028f, changeTextPaint)
            }

            com.example.model.CustomWidgetType.EVENT_COUNTDOWN -> {
                val eventPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = onContainerColor
                    textSize = height * 0.12f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                canvas.drawText("🗓 ${config.countdownEvent}", pad * 1.8f, height * 0.28f, eventPaint)

                val daysPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = primaryColor
                    textSize = height * 0.32f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                canvas.drawText("${config.countdownDays}", pad * 1.8f, height * 0.68f, daysPaint)

                val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = onContainerColor
                    alpha = 180
                    textSize = height * 0.10f
                    typeface = Typeface.DEFAULT
                }
                canvas.drawText("days remaining", pad * 1.8f + height * 0.45f, height * 0.65f, subPaint)
            }

            else -> {
                val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = onContainerColor
                    textSize = height * 0.14f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                canvas.drawText(config.title, pad * 1.8f, height * 0.48f, titlePaint)
            }
        }

        return bitmap
    }

    private fun drawScallop(canvas: Canvas, cx: Float, cy: Float, radius: Float, petals: Int, depth: Float, paint: Paint) {
        val path = Path()
        val points = 72
        val step = (2 * Math.PI / points).toFloat()

        for (i in 0..points) {
            val angle = i * step
            val modulation = 1f + depth * cos(petals * angle)
            val r = radius * modulation
            val x = cx + r * cos(angle)
            val y = cy + r * sin(angle)

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()
        canvas.drawPath(path, paint)
    }

    private val PaletteOption.onPrimaryColor: Long
        get() = 0xFFFFFFFF
}
