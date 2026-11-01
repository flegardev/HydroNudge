package app.hydronudge.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import kotlinx.coroutines.flow.collectLatest
import androidx.navigation.compose.*
import app.hydronudge.data.local.DrinkEntry
import app.hydronudge.ui.dashboard.Dashboard
import app.hydronudge.ui.drinks.DrinkEditor
import app.hydronudge.ui.drinks.FavoriteEditor
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import app.hydronudge.ui.history.HistoryScreen
import app.hydronudge.ui.settings.SettingsScreen

@Composable
fun HydroApp(state: HydroState, model: HydroViewModel) {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val current = backStack?.destination?.route ?: "today"
    val snackbar = remember { SnackbarHostState() }
    var adding by rememberSaveable { mutableStateOf(false) }
    var editingId by rememberSaveable { mutableStateOf<String?>(null) }
    val editing = state.entries.firstOrNull { it.id == editingId }
    var favoriteId by rememberSaveable { mutableStateOf<String?>(null) }
    val favoriteEditing = state.favorites.firstOrNull { it.id == favoriteId }
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(model) {
        model.feedback.collectLatest { event ->
            if (event.message == "Drink added") haptic.performHapticFeedback(HapticFeedbackType.Confirm)
            if(snackbar.showSnackbar(event.message, if(event.undoEntry != null) "Undo" else null,
                    duration = SnackbarDuration.Short) == SnackbarResult.ActionPerformed) model.undo(event)
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            NavigationBar {
                listOf("today" to "Today", "history" to "History", "settings" to "Settings").forEach { (route,label) ->
                    NavigationBarItem(modifier=Modifier.testTag("nav:$route"),selected = current == route, onClick = {
                        nav.navigate(route) { popUpTo("today") { saveState = true }; launchSingleTop = true; restoreState = true }
                    }, icon = { Text(when(route) { "today" -> "◉"; "history" -> "▥"; else -> "⚙" }) }, label = { Text(label) })
                }
            }
        }
    ) { padding ->
        if (!state.ready) Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) { CircularProgressIndicator() }
        else Box(Modifier.fillMaxSize().padding(padding),contentAlignment=Alignment.TopCenter) { NavHost(nav, startDestination = "today", modifier = Modifier.widthIn(max=720.dp).fillMaxSize()) {
            composable("today") { Dashboard(state, model::quickAdd, { adding = true }, { editingId = it.id }, model::delete,
                { nav.navigate("settings") }, { model.save(DrinkEntry(name = it.name, category = it.category, amountMl = it.amountMl)) }, model::removeFavorite, {favoriteId=it.id}) }
            composable("history") { HistoryScreen(state, { editingId = it.id }, model::delete) }
            composable("settings") { SettingsScreen(state.settings, model::saveSettings, model::clear, model::export, model::import) }
        } }
    }
    if (state.ready && (adding || editing != null)) DrinkEditor(state.settings, editing, onDismiss = { adding = false; editingId = null },
        onSave = { entry, favorite ->
            model.save(entry, editing != null)
            if(favorite != null) model.favorite(favorite)
            adding = false; editingId = null
        })
    if(state.ready && favoriteEditing != null) FavoriteEditor(favoriteEditing,state.settings.unit,{favoriteId=null},model::favorite)
}
