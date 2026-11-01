package app.hydronudge.ui.history

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import app.hydronudge.R
import app.hydronudge.data.local.DrinkEntry
import app.hydronudge.domain.*
import app.hydronudge.ui.HydroState
import app.hydronudge.ui.dashboard.DrinkRow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HistoryScreen(state: HydroState, edit: (DrinkEntry)->Unit, delete: (DrinkEntry)->Unit) {
    var range by rememberSaveable { mutableStateOf("7 days") }
    var monthOffset by rememberSaveable { mutableIntStateOf(0) }
    val month=java.time.YearMonth.from(state.date).plusMonths(monthOffset.toLong())
    val start = when(range) { "Today" -> state.date; "30 days" -> state.date.minusDays(29); "Month" -> month.atDay(1); else -> state.date.minusDays(6) }
    val end=if(range=="Month" && monthOffset<0) month.atEndOfMonth() else state.date
    val report=remember(state.entries,start,end,state.zone,state.settings.goalMl) {historyReport(state.entries,start,end,state.zone,state.settings.goalMl)}
    val entries=report.entries
    val total=report.totalMl
    val bars=report.dailyMl
    val primary = MaterialTheme.colorScheme.primary
    val track = MaterialTheme.colorScheme.surfaceVariant
    LazyColumn(modifier=Modifier.testTag("history-list"),contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { Text("Every sip, remembered", style = MaterialTheme.typography.headlineLarge) }
        item { FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("Today","7 days","30 days","Month").forEach { r -> FilterChip(selected = range == r, onClick = { range = r }, label = { Text(r) }) } } }
        if(range=="Month") item { FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick={monthOffset--}) {Text("Previous month")}
            Text(month.toString(),Modifier.padding(12.dp))
            OutlinedButton(onClick={monthOffset++},enabled=monthOffset<0) {Text("Next month")}
        } }
        item {
            Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(state.settings.unit.format(total), style = MaterialTheme.typography.headlineMedium)
                Text("Recorded from $start to $end")
                Text(if(report.recordedDays==0) "Daily average available after your first record" else stringResource(R.string.history_average,pluralStringResource(R.plurals.recorded_days,report.recordedDays,report.recordedDays),state.settings.unit.format(report.averageRecordedDayMl)))
                Text(stringResource(R.string.history_goal_days,pluralStringResource(R.plurals.recorded_days,report.goalDays,report.goalDays)))
                Text("Blank days mean no records, not confirmed zero consumption. Goals are compared with your current setting.", style = MaterialTheme.typography.bodySmall)
            } }
        }
        item {
            Canvas(Modifier.fillMaxWidth().height(150.dp).semantics { contentDescription = "Daily logged volumes, oldest to newest: ${bars.joinToString { state.settings.unit.format(it) }}" }) {
                val max = maxOf(state.settings.goalMl.toLong(), bars.maxOrNull() ?: 1).toFloat()
                val width = size.width / bars.size
                bars.forEachIndexed { i, ml ->
                    drawRect(track, Offset(i*width + 2,0f), Size((width-4).coerceAtLeast(1f),size.height))
                    val h = size.height * ml / max
                    drawRect(primary, Offset(i*width+2,size.height-h), Size((width-4).coerceAtLeast(1f),h))
                }
            }
            Text("$start  →  $end", style = MaterialTheme.typography.labelSmall)
        }
        item { Text("Drink breakdown", style = MaterialTheme.typography.titleLarge) }
        if(entries.isEmpty()) item { Text("Nothing recorded in this period. Add your first drink from Today.") }
        report.categoryMl.forEach { (category, volume) -> item {
            val label = DrinkCategory.valueOf(category).label
            Text("$label · ${state.settings.unit.format(volume)}")
        } }
        items(entries, key = { it.id }) { DrinkRow(it,state.settings.unit,state.zone, { edit(it) }, { delete(it) }) }
    }
}
