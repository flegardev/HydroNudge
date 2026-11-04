package app.hydronudge.widgets

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.*
import androidx.glance.appwidget.*
import androidx.glance.appwidget.action.*
import androidx.glance.layout.*
import androidx.glance.material3.ColorProviders
import androidx.glance.text.*
import app.hydronudge.HydroApplication
import app.hydronudge.MainActivity
import app.hydronudge.data.local.DrinkEntry
import app.hydronudge.domain.*
import app.hydronudge.reminders.ReminderScheduler
import app.hydronudge.ui.theme.HydroLightColors
import app.hydronudge.ui.theme.HydroDarkColors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.UUID

class HydroWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Responsive(setOf(
        androidx.compose.ui.unit.DpSize(110.dp,110.dp),
        androidx.compose.ui.unit.DpSize(240.dp,150.dp),
        androidx.compose.ui.unit.DpSize(280.dp,240.dp)
    ))
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val app = context.applicationContext as HydroApplication
        val initialEntries = app.repository.all()
        val initialSettings = app.preferences.settings.first()
        provideContent {
            val entries by app.repository.entries.collectAsState(initialEntries)
            val settings by app.preferences.settings.collectAsState(initialSettings)
            val w = dayWindow(LocalDate.now(),ZoneId.systemDefault())
            val today = entries.filter { it.isWater() && it.timestamp >= w.start && it.timestamp < w.endExclusive }
            val total = today.sumOf { it.amountMl.toLong() }
            val token = remember(entries,settings) { UUID.randomUUID().toString() }
            val size = LocalSize.current
            val compact = size.height < 200.dp
            GlanceTheme(colors = ColorProviders(
                light = HydroLightColors,
                dark = HydroDarkColors
            )) {
                Column(GlanceModifier.fillMaxSize().background(GlanceTheme.colors.surface).appWidgetBackground().cornerRadius(28.dp).padding(if(compact) 12.dp else 16.dp), verticalAlignment=Alignment.Vertical.CenterVertically) {
                    if(compact && size.width >= 240.dp) {
                        Row(GlanceModifier.fillMaxWidth(),verticalAlignment=Alignment.Vertical.CenterVertically) {
                            Text("HydroNudge",modifier=GlanceModifier.defaultWeight().clickable(actionStartActivity<MainActivity>()),style=TextStyle(color=GlanceTheme.colors.primary,fontWeight=FontWeight.Bold,fontSize=12.sp))
                            if(settings.goalMl>0) Text("${goalPercent(total,settings.goalMl)}%",style=TextStyle(color=GlanceTheme.colors.onSurface,fontWeight=FontWeight.Bold,fontSize=22.sp))
                        }
                    } else {
                    Text(if(size.width >= 240.dp) "HydroNudge" else "WATER",style=TextStyle(color=GlanceTheme.colors.primary,fontWeight=FontWeight.Bold,fontSize=if(compact) 12.sp else 14.sp),modifier=GlanceModifier.clickable(actionStartActivity<MainActivity>()))
                    if(settings.goalMl>0) Text("${goalPercent(total,settings.goalMl)}%",style=TextStyle(color=GlanceTheme.colors.onSurface,fontWeight=FontWeight.Bold,fontSize=if(size.width < 240.dp) 22.sp else if(compact) 26.sp else 32.sp))
                    }
                    if(size.width >= 240.dp) {
                        Text(if(settings.goalMl>0) "${settings.unit.format(total)} of ${settings.unit.format(settings.goalMl.toLong())}" else "${settings.unit.format(total)} recorded",style=TextStyle(color=GlanceTheme.colors.onSurface,fontSize=if(compact) 12.sp else 14.sp))
                        Spacer(GlanceModifier.height(if(compact) 4.dp else 8.dp))
                        if(settings.goalMl>0) LinearProgressIndicator(progress=(total.toFloat()/settings.goalMl).coerceIn(0f,1f),modifier=GlanceModifier.fillMaxWidth().height(4.dp),color=GlanceTheme.colors.primary,backgroundColor=GlanceTheme.colors.surfaceVariant)
                        Spacer(GlanceModifier.height(if(compact) 4.dp else 8.dp))
                        Row(GlanceModifier.fillMaxWidth()) {
                            Button("+ ${settings.unit.format(250)}",modifier=GlanceModifier.defaultWeight().height(48.dp),onClick=actionRunCallback<WidgetDrinkAction>(actionParametersOf(WidgetDrinkAction.Amount to 250,WidgetDrinkAction.Token to "$token:250")))
                            Spacer(GlanceModifier.width(4.dp))
                            Button("+ ${settings.unit.format(500)}",modifier=GlanceModifier.defaultWeight().height(48.dp),onClick=actionRunCallback<WidgetDrinkAction>(actionParametersOf(WidgetDrinkAction.Amount to 500,WidgetDrinkAction.Token to "$token:500")))
                        }
                    }
                    if(size.height >= 240.dp) {
                        val last = today.maxByOrNull { it.timestamp }
                        Text(if(last == null) "No water recorded today" else "Last: Water · ${Instant.ofEpochMilli(last.timestamp).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"))}",style=TextStyle(color=GlanceTheme.colors.onSurface,fontSize=12.sp),maxLines=2)
                    }
                    if(size.width < 240.dp) Text(settings.unit.format(total),style=TextStyle(color=GlanceTheme.colors.onSurface,fontSize=12.sp),modifier=GlanceModifier.clickable(actionStartActivity<MainActivity>()))
                }
            }
        }
    }
}

class WidgetDrinkAction : ActionCallback {
    companion object { val Amount = ActionParameters.Key<Int>("amount"); val Token = ActionParameters.Key<String>("token") }
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val amount = parameters[Amount] ?: return
        val token = parameters[Token] ?: return
        if(amount != 250 && amount != 500) return
        val app = context.applicationContext as HydroApplication
        app.repository.add(DrinkEntry(id="widget:$glanceId:$token",name="Water",category="WATER",amountMl=amount))
        HydroWidget().updateAll(context)
    }
}

class HydroWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = HydroWidget()
    override fun onEnabled(context: Context) { super.onEnabled(context); schedule(context) }
    override fun onDisabled(context: Context) { super.onDisabled(context); schedule(context) }
    private fun schedule(context: Context) {
        val app = context.applicationContext as HydroApplication
        app.applicationScope.launch { ReminderScheduler.apply(context,app.preferences.settings.first()) }
    }
}
