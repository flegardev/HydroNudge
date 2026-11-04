package app.hydronudge.ui

import androidx.compose.foundation.layout.*
import app.hydronudge.ui.components.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import kotlinx.coroutines.flow.collectLatest
import androidx.navigation.compose.*
import app.hydronudge.data.local.DrinkEntry
import app.hydronudge.ui.dashboard.Dashboard
import app.hydronudge.ui.drinks.DrinkEditor
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import app.hydronudge.ui.history.HistoryScreen
import app.hydronudge.ui.settings.SettingsScreen

@Composable
fun HydroApp(state: HydroState, model: HydroViewModel) {
    val density=LocalDensity.current
    val compactNavigation=density.fontScale>1.5f && LocalWindowInfo.current.containerSize.width/density.density<360f
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val current = backStack?.destination?.route ?: "today"
    val snackbar = remember { SnackbarHostState() }
    var adding by rememberSaveable { mutableStateOf(false) }
    var editingId by rememberSaveable { mutableStateOf<String?>(null) }
    val editing = state.entries.firstOrNull { it.id == editingId }
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(model) {
        model.feedback.collectLatest { event ->
            if (event.message == "Water added") haptic.performHapticFeedback(HapticFeedbackType.Confirm)
            if(snackbar.showSnackbar(event.message, if(event.undoEntry != null) "Undo" else null,
                    duration = SnackbarDuration.Short, withDismissAction = true) == SnackbarResult.ActionPerformed) model.undo(event)
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            NavigationBar(containerColor=MaterialTheme.colorScheme.surface.copy(alpha=0.95f),tonalElevation=0.dp, modifier=Modifier.padding(horizontal=16.dp).clip(RoundedCornerShape(topStart=28.dp,topEnd=28.dp))) {
                listOf("today" to "Today", "history" to "History", "settings" to "Settings").forEach { (route,label) ->
                    NavigationBarItem(modifier=Modifier.testTag("nav:$route"),selected = current == route, onClick = {
                        nav.navigate(route) { popUpTo("today") { saveState = true }; launchSingleTop = true; restoreState = true }
                    }, icon = { Icon(when(route) { "today" -> HydroIcons.Drop; "history" -> HydroIcons.Chart; else -> HydroIcons.Settings },if(compactNavigation) label else null) }, label = if(compactNavigation) null else ({ Text(label) }))
                }
            }
        }
    ) { padding ->
        if (!state.ready) Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) { CircularProgressIndicator() }
        else OceanBackground(Modifier.fillMaxSize().padding(padding)) { NavHost(nav, startDestination = "today", modifier = Modifier.align(Alignment.TopCenter).widthIn(max=720.dp).fillMaxSize(),
            enterTransition={fadeIn(tween(220))}, exitTransition={fadeOut(tween(140))},
            popEnterTransition={fadeIn(tween(220))}, popExitTransition={fadeOut(tween(140))}) {
            composable("today") { Dashboard(state, model::quickAdd, { adding = true }, { editingId = it.id }, model::delete,
                { nav.navigate("settings") }, { nav.navigate("history") { launchSingleTop=true } }) }
            composable("history") { HistoryScreen(state, { editingId = it.id }, model::delete) }
            composable("settings") { SettingsScreen(state.settings, model::saveSettings, model::clear, model::export, model::import) }
        } }
    }
    if (state.ready && (adding || editing != null)) DrinkEditor(state.settings, editing, onDismiss = { adding = false; editingId = null },
        onSave = { entry ->
            model.save(entry, editing != null)
            adding = false; editingId = null
        })
}
