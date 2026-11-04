package app.hydronudge.reminders

import app.hydronudge.data.preferences.*
import java.time.ZonedDateTime

object ReminderPolicy {
    fun withinWakingHours(minute: Int, wake: Int, sleep: Int): Boolean = when {
        wake == sleep -> true // Equal endpoints explicitly mean all day.
        wake < sleep -> minute >= wake && minute < sleep
        else -> minute >= wake || minute < sleep
    }
    fun shouldRemind(settings: HydroSettings, now: ZonedDateTime, totalMl: Long, lastDrink: Long?): Boolean {
        val nowMs = now.toInstant().toEpochMilli()
        return settings.reminders && nowMs >= settings.pausedUntil &&
            withinWakingHours(now.hour*60+now.minute,settings.wakeMinute,settings.sleepMinute) &&
            (!settings.skipAtGoal || settings.goalMl <= 0 || totalMl < settings.goalMl) &&
            (lastDrink == null || nowMs - lastDrink >= settings.intervalMinutes * 60000L)
    }
    fun message(style: ReminderStyle, percent: Long): String = when(style) {
        ReminderStyle.GENTLE -> "HydroNudge check-in. Drink if you feel thirsty."
        ReminderStyle.NORMAL -> "HydroNudge check-in. Your water log is here if you want it."
        ReminderStyle.FUNNY -> "A quiet HydroNudge check-in. No need to log anything."
        ReminderStyle.MINIMAL -> "Optional water check-in"
    }
}
