package app.hydronudge.quicksettings

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import app.hydronudge.HydroApplication
import app.hydronudge.data.local.DrinkEntry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class HydrationTile : TileService() {
    private val busy = AtomicBoolean(false)
    private var lastTap = 0L
    override fun onStartListening() {
        super.onStartListening()
        val app = application as HydroApplication
        app.applicationScope.launch {
            val settings = app.preferences.settings.first()
            qsTile?.apply { label="Log ${settings.unit.format(settings.tileAmountMl.toLong())}"; state=Tile.STATE_ACTIVE; updateTile() }
        }
    }
    override fun onClick() {
        super.onClick()
        if(isLocked) unlockAndRun { logDrink() } else logDrink()
    }
    private fun logDrink() {
        val now = android.os.SystemClock.elapsedRealtime()
        if(now-lastTap < 1000 || !busy.compareAndSet(false,true)) return
        lastTap=now
        val app = application as HydroApplication
        app.applicationScope.launch {
            try {
                val settings = app.preferences.settings.first()
                app.repository.add(DrinkEntry(name="Water",category="WATER",amountMl=settings.tileAmountMl))
                qsTile?.apply { label="Drink added"; state=Tile.STATE_ACTIVE; updateTile() }
            } finally { busy.set(false) }
        }
    }
}
