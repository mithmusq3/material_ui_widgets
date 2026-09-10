package com.example.data

import android.content.Context
import android.content.SharedPreferences
import android.os.BatteryManager
import com.example.model.PaletteOption
import com.example.model.WidgetCategory
import com.example.model.WidgetConfig
import com.example.model.WidgetFont
import com.example.model.WidgetShape
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WidgetRepository(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("pixel_widget_prefs", Context.MODE_PRIVATE)

    private val _customWidgets = MutableStateFlow<List<WidgetConfig>>(loadSavedWidgets())
    val customWidgets: StateFlow<List<WidgetConfig>> = _customWidgets.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(loadFavorites())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    fun getDeviceBatteryInfo(): Pair<Int, Boolean> {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
        val level = batteryManager?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: 85
        val isCharging = batteryManager?.isCharging == true
        return Pair(level, isCharging)
    }

    fun getFormattedDateTime(): Pair<String, String> {
        val timeFormat = SimpleDateFormat("h:mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        val now = Date()
        return Pair(timeFormat.format(now), dateFormat.format(now))
    }

    fun toggleFavorite(presetId: String) {
        val current = _favorites.value.toMutableSet()
        if (current.contains(presetId)) {
            current.remove(presetId)
        } else {
            current.add(presetId)
        }
        _favorites.value = current
        prefs.edit().putStringSet("favorite_ids", current).apply()
    }

    fun saveCustomWidget(config: WidgetConfig) {
        val list = _customWidgets.value.filter { it.id != config.id }.toMutableList()
        list.add(0, config)
        _customWidgets.value = list
        persistCustomWidgets(list)
    }

    fun deleteCustomWidget(widgetId: String) {
        val list = _customWidgets.value.filter { it.id != widgetId }
        _customWidgets.value = list
        persistCustomWidgets(list)
    }

    private fun persistCustomWidgets(list: List<WidgetConfig>) {
        val ids = list.map { it.id }.toSet()
        val editor = prefs.edit()
        editor.putStringSet("custom_widget_ids", ids)
        for (item in list) {
            editor.putString("widget_${item.id}_preset", item.presetId)
            editor.putString("widget_${item.id}_title", item.title)
            editor.putString("widget_${item.id}_category", item.category.name)
            editor.putString("widget_${item.id}_palette", item.palette.name)
            editor.putString("widget_${item.id}_shape", item.shape.name)
            editor.putString("widget_${item.id}_font", item.font.name)
            editor.putInt("widget_${item.id}_radius", item.cornerRadiusDp)
            editor.putString("widget_${item.id}_city", item.customCity)
            editor.putString("widget_${item.id}_quote", item.quoteText)
            editor.putString("widget_${item.id}_author", item.quoteAuthor)
            editor.putString("widget_${item.id}_song", item.songTitle)
            editor.putString("widget_${item.id}_artist", item.artistName)
            editor.putString("widget_${item.id}_custom_type", item.customType.name)
            editor.putString("widget_${item.id}_web_url", item.webUrl)
            editor.putString("widget_${item.id}_web_title", item.webTitle)
            editor.putString("widget_${item.id}_web_sub", item.webSubtitle)
            editor.putString("widget_${item.id}_counter_name", item.counterName)
            editor.putInt("widget_${item.id}_counter_cur", item.counterCurrent)
            editor.putInt("widget_${item.id}_counter_tar", item.counterTarget)
            editor.putString("widget_${item.id}_check_title", item.checklistTitle)
            editor.putString("widget_${item.id}_check_items", item.checklistItems.joinToString(";;;"))
            editor.putString("widget_${item.id}_check_checked", item.checklistChecked.joinToString(",") { it.toString() })
            editor.putString("widget_${item.id}_ticker_sym", item.tickerSymbol)
            editor.putString("widget_${item.id}_ticker_price", item.tickerPrice)
            editor.putString("widget_${item.id}_ticker_change", item.tickerChange)
            editor.putBoolean("widget_${item.id}_ticker_pos", item.tickerIsPositive)
            editor.putString("widget_${item.id}_cd_event", item.countdownEvent)
            editor.putInt("widget_${item.id}_cd_days", item.countdownDays)
            editor.putBoolean("widget_${item.id}_is_user", item.isUserCreated)
        }
        editor.apply()
    }

    private fun loadSavedWidgets(): List<WidgetConfig> {
        val ids = prefs.getStringSet("custom_widget_ids", emptySet()) ?: emptySet()
        val result = mutableListOf<WidgetConfig>()
        for (id in ids) {
            try {
                val presetId = prefs.getString("widget_${id}_preset", "clock_scallop") ?: "clock_scallop"
                val title = prefs.getString("widget_${id}_title", "My Custom Widget") ?: "My Custom Widget"
                val categoryStr = prefs.getString("widget_${id}_category", WidgetCategory.CLOCKS.name)
                val category = WidgetCategory.valueOf(categoryStr ?: WidgetCategory.CLOCKS.name)
                val paletteStr = prefs.getString("widget_${id}_palette", PaletteOption.SYSTEM_MONET.name)
                val palette = PaletteOption.valueOf(paletteStr ?: PaletteOption.SYSTEM_MONET.name)
                val shapeStr = prefs.getString("widget_${id}_shape", WidgetShape.SCALLOP_FLOWER.name)
                val shape = WidgetShape.valueOf(shapeStr ?: WidgetShape.SCALLOP_FLOWER.name)
                val fontStr = prefs.getString("widget_${id}_font", WidgetFont.PIXEL_SANS.name)
                val font = WidgetFont.valueOf(fontStr ?: WidgetFont.PIXEL_SANS.name)
                val radius = prefs.getInt("widget_${id}_radius", 32)
                val city = prefs.getString("widget_${id}_city", "San Francisco") ?: "San Francisco"
                val quote = prefs.getString("widget_${id}_quote", "Stay hungry, stay foolish.") ?: "Stay hungry, stay foolish."
                val author = prefs.getString("widget_${id}_author", "Steve Jobs") ?: "Steve Jobs"
                val song = prefs.getString("widget_${id}_song", "Starboy") ?: "Starboy"
                val artist = prefs.getString("widget_${id}_artist", "The Weeknd") ?: "The Weeknd"

                val customTypeStr = prefs.getString("widget_${id}_custom_type", com.example.model.CustomWidgetType.PIXEL_CLOCK.name)
                val customType = try {
                    com.example.model.CustomWidgetType.valueOf(customTypeStr ?: com.example.model.CustomWidgetType.PIXEL_CLOCK.name)
                } catch (e: Exception) { com.example.model.CustomWidgetType.PIXEL_CLOCK }

                val webUrl = prefs.getString("widget_${id}_web_url", "https://news.ycombinator.com") ?: "https://news.ycombinator.com"
                val webTitle = prefs.getString("widget_${id}_web_title", "Webpage") ?: "Webpage"
                val webSub = prefs.getString("widget_${id}_web_sub", "Live snapshot") ?: "Live snapshot"
                val counterName = prefs.getString("widget_${id}_counter_name", "Counter") ?: "Counter"
                val counterCur = prefs.getInt("widget_${id}_counter_cur", 5)
                val counterTar = prefs.getInt("widget_${id}_counter_tar", 8)
                val checkTitle = prefs.getString("widget_${id}_check_title", "Checklist") ?: "Checklist"
                val checkItemsStr = prefs.getString("widget_${id}_check_items", "Item 1;;;Item 2") ?: "Item 1;;;Item 2"
                val checkItems = checkItemsStr.split(";;;").filter { it.isNotBlank() }
                val checkCheckedStr = prefs.getString("widget_${id}_check_checked", "false,false") ?: "false,false"
                val checkChecked = checkCheckedStr.split(",").map { it.toBoolean() }
                val tickerSym = prefs.getString("widget_${id}_ticker_sym", "BTC / USD") ?: "BTC / USD"
                val tickerPrice = prefs.getString("widget_${id}_ticker_price", "$64,000") ?: "$64,000"
                val tickerChange = prefs.getString("widget_${id}_ticker_change", "+2.5%") ?: "+2.5%"
                val tickerPos = prefs.getBoolean("widget_${id}_ticker_pos", true)
                val cdEvent = prefs.getString("widget_${id}_cd_event", "Special Event") ?: "Special Event"
                val cdDays = prefs.getInt("widget_${id}_cd_days", 10)
                val isUser = prefs.getBoolean("widget_${id}_is_user", false)

                result.add(
                    WidgetConfig(
                        id = id,
                        presetId = presetId,
                        title = title,
                        category = category,
                        customType = customType,
                        palette = palette,
                        shape = shape,
                        font = font,
                        cornerRadiusDp = radius,
                        webUrl = webUrl,
                        webTitle = webTitle,
                        webSubtitle = webSub,
                        counterName = counterName,
                        counterCurrent = counterCur,
                        counterTarget = counterTar,
                        checklistTitle = checkTitle,
                        checklistItems = checkItems,
                        checklistChecked = checkChecked,
                        tickerSymbol = tickerSym,
                        tickerPrice = tickerPrice,
                        tickerChange = tickerChange,
                        tickerIsPositive = tickerPos,
                        countdownEvent = cdEvent,
                        countdownDays = cdDays,
                        customCity = city,
                        quoteText = quote,
                        quoteAuthor = author,
                        songTitle = song,
                        artistName = artist,
                        isUserCreated = isUser
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

    private fun loadFavorites(): Set<String> {
        return prefs.getStringSet("favorite_ids", setOf("clock_scallop", "weather_pill_glance", "battery_ring_status"))
            ?: setOf("clock_scallop", "weather_pill_glance")
    }
}
