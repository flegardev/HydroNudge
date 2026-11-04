package app.hydronudge.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import app.hydronudge.ui.components.*
import androidx.compose.ui.Alignment
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
    var goal by rememberSaveable(settings.goalMl,settings.unit) { mutableStateOf(if(settings.goalMl > 0) settings.unit.input(settings.goalMl) else "") }
    var quick by rememberSaveable(settings.quickAmounts,settings.unit) { mutableStateOf(settings.quickAmounts.joinToString(", ") { settings.unit.input(it) }) }
    var confirm by rememberSaveable { mutableStateOf(false) }
    var confirmImport by rememberSaveable { mutableStateOf(false) }
    var legalDocument by rememberSaveable { mutableStateOf<String?>(null) }
    var help by rememberSaveable { mutableStateOf<String?>(null) }
    val exportPicker = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { it?.let(export) }
    val importPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { it?.let(import) }
    var tileAmount by rememberSaveable(settings.tileAmountMl,settings.unit) { mutableStateOf(settings.unit.input(settings.tileAmountMl)) }
    val tileMl = tileAmount.replace(',','.').toDoubleOrNull()?.takeIf { it.isFinite() && it > 0 && it <= 20000/settings.unit.millilitres }?.let(settings.unit::toMl)
    val goalMl = goal.replace(',', '.').toDoubleOrNull()?.takeIf { it.isFinite() && it > 0 && it <= 20000 / settings.unit.millilitres }?.let(settings.unit::toMl)
    val amounts = parseQuickAmounts(quick,settings.unit)
    LazyColumn(modifier=Modifier.testTag("settings-list"),contentPadding=PaddingValues(20.dp),verticalArrangement=Arrangement.spacedBy(24.dp)) {
        item { Text("Settings",style=MaterialTheme.typography.headlineLarge) }
        item {
            HydroSettingsGroup("Daily goal",HydroIcons.Drop) {
                FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                    VolumeUnit.entries.forEach { u -> FilterChip(selected=settings.unit==u,onClick={save(settings.copy(unit=u));goal=if(settings.goalMl>0) u.input(settings.goalMl) else "";quick=settings.quickAmounts.joinToString(", ") {u.input(it)}},label={Text(u.label)}) }
                }
                OutlinedTextField(goal,{goal=it},label={Text("Daily water goal (${settings.unit.label})")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal),singleLine=true,isError=goalMl==null || goalMl !in 100..20000,modifier=Modifier.fillMaxWidth().testTag("daily-goal"))
                Text("A goal is optional and is not medical advice. Needs vary. For a child, a parent or caregiver should choose any goal with appropriate professional guidance.",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
                Button(onClick={goalMl?.let {save(settings.copy(goalMl=it))}},enabled=goalMl!=null && goalMl in 100..20000,modifier=Modifier.testTag("save-goal")) {Text("Save goal")}
                if(settings.goalMl > 0) TextButton(onClick={save(settings.copy(goalMl=0));goal=""},modifier=Modifier.testTag("clear-goal")) {Text("Remove daily goal")}

            }
        }
        item {
            HydroSettingsGroup("Appearance",HydroIcons.Settings) {
                FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                    ThemeMode.entries.forEach { theme -> FilterChip(modifier=Modifier.testTag("theme:${theme.name}"),selected=settings.theme==theme,onClick={save(settings.copy(theme=theme))},label={Text(theme.name.lowercase().replaceFirstChar {it.uppercase()})}) }
                }
                SettingToggle("Use device colors (Android 12+)",settings.dynamicColors) {save(settings.copy(dynamicColors=it))}
            }
        }
        item { NotificationSettings(settings,save) }
        item {
            HydroSettingsGroup("Quick add & tile",HydroIcons.Plus) {
                SettingsDisclosure("Customize quick add", "settings:quick-add") {
                    OutlinedTextField(quick,{quick=it},label={Text("Quick-add amounts (${settings.unit.label})")},supportingText={Text("Four positive amounts, separated by commas. Use a dot for decimals.")},isError=amounts==null,modifier=Modifier.fillMaxWidth())
                    Button(onClick={amounts?.let {save(settings.copy(quickAmounts=it))}},enabled=amounts!=null) {Text("Save quick amounts")}
                }
                SettingsDisclosure("Quick Settings serving", "settings:tile") {
                    OutlinedTextField(tileAmount,{tileAmount=it},label={Text("Quick Settings serving (${settings.unit.label})")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal),singleLine=true,isError=tileMl==null || tileMl !in 1..20000,modifier=Modifier.fillMaxWidth())
                    Button(onClick={tileMl?.let {save(settings.copy(tileAmountMl=it))}},enabled=tileMl!=null && tileMl in 1..20000) {Text("Save tile amount")}
                }
                TextButton(onClick={help="home"}) {Text("Widget & tile setup")}
            }
        }
        item {
            HydroSettingsGroup("Data and privacy",HydroIcons.Settings) {
                Text("Local records. No accounts, ads or subscriptions.")
                Text("Your chosen document provider may use cloud storage.",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
                FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick={exportPicker.launch("HydroNudge-${java.time.LocalDate.now()}.json")}) {Text("Export full backup")}
                    OutlinedButton(onClick={confirmImport=true}) {Text("Import backup")}
                }
                TextButton(onClick={help="backup"}) {Text("How backups work")}
                TextButton(onClick={confirm=true}) {Text("Delete all local data",color=MaterialTheme.colorScheme.error)}
            }
        }
        item { HydroSettingsGroup("About HydroNudge",HydroIcons.Drop) {
            Text("HydroNudge · 1.0.0",style=MaterialTheme.typography.titleMedium)
            TextButton(onClick={legalDocument="privacy-policy.txt"}) { Text("Privacy policy") }
            TextButton(onClick={legalDocument="third-party-notices.txt"}) { Text("Third-party licenses") }
        } }
    }
    help?.let { topic ->
        AlertDialog(
            onDismissRequest={help=null},
            title={Text(if(topic=="backup") "How backups work" else "Widget & tile setup")},
            text={Text(
                if(topic=="backup") "Android cloud backup is disabled. Exports are unencrypted JSON and include water, preserved legacy records and saved servings. Preferences stay on this device. Only water counts in totals. Import merges new IDs and keeps existing records. Your chosen document provider may use cloud storage. Deleting app data does not remove exported files. Keep a private copy before changing devices."
                else "Add HydroNudge from your launcher's widget picker. Resize it for water buttons. Add Log water in Android's Quick Settings editor; tapping logs your chosen serving after unlocking.",
                modifier=Modifier.heightIn(max=400.dp).verticalScroll(rememberScrollState())
            )},
            confirmButton={TextButton(onClick={help=null}) {Text("Close")}}
        )
    }
    legalDocument?.let { asset ->
        LegalDocumentDialog(if(asset=="privacy-policy.txt") "Privacy policy" else "Third-party licenses",asset) { legalDocument=null }
    }
    if(confirm) AlertDialog(onDismissRequest = { confirm = false }, title = { Text("Delete all local data?") }, text = { Text("Water and preserved legacy records will be permanently removed. Preferences will reset and reminders will turn off. Exported files must be deleted separately.") }, confirmButton = { TextButton(onClick = { clear(); confirm = false }) { Text("Delete all data") } }, dismissButton = { TextButton(onClick = { confirm = false }) { Text("Keep my data") } })
    if(confirmImport) AlertDialog(onDismissRequest={confirmImport=false},title={Text("Merge a backup?")},text={Text("Water and legacy records will be preserved. Only water counts in the app. Records with an existing ID are kept unchanged. Invalid files are rejected before anything is written. Safe import limit: 32 MiB.")},confirmButton={TextButton(onClick={confirmImport=false;importPicker.launch(arrayOf("application/json","text/plain","application/octet-stream"))}) {Text("Choose backup")}},dismissButton={TextButton(onClick={confirmImport=false}) {Text("Cancel")}})
}
