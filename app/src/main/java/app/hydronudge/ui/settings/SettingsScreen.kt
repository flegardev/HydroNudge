package app.hydronudge.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import app.hydronudge.data.preferences.*
import app.hydronudge.domain.VolumeUnit
import app.hydronudge.domain.parseQuickAmounts
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(settings: HydroSettings, save: (HydroSettings)->Unit, clear: ()->Unit, export: (Uri)->Unit, import: (Uri)->Unit) {
    var goal by rememberSaveable(settings.goalMl) { mutableStateOf(settings.unit.input(settings.goalMl)) }
    var quick by rememberSaveable(settings.quickAmounts) { mutableStateOf(settings.quickAmounts.joinToString(", ") { settings.unit.input(it) }) }
    var confirm by rememberSaveable { mutableStateOf(false) }
    var confirmImport by rememberSaveable { mutableStateOf(false) }
    val exportPicker = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { it?.let(export) }
    val importPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { it?.let(import) }
    var tileAmount by rememberSaveable(settings.tileAmountMl,settings.unit) { mutableStateOf(settings.unit.input(settings.tileAmountMl)) }
    val tileMl = tileAmount.replace(',','.').toDoubleOrNull()?.takeIf { it.isFinite() && it > 0 && it <= 20000/settings.unit.millilitres }?.let(settings.unit::toMl)
    val goalMl = goal.replace(',', '.').toDoubleOrNull()?.takeIf { it.isFinite() && it > 0 && it <= 20000 / settings.unit.millilitres }?.let(settings.unit::toMl)
    val amounts = parseQuickAmounts(quick,settings.unit)
    LazyColumn(modifier=Modifier.testTag("settings-list"),contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { Text("Your rhythm", style = MaterialTheme.typography.headlineLarge); Text("Make HydroNudge feel like you.") }
        item { Text("Hydration", style = MaterialTheme.typography.titleLarge) }
        item { OutlinedTextField(goal, { goal = it }, label = { Text("Daily goal (${settings.unit.label})") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true, modifier = Modifier.fillMaxWidth().testTag("daily-goal")) }
        item { Text("2,500 mL is an editable starting point, not a medical prescription. Needs vary with your body, activity and health.", style = MaterialTheme.typography.bodySmall) }
        item { Button(onClick = { goalMl?.let { save(settings.copy(goalMl = it)) } }, enabled = goalMl != null && goalMl in 100..20000,modifier=Modifier.testTag("save-goal")) { Text("Save goal") } }
        item { FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { VolumeUnit.entries.forEach { u -> FilterChip(selected = settings.unit == u, onClick = { save(settings.copy(unit = u)); goal = u.input(settings.goalMl); quick = settings.quickAmounts.joinToString(", ") { u.input(it) } }, label = { Text(u.label) }) } } }
        item { OutlinedTextField(quick, { quick = it }, label = { Text("Quick-add amounts (${settings.unit.label})") }, supportingText={Text("Enter four positive amounts separated by commas. Use a dot for decimals.")},isError=amounts==null,modifier = Modifier.fillMaxWidth()) }
        item { Button(onClick = { amounts?.let {save(settings.copy(quickAmounts = it))} }, enabled = amounts!=null) { Text("Save quick amounts") } }
        item { NotificationSettings(settings,save) }
        item { Text("Home screen & Quick Settings",style=MaterialTheme.typography.titleLarge); Text("Add the HydroNudge widget from your launcher’s widget picker. Resize it for drink buttons. Add ‘Log a drink’ in Android’s Quick Settings editor; tapping logs water after unlocking.",style=MaterialTheme.typography.bodyMedium) }
        item { OutlinedTextField(tileAmount,{tileAmount=it},label={Text("Quick Settings serving (${settings.unit.label})")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal),singleLine=true,modifier=Modifier.fillMaxWidth()) }
        item { Button(onClick={tileMl?.let {save(settings.copy(tileAmountMl=it))}},enabled=tileMl != null && tileMl in 1..20000) {Text("Save tile amount")} }
        item { Text("Appearance", style = MaterialTheme.typography.titleLarge) }
        item { FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { ThemeMode.entries.forEach { theme -> FilterChip(modifier=Modifier.testTag("theme:${theme.name}"),selected = settings.theme == theme, onClick = { save(settings.copy(theme = theme)) }, label = { Text(theme.name.lowercase().replaceFirstChar { it.uppercase() }) }) } } }
        item { Row { Switch(settings.dynamicColors, { save(settings.copy(dynamicColors = it)) }); Text("Use device colors (Android 12+)", Modifier.padding(12.dp)) } }
        item { Text("Your data", style = MaterialTheme.typography.titleLarge); Text("Everything stays on this device. Android cloud backup is disabled. No ads, subscriptions, accounts, analytics or tracking.") }
        item { Text("Backups include all drink records and favorites. Preferences stay on this device. Import merges new IDs and keeps existing records. Use a private location; a chosen document provider may use cloud storage.",style=MaterialTheme.typography.bodySmall) }
        item { FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick={exportPicker.launch("HydroNudge-${java.time.LocalDate.now()}.json")}) {Text("Export drink backup")}
            OutlinedButton(onClick={confirmImport=true}) {Text("Import drink backup")}
        } }
        item { OutlinedButton(onClick = { confirm = true }) { Text("Delete all local data", color = MaterialTheme.colorScheme.error) } }
        item { Text("HydroNudge · 1.0.0\nStay hydrated. Stay free.", style = MaterialTheme.typography.titleMedium) }
        item { Text("Built with Kotlin and Android Jetpack. Open-source libraries are licensed by their respective authors. See the repository’s release documentation for notices.", style = MaterialTheme.typography.bodySmall) }
    }
    if(confirm) AlertDialog(onDismissRequest = { confirm = false }, title = { Text("Delete all local data?") }, text = { Text("Drinks and favorites will be permanently removed. Preferences will reset and reminders will turn off. Exported files must be deleted separately.") }, confirmButton = { TextButton(onClick = { clear(); confirm = false }) { Text("Delete all data") } }, dismissButton = { TextButton(onClick = { confirm = false }) { Text("Keep my data") } })
    if(confirmImport) AlertDialog(onDismissRequest={confirmImport=false},title={Text("Merge a drink backup?")},text={Text("New drink records and favorites will be added. Records with an existing ID are kept unchanged. Invalid files are rejected before anything is written. Safe import limit: 32 MiB.")},confirmButton={TextButton(onClick={confirmImport=false;importPicker.launch(arrayOf("application/json","text/plain","application/octet-stream"))}) {Text("Choose backup")}},dismissButton={TextButton(onClick={confirmImport=false}) {Text("Cancel")}})
}
