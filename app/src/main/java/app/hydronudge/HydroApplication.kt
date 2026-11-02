package app.hydronudge

import android.app.Application
import androidx.room.Room
import app.hydronudge.data.DrinkRepository
import app.hydronudge.data.local.HydroDatabase
import app.hydronudge.data.preferences.HydroPreferences
import app.hydronudge.notifications.HydroNotifications
import app.hydronudge.reminders.ReminderScheduler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import app.hydronudge.widgets.HydroWidget
import androidx.glance.appwidget.updateAll

class HydroApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, error -> android.util.Log.e("HydroNudge","Background update failed",error) })
    private val surfaceMutex = Mutex()
    val database by lazy { Room.databaseBuilder(this, HydroDatabase::class.java, "hydronudge.db").build() }
    val repository by lazy { DrinkRepository(database.drinks(), { applicationScope.launch { refreshSurfaces() } }) }
    val preferences by lazy { HydroPreferences(this) }
    override fun onCreate() {
        super.onCreate()
        applicationScope.launch { preferences.settings.distinctUntilChanged().collect { settings ->
            ReminderScheduler.apply(this@HydroApplication,settings)
            refreshSurfaces()
        } }
    }
    suspend fun refreshSurfaces() { surfaceMutex.withLock { HydroNotifications(this).refresh(); HydroWidget().updateAll(this) } }
}
