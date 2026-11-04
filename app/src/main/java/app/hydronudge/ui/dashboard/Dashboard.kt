package app.hydronudge.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalConfiguration
import app.hydronudge.data.local.*
import app.hydronudge.domain.*
import app.hydronudge.ui.HydroState
import app.hydronudge.ui.components.*
import androidx.compose.runtime.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun Dashboard(state: HydroState, quickAdd: (Int)->Unit, addDrink: ()->Unit, edit: (DrinkEntry)->Unit,
    delete: (DrinkEntry)->Unit, settings: ()->Unit, history: ()->Unit) {
    LazyColumn(modifier=Modifier.testTag("dashboard-list"),contentPadding=PaddingValues(20.dp),verticalArrangement=Arrangement.spacedBy(24.dp)) {
        item {
            Row(verticalAlignment=Alignment.CenterVertically) {
                Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(4.dp)) {
                    Text("HydroNudge",style=MaterialTheme.typography.headlineMedium)
                    Text(state.date.format(DateTimeFormatter.ofPattern("EEE, MMM d")),style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick=settings) { Icon(HydroIcons.Settings,"Settings") }
            }
        }
        item {
            OceanHeroCard(Modifier.fillMaxWidth()) {
                Text("TODAY",style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.primary)
                WaterProgressVisualization(state.totalMl,state.settings.goalMl,state.settings.unit)
            }
        }
        item {
            Column(verticalArrangement=Arrangement.spacedBy(12.dp)) {
                HydroSectionHeader("Add water")
                state.settings.quickAmounts.chunked(2).forEach { amounts ->
                    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(12.dp)) {
                        amounts.forEach { ml ->
                            HydroQuickAddButton(state.settings.unit.format(ml.toLong()),{quickAdd(ml)},Modifier.weight(1f).testTag("quick-add:$ml"))
                        }
                    }
                }
                OutlinedButton(onClick=addDrink,modifier=Modifier.fillMaxWidth().heightIn(min=52.dp).testTag("choose-drink"),shape=MaterialTheme.shapes.small) {
                    Icon(HydroIcons.Plus,null,Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Custom amount")
                    Spacer(Modifier.weight(1f)); Icon(HydroIcons.Arrow,null,Modifier.size(18.dp))
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.CenterVertically) {
                Text("Recent", style=MaterialTheme.typography.titleLarge, modifier=Modifier.weight(1f))
                if (state.today.size > 3) {
                    TextButton(onClick=history, modifier=Modifier.testTag("view-all-water")) {
                        Text("View all (${state.today.size})")
                    }
                }
            }
        }
        if(state.today.isEmpty()) item {
            androidx.compose.animation.AnimatedVisibility(visible=true,enter=androidx.compose.animation.fadeIn(),exit=androidx.compose.animation.fadeOut()) {
                HydroEmptyState("No water yet", "Tap an amount to start.")
            }
        }
        items(state.today.take(3),key={it.id}) { HydroActivityRow(it,state.settings.unit,state.zone,{edit(it)},{delete(it)},showDate=false) }
    }
}

@Composable
fun HydroActivityRow(entry: DrinkEntry, unit: VolumeUnit, zone: ZoneId, edit: ()->Unit, delete: ()->Unit, showDate: Boolean = true) {
    var menu by remember { mutableStateOf(false) }
    Surface(Modifier.fillMaxWidth().testTag("drink:${entry.id}"),shape=MaterialTheme.shapes.small,color=MaterialTheme.colorScheme.surface)  {
        Column(Modifier.padding(16.dp)) {
        Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(12.dp)) {
            WaterBadge()
            Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(4.dp)) {
                Text(unit.format(entry.amountMl.toLong()),style=MaterialTheme.typography.titleMedium)
                Text(Instant.ofEpochMilli(entry.timestamp).atZone(zone).format(if(showDate) DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT) else DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Box {
                IconButton(onClick={menu=true},modifier=Modifier.testTag("actions:${entry.id}")) {Icon(HydroIcons.More,"Actions for water")}
                DropdownMenu(expanded=menu,onDismissRequest={menu=false}) {
                    DropdownMenuItem(text={Text("Edit water")},onClick={menu=false;edit()},modifier=Modifier.testTag("edit:${entry.id}"))
                    DropdownMenuItem(text={Text("Delete",color=MaterialTheme.colorScheme.error)},onClick={menu=false;delete()},modifier=Modifier.testTag("delete:${entry.id}"))
                }
            }
        }
        if(entry.note.isNotBlank()) Text(entry.note,style=MaterialTheme.typography.bodyMedium)
    }
    }
}
