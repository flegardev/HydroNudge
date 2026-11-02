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
import app.hydronudge.data.local.*
import app.hydronudge.domain.*
import app.hydronudge.ui.HydroState
import app.hydronudge.ui.components.ProgressRing
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Dashboard(state: HydroState, quickAdd: (Int)->Unit, addDrink: ()->Unit, edit: (DrinkEntry)->Unit,
    delete: (DrinkEntry)->Unit, settings: ()->Unit, favorite: (FavoriteDrink)->Unit, removeFavorite: (FavoriteDrink)->Unit, editFavorite: (FavoriteDrink)->Unit) {
    LazyColumn(modifier=Modifier.testTag("dashboard-list"),contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("HydroNudge", style = MaterialTheme.typography.headlineMedium)
                    Text(state.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                TextButton(onClick = settings) { Text("Settings") }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) { ProgressRing(state.totalMl, state.settings.goalMl, state.settings.unit); Spacer(Modifier.height(16.dp)) }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("A little sip goes a long way", style = MaterialTheme.typography.titleLarge)
                Text("QUICK ADD · WATER", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                state.settings.quickAmounts.chunked(2).forEach { amounts ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        amounts.forEach { ml -> FilledTonalButton(onClick = { quickAdd(ml) }, modifier = Modifier.weight(1f).heightIn(min = 56.dp).testTag("quick-add:$ml")) {
                            Text("+ ${state.settings.unit.format(ml.toLong())}")
                        } }
                    }
                }
                Button(onClick = addDrink, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).testTag("choose-drink")) { Text("Choose a drink") }
            }
        }
        if (state.favorites.isNotEmpty()) {
            item { Text("Your favorites", style = MaterialTheme.typography.titleLarge) }
            items(state.favorites, key = { "favorite:${it.id}" }) { f ->
                Card {
                    Column(Modifier.fillMaxWidth().padding(12.dp)) {
                        Text(f.name, fontWeight = FontWeight.SemiBold)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = { favorite(f) }) { Text("+ ${state.settings.unit.format(f.amountMl.toLong())}") }
                            TextButton(onClick = { editFavorite(f) }) { Text("Edit serving") }
                            TextButton(onClick = { removeFavorite(f) }) { Text("Remove favorite") }
                        }
                    }
                }
            }
        }
        item { Text("Recent drinks", style = MaterialTheme.typography.titleLarge) }
        if (state.today.isEmpty()) item {
            OutlinedCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Your day starts with a sip", style = MaterialTheme.typography.titleMedium)
                Text("Log water, coffee, milk or any prepared drink. Its recorded volume counts toward your fluids.")
            } }
        }
        items(state.today, key = { it.id }) { DrinkRow(it, state.settings.unit, state.zone, { edit(it) }, { delete(it) }) }
        item { Text("Stay hydrated. Stay free.\nNo ads. No subscriptions. No tracking.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DrinkRow(entry: DrinkEntry, unit: VolumeUnit, zone: ZoneId, edit: ()->Unit, delete: ()->Unit) {
    Card(Modifier.fillMaxWidth().testTag("drink:${entry.id}")) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(DrinkCategory.entries.firstOrNull { it.name == entry.category }?.symbol ?: "✦", style = MaterialTheme.typography.headlineMedium)
                Column(Modifier.weight(1f)) {
                    Text(entry.name, style = MaterialTheme.typography.titleMedium)
                    Text(Instant.ofEpochMilli(entry.timestamp).atZone(zone).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)), style = MaterialTheme.typography.bodySmall)
                }
            }
            Text(unit.format(entry.amountMl.toLong()), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            if(entry.note.isNotBlank()) Text(entry.note, style = MaterialTheme.typography.bodyMedium)
            FlowRow { TextButton(onClick = edit,modifier=Modifier.testTag("edit:${entry.id}")) { Text("Edit ${entry.name}") }; TextButton(onClick = delete,modifier=Modifier.testTag("delete:${entry.id}")) { Text("Delete", color = MaterialTheme.colorScheme.error) } }
        }
    }
}
