package app.hydronudge

import app.hydronudge.data.preferences.HydroSettings
import app.hydronudge.reminders.ReminderPolicy
import org.junit.Assert.*
import org.junit.Test
import java.time.ZonedDateTime

class ReminderPolicyTest {
    private val now = ZonedDateTime.parse("2026-10-31T12:00:00+01:00[Europe/Ljubljana]")
    @Test fun wakingHoursAndQuietHours() { assertTrue(ReminderPolicy.withinWakingHours(480,480,1320)); assertFalse(ReminderPolicy.withinWakingHours(1320,480,1320)); assertFalse(ReminderPolicy.withinWakingHours(100,480,1320)) }
    @Test fun overnightAndAllDay() { assertTrue(ReminderPolicy.withinWakingHours(60,1320,480)); assertFalse(ReminderPolicy.withinWakingHours(600,1320,480)); assertTrue(ReminderPolicy.withinWakingHours(600,0,0)) }
    @Test fun disabledPausedGoalAndRecentDrinkSkip() {
        val settings = HydroSettings(reminders=true)
        assertTrue(ReminderPolicy.shouldRemind(settings,now,500,null))
        assertFalse(ReminderPolicy.shouldRemind(settings.copy(reminders=false),now,500,null))
        assertFalse(ReminderPolicy.shouldRemind(settings.copy(pausedUntil=now.toInstant().toEpochMilli()+60000),now,500,null))
        assertFalse(ReminderPolicy.shouldRemind(settings,now,2500,null))
        assertFalse(ReminderPolicy.shouldRemind(settings,now,500,now.minusMinutes(59).toInstant().toEpochMilli()))
        assertTrue(ReminderPolicy.shouldRemind(settings,now,500,now.minusMinutes(60).toInstant().toEpochMilli()))
        assertTrue(ReminderPolicy.shouldRemind(settings.copy(skipAtGoal=false),now,2600,null))
    }
}
