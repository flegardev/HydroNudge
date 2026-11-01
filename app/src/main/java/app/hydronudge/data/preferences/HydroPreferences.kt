package app.hydronudge.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import app.hydronudge.domain.VolumeUnit
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.hydroDataStore by preferencesDataStore("hydronudge_preferences")
enum class ThemeMode { SYSTEM, LIGHT, DARK }
enum class ReminderStyle { GENTLE, NORMAL, FUNNY, MINIMAL }
data class HydroSettings(
    val goalMl: Int = 2500,
    val unit: VolumeUnit = VolumeUnit.ML,
    val quickAmounts: List<Int> = listOf(150, 250, 330, 500),
    val theme: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColors: Boolean = false,
    val reminders: Boolean = false,
    val intervalMinutes: Int = 60,
    val wakeMinute: Int = 8 * 60,
    val sleepMinute: Int = 22 * 60,
    val style: ReminderStyle = ReminderStyle.GENTLE,
    val sound: Boolean = false,
    val vibration: Boolean = false,
    val skipAtGoal: Boolean = true,
    val pausedUntil: Long = 0,
    val statusNotification: Boolean = false,
    val statusDismissed: Boolean = false,
    val tileAmountMl: Int = 250
)
class HydroPreferences(private val context: Context) {
    private val goal = intPreferencesKey("goal_ml")
    private val unit = stringPreferencesKey("unit")
    private val quick = stringPreferencesKey("quick_amounts_ml")
    private val theme = stringPreferencesKey("theme")
    private val dynamic = booleanPreferencesKey("dynamic_colors")
    private val reminders = booleanPreferencesKey("reminders")
    private val interval = intPreferencesKey("interval_minutes")
    private val wake = intPreferencesKey("wake_minute")
    private val sleep = intPreferencesKey("sleep_minute")
    private val style = stringPreferencesKey("reminder_style")
    private val sound = booleanPreferencesKey("sound")
    private val vibration = booleanPreferencesKey("vibration")
    private val skip = booleanPreferencesKey("skip_at_goal")
    private val paused = longPreferencesKey("paused_until")
    private val status = booleanPreferencesKey("status_notification")
    private val dismissed = booleanPreferencesKey("status_dismissed")
    private val tileAmount = intPreferencesKey("tile_amount_ml")
    val settings = context.hydroDataStore.data.catch { if (it is IOException) emit(emptyPreferences()) else throw it }.map { p ->
        HydroSettings(
            goalMl = p[goal]?.takeIf { it in 100..20000 } ?: 2500,
            unit = VolumeUnit.entries.firstOrNull { it.name == p[unit] } ?: VolumeUnit.ML,
            quickAmounts = p[quick]?.split(",")?.mapNotNull { it.toIntOrNull()?.takeIf { n -> n in 1..20000 } }?.takeIf { it.size == 4 } ?: listOf(150,250,330,500),
            theme = ThemeMode.entries.firstOrNull { it.name == p[theme] } ?: ThemeMode.SYSTEM,
            dynamicColors = p[dynamic] ?: false,
            reminders = p[reminders] ?: false,
            intervalMinutes = p[interval]?.coerceIn(15,1440) ?: 60,
            wakeMinute = p[wake]?.coerceIn(0,1439) ?: 480,
            sleepMinute = p[sleep]?.coerceIn(0,1439) ?: 1320,
            style = ReminderStyle.entries.firstOrNull { it.name == p[style] } ?: ReminderStyle.GENTLE,
            sound = p[sound] ?: false,
            vibration = p[vibration] ?: false,
            skipAtGoal = p[skip] ?: true,
            pausedUntil = p[paused] ?: 0,
            statusNotification = p[status] ?: false,
            statusDismissed = p[dismissed] ?: false,
            tileAmountMl = p[tileAmount]?.coerceIn(1,20000) ?: 250
        )
    }
    suspend fun save(value: HydroSettings) {
        require(value.goalMl in 100..20000)
        require(value.quickAmounts.size == 4 && value.quickAmounts.all { it in 1..20000 })
        require(value.intervalMinutes in 15..1440 && value.wakeMinute in 0..1439 && value.sleepMinute in 0..1439)
        require(value.tileAmountMl in 1..20000)
        context.hydroDataStore.edit { p ->
            p[goal] = value.goalMl; p[unit] = value.unit.name
            p[quick] = value.quickAmounts.joinToString(","); p[theme] = value.theme.name
            p[dynamic] = value.dynamicColors
            p[reminders] = value.reminders; p[interval] = value.intervalMinutes
            p[wake] = value.wakeMinute; p[sleep] = value.sleepMinute; p[style] = value.style.name
            p[sound] = value.sound; p[vibration] = value.vibration; p[skip] = value.skipAtGoal
            p[paused] = value.pausedUntil; p[status] = value.statusNotification; p[tileAmount] = value.tileAmountMl
            p[dismissed] = value.statusDismissed
        }
    }
    suspend fun dismissStatus() { context.hydroDataStore.edit { it[dismissed] = true } }
}
