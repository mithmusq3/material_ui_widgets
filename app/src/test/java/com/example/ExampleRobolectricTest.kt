package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.WidgetPresets
import com.example.data.WidgetRepository
import com.example.model.PaletteOption
import com.example.model.WidgetCategory
import com.example.model.WidgetShape
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Material You Widgets", appName)
  }

  @Test
  fun `verify widget presets have valid configs`() {
    val presets = WidgetPresets.allPresets
    assertTrue("Presets should not be empty", presets.isNotEmpty())

    val clockPreset = presets.first { it.category == WidgetCategory.CLOCKS }
    assertNotNull(clockPreset)
    assertEquals(WidgetShape.SCALLOP_FLOWER, clockPreset.defaultConfig.shape)

    val weatherPreset = presets.first { it.category == WidgetCategory.WEATHER }
    assertNotNull(weatherPreset)
    assertEquals("weather_pill_glance", weatherPreset.id)
  }

  @Test
  fun `repository can save and retrieve custom widget`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = WidgetRepository(context)

    val custom = WidgetPresets.allPresets.first().defaultConfig.copy(
      id = "custom_test_1",
      title = "Custom Sage Scallop",
      palette = PaletteOption.MEADOW_SAGE
    )

    repo.saveCustomWidget(custom)
    val savedList = repo.customWidgets.value
    assertTrue("Custom widget should be saved", savedList.any { it.id == "custom_test_1" })

    repo.toggleFavorite("music_pill_player")
    assertTrue("Favorite should contain music_pill_player", repo.favorites.value.contains("music_pill_player"))

    repo.deleteCustomWidget("custom_test_1")
    assertTrue("Custom widget should be removed", repo.customWidgets.value.none { it.id == "custom_test_1" })
  }

  @Test
  fun `repository can save and retrieve custom webpage and tracker widgets`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = WidgetRepository(context)

    val webWidget = com.example.model.WidgetConfig(
      id = "web_widget_1",
      title = "My News Feed",
      category = WidgetCategory.WEBPAGE,
      isUserCreated = true,
      customType = com.example.model.CustomWidgetType.WEBPAGE_BOOKMARK,
      webUrl = "https://news.ycombinator.com",
      webTitle = "Hacker News",
      webSubtitle = "Tech Headlines Live",
      palette = PaletteOption.SUNSET_CORAL
    )

    repo.saveCustomWidget(webWidget)
    val saved = repo.customWidgets.value.find { it.id == "web_widget_1" }
    assertNotNull(saved)
    assertEquals("https://news.ycombinator.com", saved?.webUrl)
    assertEquals("Hacker News", saved?.webTitle)
    assertEquals(com.example.model.CustomWidgetType.WEBPAGE_BOOKMARK, saved?.customType)

    // Test Checklist widget persistence
    val checklistWidget = com.example.model.WidgetConfig(
      id = "checklist_widget_1",
      title = "Daily Focus",
      category = WidgetCategory.CHECKLIST,
      isUserCreated = true,
      customType = com.example.model.CustomWidgetType.QUICK_CHECKLIST,
      checklistTitle = "Top 3 Goals",
      checklistItems = listOf("Morning run", "Read 20 pages", "Write code")
    )
    repo.saveCustomWidget(checklistWidget)
    val savedChecklist = repo.customWidgets.value.find { it.id == "checklist_widget_1" }
    assertNotNull(savedChecklist)
    assertEquals(3, savedChecklist?.checklistItems?.size)
    assertEquals("Morning run", savedChecklist?.checklistItems?.firstOrNull())
  }
}
