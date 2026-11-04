package app.hydronudge.qa

import android.content.pm.ActivityInfo
import androidx.compose.ui.test.*
import app.hydronudge.data.preferences.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

/** Real native-window captures, including sheets and IME; fixture cleanup inherited. */
class EmulatorRedesignTest : OceanFixture() {
    @Test fun captureWaterOnlyScreensAndResponsiveLayouts() {
        percent(0);capture("01-dark-empty")
        seed(1750);percent(70);capture("02-dark-partial")
        runBlocking {app.repository.edit(records().single().copy(amountMl=2500))};percent(100);capture("03-dark-goal")
        runBlocking {app.repository.edit(records().single().copy(amountMl=3000))};percent(120);capture("04-dark-over")
        runBlocking {app.repository.edit(records().single().copy(amountMl=1750))}
        prefs(HydroSettings(theme=ThemeMode.LIGHT,dynamicColors=false));percent(70);capture("05-light")
        prefs(HydroSettings(theme=ThemeMode.DARK,dynamicColors=false));percent(70)
        tag("dashboard-list").performScrollToNode(hasTestTag("choose-drink"));tag("choose-drink").performClick();capture("06-sheet")
        tag("drink-amount").performClick();
        androidx.test.uiautomator.UiDevice.getInstance(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation()).waitForIdle();Thread.sleep(900);capture("07-sheet-keyboard")
        tag("save-drink").assertIsDisplayed()
        androidx.test.espresso.Espresso.closeSoftKeyboard();compose.onNodeWithText("Cancel").performClick()
        tag("dashboard-list").performScrollToNode(hasTestTag("drink:fixture"));capture("08-recent")
        tag("nav:history").performClick()
        for((range,name) in listOf("Today" to "09-history-today","7 days" to "10-history-week","30 days" to "11-history-thirty","Month" to "12-history-month")) {
            tag("history-list").performScrollToIndex(0);tag("history-range:$range").performScrollTo().performClick();capture(name)
            tag("history-list").performScrollToNode(hasContentDescription("Daily logged volumes",substring=true));capture("$name-chart")
        }
        tag("nav:settings").performClick();capture("13-settings")
        tag("settings-list").performScrollToNode(hasTestTag("toggle:Hydration status notification"));capture("14-notification-settings")
        tag("nav:today").performClick();top()
        val orientation=compose.activity.requestedOrientation
        val originalFont=QaArtifacts.shell("settings get system font_scale").trim()
        try {
            compose.activityRule.scenario.onActivity {it.requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE}
            waitFor {compose.activity.resources.configuration.orientation==android.content.res.Configuration.ORIENTATION_LANDSCAPE}
            top();capture("15-landscape");tag("dashboard-list").performScrollToNode(hasTestTag("quick-add:250"));tag("quick-add:250").assertIsDisplayed()
            compose.activityRule.scenario.onActivity {it.requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT}
            waitFor {compose.activity.resources.configuration.orientation==android.content.res.Configuration.ORIENTATION_PORTRAIT}
            for(scale in listOf(1.3f,1.5f,2f)) {
                QaArtifacts.shell("settings put system font_scale $scale")
                waitFor {kotlin.math.abs(compose.activity.resources.configuration.fontScale-scale)<0.02f}
                top();capture("16-font-$scale")
                tag("dashboard-list").performScrollToNode(hasTestTag("quick-add:500"));tag("quick-add:500").assertIsDisplayed()
                tag("dashboard-list").performScrollToNode(hasTestTag("choose-drink"));tag("choose-drink").performClick()
                tag("save-drink").assertIsDisplayed();capture("17-sheet-font-$scale")
                compose.onNodeWithText("Cancel").performClick()
            }
        } finally {
            QaArtifacts.shell(if(originalFont=="null") "settings delete system font_scale" else "settings put system font_scale $originalFont")
            compose.activityRule.scenario.onActivity {it.requestedOrientation=orientation}
        }
        QaArtifacts.note("PASS real water-only zero/partial/goal/over/light/dark/editor/IME/history/settings/landscape/font captures")
    }
}
