package app.hydronudge.ui.settings

import android.Manifest
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.text.format.DateFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import app.hydronudge.data.preferences.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import app.hydronudge.HydroApplication
import kotlinx.coroutines.launch

@Composable
fun SettingToggle(label: String, checked: Boolean, change: (Boolean)->Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f)); Switch(checked,change,modifier=Modifier.testTag("toggle:$label"))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NotificationSettings(value: HydroSettings, save: (HydroSettings)->Unit) {
    val context = LocalContext.current
    var granted by remember { mutableStateOf(NotificationManagerCompat.from(context).areNotificationsEnabled()) }
    val request = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        granted = it
        if(it) (context.applicationContext as HydroApplication).let { app -> app.applicationScope.launch { app.refreshSurfaces() } }
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { granted=NotificationManagerCompat.from(context).areNotificationsEnabled() }
    fun askIfNeeded() {
        if(Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) request.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
    var interval by rememberSaveable(value.intervalMinutes) { mutableStateOf(value.intervalMinutes.toString()) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("A nudge, on your terms", style = MaterialTheme.typography.titleLarge)
        SettingToggle("Drink reminders",value.reminders) { save(value.copy(reminders=it)); if(it) askIfNeeded() }
        Text("Best-effort reminders, never exact alarms. Android may delay them to save battery. Recent drinks reset the check; missed reminders are not replayed.", style = MaterialTheme.typography.bodySmall)
        if(value.reminders) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) { listOf(15,30,45,60,90,120).forEach { minutes ->
                FilterChip(selected = value.intervalMinutes == minutes, onClick = { save(value.copy(intervalMinutes=minutes)) }, label = { Text("$minutes min") })
            } }
            OutlinedTextField(interval,{ interval=it },label = { Text("Custom interval (15–1440 minutes)") },keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Number),singleLine=true,modifier=Modifier.fillMaxWidth())
            Button(onClick = { interval.toIntOrNull()?.let { save(value.copy(intervalMinutes=it)) } },enabled=interval.toIntOrNull() in 15..1440) { Text("Save interval") }
            Text("Waking hours · reminders are quiet outside this window",style=MaterialTheme.typography.labelLarge)
            fun timeLabel(minute: Int) = "%02d:%02d".format(minute/60,minute%60)
            FlowRow {
                OutlinedButton(onClick = { TimePickerDialog(context,{ _,h,m -> save(value.copy(wakeMinute=h*60+m)) },value.wakeMinute/60,value.wakeMinute%60,DateFormat.is24HourFormat(context)).show() }) { Text("Wake ${timeLabel(value.wakeMinute)}") }
                OutlinedButton(onClick = { TimePickerDialog(context,{ _,h,m -> save(value.copy(sleepMinute=h*60+m)) },value.sleepMinute/60,value.sleepMinute%60,DateFormat.is24HourFormat(context)).show() }) { Text("Sleep ${timeLabel(value.sleepMinute)}") }
            }
            Text("Matching wake and sleep times allow reminders all day. Overnight schedules are supported.", style=MaterialTheme.typography.bodySmall)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) { ReminderStyle.entries.forEach { style ->
                FilterChip(selected=value.style==style,onClick={ save(value.copy(style=style)) },label={ Text(if(style==ReminderStyle.FUNNY) "Aggressive / funny" else style.name.lowercase().replaceFirstChar { it.uppercase() }) })
            } }
            SettingToggle("Reminder sound",value.sound) { save(value.copy(sound=it)) }
            SettingToggle("Reminder vibration",value.vibration) { save(value.copy(vibration=it)) }
            SettingToggle("Skip reminders after reaching my goal",value.skipAtGoal) { save(value.copy(skipAtGoal=it)) }
            if(value.pausedUntil > System.currentTimeMillis()) {
                Text("Paused until ${Instant.ofEpochMilli(value.pausedUntil).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("EEE HH:mm"))}")
                OutlinedButton(onClick={ save(value.copy(pausedUntil=0)) }) { Text("Resume reminders") }
            } else OutlinedButton(onClick={ save(value.copy(pausedUntil=System.currentTimeMillis()+2*3600000)) }) { Text("Pause for 2 hours") }
        }
        HorizontalDivider()
        SettingToggle("Hydration status notification",value.statusNotification) { save(value.copy(statusNotification=it,statusDismissed=false)); if(it) askIfNeeded() }
        if(value.statusNotification && value.statusDismissed) {
            Text("Status dismissed. It stays hidden until you choose to show it again.")
            OutlinedButton(onClick={save(value.copy(statusDismissed=false))}) { Text("Show status again") }
        }
        Text("Shows today’s total with +250 mL and +500 mL actions. Android can allow dismissal. Updates follow recorded changes and periodic background refresh; midnight refresh may be delayed. Lock-screen visibility is yours to control.",style=MaterialTheme.typography.bodySmall)
        if(!granted && (value.reminders || value.statusNotification)) Text("Notifications are blocked. Allow them in Android settings to receive nudges or use notification actions.",color=MaterialTheme.colorScheme.error)
        TextButton(onClick={ context.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE,context.packageName)) }) { Text("Android notification settings") }
        Text("Android notification channels and Do Not Disturb have the final say over sound and vibration.",style=MaterialTheme.typography.bodySmall)
    }
}
