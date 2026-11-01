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
            (!settings.skipAtGoal || totalMl < settings.goalMl) &&
            (lastDrink == null || nowMs - lastDrink >= settings.intervalMinutes * 60000L)
    }
    fun message(style: ReminderStyle, percent: Long): String = when(style) {
        ReminderStyle.GENTLE -> "A gentle check-in. Fancy a sip?"
        ReminderStyle.NORMAL -> "Hydration check! You’re at $percent% of your goal today."
        ReminderStyle.FUNNY -> "Your drink is waiting. It has no other plans."
        ReminderStyle.MINIMAL -> "Time for a drink?"
    }
}
