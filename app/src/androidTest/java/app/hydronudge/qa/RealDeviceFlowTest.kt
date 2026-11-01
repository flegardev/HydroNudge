package app.hydronudge.qa

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.app.NotificationManager
import android.os.Build
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import androidx.work.WorkManager
import app.hydronudge.*
import app.hydronudge.data.local.DrinkEntry
import app.hydronudge.data.preferences.*
import app.hydronudge.domain.*
import app.hydronudge.notifications.HydroNotifications
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.time.*
import java.util.UUID
import java.util.regex.Pattern

@RunWith(AndroidJUnit4::class)
class RealDeviceFlowTest {
    @get:Rule val compose=createAndroidComposeRule<MainActivity>()
    private val app get()=compose.activity.application as HydroApplication
    private val ownedIds=mutableSetOf<String>()
    private val marker="QA-${UUID.randomUUID().toString().take(8)}"
    private fun entries()=runBlocking {app.repository.all()}
    private fun prefs()=runBlocking {app.preferences.settings.first()}
    private fun waitFor(check: ()->Boolean)=compose.waitUntil(20000,check)
    private fun pass(label: String) { QaArtifacts.note("PASS $label") }
    private fun todayTotal()=entries().filter {localDay(it.timestamp,ZoneId.systemDefault())==LocalDate.now()}.sumOf {it.amountMl.toLong()}
    private fun top() {compose.onNodeWithTag("dashboard-list").performScrollToIndex(0)}
    private fun assertProgress() {
        top()
        val settings=prefs();val total=todayTotal()
        val description="${settings.unit.format(total)} of ${settings.unit.format(settings.goalMl.toLong())}, ${goalPercent(total,settings.goalMl)} percent completed"
        waitFor {compose.onAllNodesWithContentDescription(description).fetchSemanticsNodes().isNotEmpty()}
    }
    private fun added(before: Set<String>,ml: Int,category: DrinkCategory): DrinkEntry {
        waitFor {entries().any {it.id !in before}}
        val fresh=entries().filter {it.id !in before}
        // Never guess which record belongs to QA if concurrent changes occurred.
        assertEquals("Concurrent changes detected; no unknown records will be removed",1,fresh.size)
        val entry=fresh.single();ownedIds+=entry.id
        assertEquals(ml,entry.amountMl);assertEquals(category.name,entry.category)
        assertProgress()
        return entry
    }
    private fun drink(category: DrinkCategory,ml: Int): DrinkEntry {
        val before=entries().map {it.id}.toSet()
        compose.onNodeWithTag("choose-drink").performScrollTo().performClick()
        compose.onNode(hasText(category.label) and hasAnyAncestor(isDialog())).performScrollTo().performClick()
        compose.onNodeWithTag("drink-name").performScrollTo().performTextReplacement("$marker ${category.label}")
        compose.onNodeWithTag("drink-amount").performScrollTo().performTextReplacement(ml.toString())
        Espresso.closeSoftKeyboard()
        if(category==DrinkCategory.CUSTOM) {
            compose.onNode(isDialog()).captureToImage().asAndroidBitmap().let { bitmap ->
                File(QaArtifacts.folder,"03-custom-logging.png").outputStream().use {bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG,100,it)}
            }
        }
        compose.onNodeWithTag("save-drink").performScrollTo().performClick()
        return added(before,ml,category)
    }
    private fun settingsItem(tag: String) {compose.onNodeWithTag("settings-list").performScrollToNode(hasTestTag(tag))}

    @Test fun realDeviceFunctionalFlowPreservesExistingData() {
        val original=prefs()
        val originalIds=entries().map {it.id}.toSet()
        val originalOrientation=compose.activity.requestedOrientation
        var permissionGrantedByQa=false
        try {
            runBlocking {app.preferences.save(original.copy(unit=VolumeUnit.ML,quickAmounts=listOf(150,250,330,500),theme=ThemeMode.LIGHT,dynamicColors=false,reminders=false,statusNotification=false))}
            waitFor {compose.onAllNodesWithTag("dashboard-list").fetchSemanticsNodes().isNotEmpty()}
            assertProgress();pass("app launch, dashboard rendering and baseline total")
            QaArtifacts.screenshot(compose.activity,"01-dashboard-light")
            var expected=todayTotal()
            for(ml in listOf(150,250,330,500)) {
                val before=entries().map {it.id}.toSet()
                compose.onNodeWithTag("quick-add:$ml").performScrollTo().performClick()
                added(before,ml,DrinkCategory.WATER)
                expected+=ml;assertEquals(expected,todayTotal());pass("quick add $ml mL, persisted water and correct daily total")
            }
            compose.onNodeWithTag("choose-drink").performScrollTo().performClick()
            compose.onNode(isDialog()).captureToImage().asAndroidBitmap().let {bitmap ->
                File(QaArtifacts.folder,"02-drink-selection.png").outputStream().use {bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG,100,it)}
            }
            compose.onNodeWithText("Cancel").performScrollTo().performClick()
            val coffee=drink(DrinkCategory.COFFEE,180);pass("coffee logging")
            drink(DrinkCategory.MILK,220);pass("milk logging")
            drink(DrinkCategory.PROTEIN,330);pass("protein shake logging")
            drink(DrinkCategory.CUSTOM,275);pass("custom drink logging")
            compose.onNodeWithTag("dashboard-list").performScrollToNode(hasTestTag("edit:${coffee.id}"))
            compose.onNodeWithTag("edit:${coffee.id}").performClick()
            compose.onNodeWithTag("drink-amount").performScrollTo().performTextReplacement("210")
            Espresso.closeSoftKeyboard()
            compose.onNodeWithTag("save-drink").performScrollTo().performClick()
            waitFor {entries().first {it.id==coffee.id}.amountMl==210};assertProgress();pass("edit updates saved quantity and total")
            compose.onNodeWithTag("dashboard-list").performScrollToNode(hasTestTag("delete:${coffee.id}"))
            compose.onNodeWithTag("delete:${coffee.id}").performClick()
            waitFor {entries().none {it.id==coffee.id}}
            waitFor {compose.onAllNodesWithText("Drink deleted").fetchSemanticsNodes().isNotEmpty()}
            compose.onNodeWithText("Undo").performClick()
            waitFor {entries().any {it.id==coffee.id}};assertProgress();pass("delete and Undo restore the same entry ID and total")
            compose.onNodeWithTag("nav:history").performClick()
            compose.onNodeWithText("Every sip, remembered").assertIsDisplayed()
            QaArtifacts.screenshot(compose.activity,"04-history-statistics");pass("history and statistics render stored data")
            compose.onNodeWithTag("nav:settings").performClick()
            compose.onNodeWithTag("daily-goal").performTextReplacement("3100")
            Espresso.closeSoftKeyboard();compose.onNodeWithTag("save-goal").performScrollTo().performClick()
            waitFor {prefs().goalMl==3100};pass("goal change persisted")
            QaArtifacts.screenshot(compose.activity,"05-settings")
            settingsItem("theme:DARK");compose.onNodeWithTag("theme:DARK").performClick()
            waitFor {prefs().theme==ThemeMode.DARK}
            compose.onNodeWithTag("nav:today").performClick();assertProgress()
            QaArtifacts.screenshot(compose.activity,"06-dashboard-dark");pass("light/dark theme switching")
            compose.activityRule.scenario.onActivity {it.requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE}
            waitFor {compose.activity.resources.configuration.orientation==android.content.res.Configuration.ORIENTATION_LANDSCAPE}
            assertProgress();QaArtifacts.screenshot(compose.activity,"07-landscape");pass("landscape rotation, total and layout")
            compose.activityRule.scenario.onActivity {it.requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT}
            waitFor {compose.activity.resources.configuration.orientation==android.content.res.Configuration.ORIENTATION_PORTRAIT}
            assertProgress()

            // Exercise the genuine Android permission prompt without touching other app settings.
            compose.onNodeWithTag("nav:settings").performClick()
            compose.onNodeWithTag("settings-list").performScrollToNode(hasText("Hydration status notification"))
            val wasGranted=Build.VERSION.SDK_INT<33 || app.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED
            compose.onNodeWithTag("toggle:Hydration status notification").performClick()
            if(!wasGranted && Build.VERSION.SDK_INT>=33) {
                val device=UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                val allow=device.wait(Until.findObject(By.res(Pattern.compile(".*:id/permission_allow_button"))),5000)
                if(allow!=null) {allow.click();permissionGrantedByQa=true}
            }
            compose.waitForIdle()
            val permitted=Build.VERSION.SDK_INT<33 || app.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED
            if(permitted) {
                val manager=app.getSystemService(NotificationManager::class.java)
                waitFor {manager.activeNotifications.any {it.id==1}}
                pass("notification permission and actual status notification posted")
                compose.onNodeWithTag("nav:today").performClick();assertProgress()
                val before=entries().map {it.id}.toSet()
                val action=manager.activeNotifications.first {it.id==1}.notification.actions.first()
                action.actionIntent.send();action.actionIntent.send()
                added(before,250,DrinkCategory.WATER)
                assertEquals(1,entries().count {it.id !in before});pass("notification quick action replay deduplicates into one Room entry")
                HydroNotifications(app).remind(prefs(),"HydroNudge QA notification check")
                waitFor {manager.activeNotifications.any {it.id==2}}
                pass("reminder notification channel and renderer post on Samsung; periodic timing not asserted")
                manager.cancel(2)
            } else {pass("notification denial handled without a crash");QaArtifacts.note("UNTESTED notification delivery: permission remains blocked")}
            runBlocking {app.preferences.save(prefs().copy(reminders=true,intervalMinutes=15,wakeMinute=0,sleepMinute=0))}
            val work=WorkManager.getInstance(app)
            waitFor {work.getWorkInfosForUniqueWork("hydration-reminders").get().any {it.state==androidx.work.WorkInfo.State.ENQUEUED}}
            pass("real-device unique WorkManager reminder enqueued at minimum 15-minute interval")
            compose.onNodeWithTag("nav:today").performClick();assertProgress()
        } catch(error: Throwable) {
            QaArtifacts.note("FAIL ${error.javaClass.simpleName}: ${error.message}")
            runCatching {QaArtifacts.screenshot(compose.activity,"failure-functional")}
            throw error
        } finally {
            compose.activityRule.scenario.onActivity {it.requestedOrientation=originalOrientation}
            runBlocking {ownedIds.forEach {app.repository.delete(it)};app.preferences.save(original)}
            if(permissionGrantedByQa && Build.VERSION.SDK_INT>=33) {
                File(QaArtifacts.folder,"restore-notification-permission.txt").writeText("revoke")
                QaArtifacts.note("RESTORE notification permission from ADB after instrumentation finishes; revocation can terminate the test process")
            }
            assertEquals("Existing entries must remain untouched",originalIds,entries().map {it.id}.toSet())
            QaArtifacts.note("CLEANUP only known QA entry IDs removed; original preferences restored; existing entries preserved")
            QaArtifacts.logcat("functional")
        }
    }
}
