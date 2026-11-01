package app.hydronudge.reminders

import android.content.Context
import android.content.ComponentName
import android.appwidget.AppWidgetManager
import app.hydronudge.widgets.HydroWidgetReceiver
import androidx.work.*
import app.hydronudge.data.preferences.HydroSettings
import java.util.concurrent.TimeUnit

object ReminderScheduler {
    fun apply(context: Context, settings: HydroSettings) {
        val manager = WorkManager.getInstance(context)
        if(settings.reminders) {
            val request = PeriodicWorkRequestBuilder<ReminderWorker>(settings.intervalMinutes.toLong(),TimeUnit.MINUTES)
                .setInitialDelay(settings.intervalMinutes.toLong(),TimeUnit.MINUTES).build()
            manager.enqueueUniquePeriodicWork("hydration-reminders",ExistingPeriodicWorkPolicy.UPDATE,request)
        } else manager.cancelUniqueWork("hydration-reminders")
        val hasWidgets = AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context,HydroWidgetReceiver::class.java)).isNotEmpty()
        if((settings.statusNotification && !settings.statusDismissed) || hasWidgets) manager.enqueueUniquePeriodicWork("hydration-surfaces",
            ExistingPeriodicWorkPolicy.KEEP,PeriodicWorkRequestBuilder<SurfaceWorker>(15,TimeUnit.MINUTES).build())
        else manager.cancelUniqueWork("hydration-surfaces")
    }
}
