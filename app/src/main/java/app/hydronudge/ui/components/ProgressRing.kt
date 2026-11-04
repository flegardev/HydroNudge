package app.hydronudge.ui.components

import android.animation.ValueAnimator
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import app.hydronudge.domain.*
import app.hydronudge.ui.theme.Aqua

/** Compact editorial hero; exact numbers continue beyond the capped arc. */
@Composable
fun WaterProgressVisualization(totalMl: Long, goalMl: Int, unit: VolumeUnit) {
    if (goalMl <= 0) {
        OceanCard(Modifier.fillMaxWidth()) {
            Text(unit.format(totalMl), style=MaterialTheme.typography.displayLarge)
            Text("water recorded today", style=MaterialTheme.typography.bodyMedium, color=MaterialTheme.colorScheme.onSurfaceVariant)
            Text("No daily goal set", style=MaterialTheme.typography.titleMedium)
            Text("A goal is optional. Needs vary; HydroNudge does not calculate medical advice. For a child, ask a parent or caregiver to choose a goal with appropriate professional guidance.", style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }
    val ratio=(totalMl.toFloat()/goalMl.coerceAtLeast(1)).coerceIn(0f,1f)
    val progress by animateFloatAsState(ratio,tween(if(ValueAnimator.areAnimatorsEnabled()) 600 else 0),label="Water progress")
    val colors=MaterialTheme.colorScheme
    val largeText=LocalDensity.current.fontScale>1.3f
    BoxWithConstraints(Modifier.fillMaxWidth().semantics {
        contentDescription="${unit.format(totalMl)} of ${unit.format(goalMl.toLong())}, ${goalPercent(totalMl,goalMl)} percent completed"
        progressBarRangeInfo=ProgressBarRangeInfo(ratio,0f..1f)
    }) {
        val stacked=largeText || maxWidth<280.dp
        val numbers: @Composable () -> Unit = {
            Column(verticalArrangement=Arrangement.spacedBy(6.dp)) {
                AnimatedContent(targetState=totalMl,transitionSpec={
                    androidx.compose.animation.fadeIn(tween(220)) togetherWith androidx.compose.animation.fadeOut(tween(140))
                },label="Water total") { displayed ->
                    Row(verticalAlignment=Alignment.Bottom,horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                        Text(unit.format(displayed).removeSuffix(" ${unit.label}"),style=MaterialTheme.typography.displayLarge,modifier=Modifier.weight(1f,fill=false))
                        Text(unit.label,style=MaterialTheme.typography.bodyMedium,color=colors.onSurfaceVariant,modifier=Modifier.padding(bottom=6.dp))
                    }
                }
                Text("of ${unit.format(goalMl.toLong())}",style=MaterialTheme.typography.bodyMedium,color=colors.onSurfaceVariant)
                Text("${goalPercent(totalMl,goalMl)}% completed",style=MaterialTheme.typography.labelLarge,color=colors.primary)
            }
        }
        Column(verticalArrangement=Arrangement.spacedBy(16.dp)) {
            if(stacked) numbers() else Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
                Box(Modifier.weight(1f)) {numbers()}
                Box(Modifier.size(104.dp),contentAlignment=Alignment.Center) {
                    Canvas(Modifier.fillMaxSize().padding(6.dp)) {
                        val d=size.minDimension
                        val stroke=Stroke(7.dp.toPx(),cap=StrokeCap.Round)
                        drawCircle(Brush.radialGradient(listOf(colors.primary.copy(alpha=0.12f),Color.Transparent)),d/2)
                        drawArc(colors.onSurface.copy(alpha=0.09f),130f,280f,false,Offset.Zero,Size(d,d),style=stroke)
                        if(progress>0) drawArc(Brush.linearGradient(listOf(colors.secondary,colors.primary)),130f,280f*progress,false,Offset.Zero,Size(d,d),style=stroke)
                        drawCircle(colors.primary.copy(alpha=0.12f),d/2-12.dp.toPx(),style=Stroke(0.7.dp.toPx()))
                    }
                    Icon(HydroIcons.Drop,null,Modifier.size(28.dp),tint=colors.primary)
                }
            }
            if(stacked) LinearProgressIndicator(progress={progress},modifier=Modifier.fillMaxWidth())
            HorizontalDivider(color=colors.primary.copy(alpha=0.12f))
            Text(if(totalMl<goalMl) "${unit.format(goalMl-totalMl)} to go" else "Daily water goal reached.",
                style=MaterialTheme.typography.bodyMedium,color=colors.onSurfaceVariant)
        }
    }
}
