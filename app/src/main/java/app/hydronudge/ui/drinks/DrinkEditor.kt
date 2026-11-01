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
import androidx.compose.ui.window.Dialog
import app.hydronudge.data.local.*
import app.hydronudge.data.preferences.HydroSettings
import app.hydronudge.domain.*
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DrinkEditor(settings: HydroSettings, entry: DrinkEntry?, onDismiss: ()->Unit, onSave: (DrinkEntry, FavoriteDrink?)->Unit) {
    val context = LocalContext.current
    var categoryName by rememberSaveable { mutableStateOf(entry?.category ?: DrinkCategory.WATER.name) }
    val category = DrinkCategory.valueOf(categoryName)
    var name by rememberSaveable { mutableStateOf(entry?.name ?: category.label) }
    var amount by rememberSaveable { mutableStateOf(settings.unit.input(entry?.amountMl ?: 250)) }
    var unitName by rememberSaveable { mutableStateOf(settings.unit.name) }
    val unit = VolumeUnit.valueOf(unitName)
    var timestamp by rememberSaveable { mutableLongStateOf(entry?.timestamp ?: System.currentTimeMillis()) }
    var note by rememberSaveable { mutableStateOf(entry?.note ?: "") }
    var favorite by rememberSaveable { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }
    val ml = amount.replace(',', '.').toDoubleOrNull()?.takeIf { it.isFinite() && it > 0 && it <= 20000 / unit.millilitres }?.let(unit::toMl)
    val valid = ml != null && ml in 1..20000 && name.isNotBlank() && name.length <= 80 && note.length <= 1000 && timestamp <= System.currentTimeMillis()
    val time = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.large) {
            Column(Modifier.fillMaxWidth().heightIn(max = 700.dp).imePadding().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(if(entry == null) "Make it a sip" else "Edit your drink", style = MaterialTheme.typography.headlineMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    DrinkCategory.entries.forEach { c -> FilterChip(selected = c == category, onClick = { categoryName = c.name; name = c.label }, label = { Text(c.label) }) }
                }
                OutlinedTextField(name, { name = it.take(80) }, label = { Text("Drink name") }, singleLine = true, modifier = Modifier.fillMaxWidth().testTag("drink-name"))
                OutlinedTextField(amount, { amount = it }, label = { Text("Amount (${unit.label})") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true, isError = ml == null || ml !in 1..20000, modifier = Modifier.fillMaxWidth().testTag("drink-amount"))
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
                OutlinedTextField(note, { note = it.take(1000) }, label = { Text("Note (optional)") }, modifier = Modifier.fillMaxWidth())
                Row { Checkbox(favorite, { favorite = it }); Text("Save this serving as a favorite", modifier = Modifier.padding(top = 12.dp)) }
                Text("Count the prepared drink once, including its water or milk. Fluid volume doesn’t describe nutritional quality.", style = MaterialTheme.typography.bodySmall)
                Button(onClick = {
                    if(valid && !submitted) { submitted = true
                        onSave(DrinkEntry(entry?.id ?: UUID.randomUUID().toString(),name.trim(),category.name,ml,timestamp,note.trim(),unit.name),
                            if(favorite) FavoriteDrink(name = name.trim(), category = category.name, amountMl = ml) else null)
                    }
                }, enabled = valid && !submitted, modifier = Modifier.fillMaxWidth().testTag("save-drink")) { Text("Save drink") }
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Cancel") }
            }
        }
    }
}
