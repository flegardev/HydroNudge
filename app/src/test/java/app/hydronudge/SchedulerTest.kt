package app.hydronudge

import android.app.Application
import androidx.work.*
import androidx.work.testing.*
import app.hydronudge.data.preferences.HydroSettings
import app.hydronudge.reminders.ReminderScheduler
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk=[35],application=Application::class)
class SchedulerTest {
    @Test fun uniqueScheduleUpdatesIntervalAndCancelsWhenDisabled() {
        val context=RuntimeEnvironment.getApplication()
        WorkManagerTestInitHelper.initializeTestWorkManager(context,Configuration.Builder().setExecutor(SynchronousExecutor()).build())
        val manager=WorkManager.getInstance(context)
        try {
            ReminderScheduler.apply(context,HydroSettings(reminders=true,intervalMinutes=30))
            var requests=manager.getWorkInfosForUniqueWork("hydration-reminders").get()
            assertEquals(1,requests.count { it.state == WorkInfo.State.ENQUEUED })
            assertEquals(30*60000L,requests.first().periodicityInfo!!.repeatIntervalMillis)
            ReminderScheduler.apply(context,HydroSettings(reminders=true,intervalMinutes=45))
            requests=manager.getWorkInfosForUniqueWork("hydration-reminders").get()
            assertEquals(1,requests.count { it.state == WorkInfo.State.ENQUEUED })
            assertEquals(45*60000L,requests.first().periodicityInfo!!.repeatIntervalMillis)
            ReminderScheduler.apply(context,HydroSettings(reminders=false))
            assertTrue(manager.getWorkInfosForUniqueWork("hydration-reminders").get().all { it.state == WorkInfo.State.CANCELLED })
        } finally { WorkManagerTestInitHelper.closeWorkDatabase() }
    }
}
