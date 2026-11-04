package app.hydronudge.ui.history

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import app.hydronudge.ui.components.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.Alignment
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
import app.hydronudge.ui.dashboard.HydroActivityRow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HistoryScreen(state: HydroState, edit: (DrinkEntry)->Unit, delete: (DrinkEntry)->Unit) {
    var range by rememberSaveable { mutableStateOf("7 days") }
    var monthOffset by rememberSaveable { mutableIntStateOf(0) }
    var showStatsHelp by rememberSaveable { mutableStateOf(false) }
    val month=java.time.YearMonth.from(state.date).plusMonths(monthOffset.toLong())
    val start = when(range) { "Today" -> state.date; "30 days" -> state.date.minusDays(29); "Month" -> month.atDay(1); else -> state.date.minusDays(6) }
    val end=if(range=="Month" && monthOffset<0) month.atEndOfMonth() else state.date
    val report=remember(state.entries,start,end,state.zone,state.settings.goalMl) {historyReport(state.entries,start,end,state.zone,state.settings.goalMl)}
    val entries=report.entries
    val total=report.totalMl
    val bars=report.dailyMl
    val reveal = remember { Animatable(1f) }
    LaunchedEffect(start,end) { reveal.snapTo(0f); reveal.animateTo(1f,tween(if(android.animation.ValueAnimator.areAnimatorsEnabled()) 260 else 0)) }
    val primary = MaterialTheme.colorScheme.primary
    val track = MaterialTheme.colorScheme.surfaceVariant
    LazyColumn(modifier=Modifier.testTag("history-list"),contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        item { Text("History",style=MaterialTheme.typography.headlineLarge) }
        item { FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("Today","7 days","30 days","Month").forEach { r -> FilterChip(modifier=Modifier.testTag("history-range:$r"),selected = range == r, onClick = { range = r }, label = { Text(r) }) } } }
        if(range=="Month") item { FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick={monthOffset--}) {Text("Previous month")}
            Text(month.toString(),Modifier.padding(12.dp))
            OutlinedButton(onClick={monthOffset++},enabled=monthOffset<0) {Text("Next month")}
        } }
        item {
            OceanCard(Modifier.fillMaxWidth()) { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("TOTAL WATER",style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.primary)
                Text(state.settings.unit.format(total), style = MaterialTheme.typography.displayLarge)
                Text(if(report.recordedDays==0) "Daily average available after your first record" else stringResource(R.string.history_average,pluralStringResource(R.plurals.recorded_days,report.recordedDays,report.recordedDays),state.settings.unit.format(report.averageRecordedDayMl)))
                Text(if(state.settings.goalMl>0) stringResource(R.string.history_goal_days,pluralStringResource(R.plurals.recorded_days,report.goalDays,report.goalDays)) else "Set an optional goal to view goal days")
                TextButton(onClick={showStatsHelp=true}) { Text("About these stats") }
            } }
        }
        item {
            OceanCard(Modifier.fillMaxWidth()) {
                Text("Daily water",style=MaterialTheme.typography.titleLarge)
                Text("Daily water · 0 to ${state.settings.unit.format(maxOf(state.settings.goalMl.toLong(),bars.maxOrNull() ?: 0))}",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
                Canvas(Modifier.fillMaxWidth().height(160.dp).semantics {
                    contentDescription="Daily logged volumes, oldest to newest: ${bars.joinToString { state.settings.unit.format(it) }}"
                }) {
                    val max=maxOf(state.settings.goalMl.toLong(),bars.maxOrNull() ?: 1).toFloat()
                    val width=size.width/bars.size
                    val gap=if(bars.size>15) 2.dp.toPx() else 7.dp.toPx()
                    for(step in 1..3) drawLine(track,Offset(0f,size.height*step/3),Offset(size.width,size.height*step/3),1.dp.toPx())
                    bars.forEachIndexed { i,ml ->
                        val h=size.height*ml/max*reveal.value
                        if(h>0) drawRoundRect(Brush.verticalGradient(listOf(primary,primary.copy(alpha=0.4f))),Offset(i*width+gap/2,size.height-h),Size((width-gap).coerceAtLeast(1f),h),CornerRadius(5.dp.toPx()))
                    }
                }
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
                    Text(start.toString(),style=MaterialTheme.typography.labelSmall)
                    Text(end.toString(),style=MaterialTheme.typography.labelSmall)
                }
                if(entries.isEmpty()) Text("Your first water entry will bring this chart to life.",style=MaterialTheme.typography.bodyMedium,color=MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item { HydroSectionHeader("Water entries") }
        if(entries.isEmpty()) item { HydroEmptyState("No entries", "Try another period.") }
        items(entries, key = { it.id }) { HydroActivityRow(it,state.settings.unit,state.zone, { edit(it) }, { delete(it) }) }
    }
    if(showStatsHelp) AlertDialog(
        onDismissRequest={showStatsHelp=false},
        title={Text("About these stats")},
        text={Text("Averages use days with water records. Blank days mean no records, not confirmed zero intake. Goal days use your current goal, including when you review older entries.")},
        confirmButton={TextButton(onClick={showStatsHelp=false}) {Text("Close")}}
    )

}
