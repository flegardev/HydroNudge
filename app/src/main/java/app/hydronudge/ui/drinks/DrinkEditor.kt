package app.hydronudge.ui.drinks

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.format.DateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.hydronudge.ui.components.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import app.hydronudge.data.local.*
import app.hydronudge.data.preferences.HydroSettings
import app.hydronudge.domain.*
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DrinkEditor(settings: HydroSettings, entry: DrinkEntry?, onDismiss: ()->Unit, onSave: (DrinkEntry)->Unit) {
    val context = LocalContext.current
    val category = DrinkCategory.WATER
    val name = "Water"
    var amount by rememberSaveable { mutableStateOf(settings.unit.input(entry?.amountMl ?: 250)) }
    var unitName by rememberSaveable { mutableStateOf(settings.unit.name) }
    val unit = VolumeUnit.valueOf(unitName)
    var timestamp by rememberSaveable { mutableLongStateOf(entry?.timestamp ?: System.currentTimeMillis()) }
    var note by rememberSaveable { mutableStateOf(entry?.note ?: "") }
    var submitted by remember { mutableStateOf(false) }
    val ml = amount.replace(',', '.').toDoubleOrNull()?.takeIf { it.isFinite() && it > 0 && it <= 20000 / unit.millilitres }?.let(unit::toMl)
    val valid = ml != null && ml in 1..20000 && name.isNotBlank() && name.length <= 80 && note.length <= 1000 && timestamp <= System.currentTimeMillis()
    val time = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())
    HydroBottomSheet(onDismiss) {
        Column(Modifier.testTag("drink-editor").fillMaxWidth().heightIn(max=760.dp).imePadding().padding(horizontal=20.dp)) {
            Column(Modifier.weight(1f,fill=false).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) {
                Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("YOUR DAILY RITUAL",style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.primary)
                        Text(if(entry == null) "Add water" else "Edit water",style=MaterialTheme.typography.headlineMedium)
                    }
                    IconButton(onClick=onDismiss) {Icon(HydroIcons.Close,"Close water editor")}
                }
                OutlinedTextField(amount, { amount = it }, textStyle=MaterialTheme.typography.displayLarge, label = { Text("Water amount (${unit.label})") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true, isError = ml == null || ml !in 1..20000, modifier = Modifier.fillMaxWidth().testTag("drink-amount"))
                FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                    listOf(150,250,330,500).forEach { serving ->
                        SuggestionChip(onClick={amount=unit.input(serving)},label={Text(unit.format(serving.toLong()))})
                    }
                }
                if(ml == null || ml !in 1..20000) Text("Enter an amount between 1 and 20,000 mL.",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.error)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { VolumeUnit.entries.forEach { u ->
                    FilterChip(selected = unit == u, onClick = { if (ml != null) amount = u.input(ml); unitName = u.name }, label = { Text(u.label) })
                } }
                Text(time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)), style = MaterialTheme.typography.bodyMedium)
                FlowRow {
                    TextButton(onClick = {
                        DatePickerDialog(context, { _,y,m,d -> timestamp = time.withDayOfMonth(1).withYear(y).withMonth(m+1).withDayOfMonth(d).toInstant().toEpochMilli() },time.year,time.monthValue-1,time.dayOfMonth).apply { datePicker.maxDate = System.currentTimeMillis() }.show()
                    }) { Text("Change date") }
                    TextButton(onClick = { TimePickerDialog(context, { _,h,m -> timestamp = time.withHour(h).withMinute(m).withSecond(0).toInstant().toEpochMilli() },time.hour,time.minute,DateFormat.is24HourFormat(context)).show() }) { Text("Change time") }
                }
                if(timestamp > System.currentTimeMillis()) Text("Choose a time in the past.", color = MaterialTheme.colorScheme.error)
                OutlinedTextField(note, { note = it.take(1000) }, label = { Text("Note (optional)") }, modifier = Modifier.fillMaxWidth().testTag("water-note"))
            }
            Spacer(Modifier.height(12.dp))
            HydroPrimaryButton(onClick = {
                    if(valid && !submitted) { submitted = true
                        onSave(DrinkEntry(entry?.id ?: UUID.randomUUID().toString(),name.trim(),category.name,ml,timestamp,note.trim(),unit.name))
                    }
                }, enabled = valid && !submitted, modifier = Modifier.fillMaxWidth().heightIn(min=56.dp).testTag("save-drink")) { Text("Save water") }
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Cancel") }
        }
    }
}
