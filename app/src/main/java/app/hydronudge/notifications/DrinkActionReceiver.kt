package app.hydronudge.notifications

import android.content.*
import app.hydronudge.HydroApplication
import app.hydronudge.data.local.DrinkEntry
import kotlinx.coroutines.launch

class DrinkActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == "app.hydronudge.DISMISS_STATUS") {
            val result=goAsync()
            val app=context.applicationContext as HydroApplication
            app.applicationScope.launch { try { app.preferences.dismissStatus() } finally { result.finish() } }
            return
        }
        if(intent.action != "app.hydronudge.ADD") return
        val token = intent.getStringExtra("token") ?: return
        val ml = intent.getIntExtra("ml",0)
        if(ml != 250 && ml != 500) return
        val result = goAsync()
        val app = context.applicationContext as HydroApplication
        app.applicationScope.launch {
            try { app.repository.add(DrinkEntry(id = token,name = "Water",category = "WATER",amountMl = ml)) }
            finally { result.finish() }
        }
    }
}
