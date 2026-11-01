package app.hydronudge.ui.components

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import app.hydronudge.domain.*
import app.hydronudge.ui.theme.Aqua

@Composable
fun ProgressRing(totalMl: Long, goalMl: Int, unit: VolumeUnit) {
    val ratio = (totalMl.toFloat() / goalMl.coerceAtLeast(1)).coerceIn(0f, 1f)
    val progress by animateFloatAsState(ratio, tween(650), label = "Hydration progress")
    val primary = MaterialTheme.colorScheme.primary
    val track = MaterialTheme.colorScheme.surfaceVariant
    val largeText=LocalDensity.current.fontScale > 1.3f
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.fillMaxWidth().then(if(largeText) Modifier.heightIn(min=280.dp).padding(vertical=24.dp) else Modifier.height(280.dp))
            .semantics(mergeDescendants = true) { contentDescription = "${unit.format(totalMl)} of ${unit.format(goalMl.toLong())}, ${goalPercent(totalMl,goalMl)} percent completed" }, contentAlignment = Alignment.Center) {
            if(!largeText) Canvas(Modifier.fillMaxSize().padding(12.dp)) {
                val diameter = size.minDimension - 22.dp.toPx()
                val origin = Offset((size.width-diameter)/2, (size.height-diameter)/2)
                val stroke = Stroke(16.dp.toPx(), cap = StrokeCap.Round)
                drawArc(track, 135f, 270f, false, origin, Size(diameter,diameter), style = stroke)
                if(progress > 0f) drawArc(Brush.sweepGradient(listOf(primary, Aqua, primary)), 135f, 270f*progress, false, origin, Size(diameter,diameter), style = stroke)
            }
            Column(Modifier.padding(horizontal = 40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("TODAY’S FLUIDS", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                Text(unit.format(totalMl), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Text("of ${unit.format(goalMl.toLong())}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                Text("${goalPercent(totalMl,goalMl)}% completed", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            }
        }
        if(largeText) LinearProgressIndicator(progress={progress},modifier=Modifier.fillMaxWidth().padding(horizontal=20.dp))
        Text(if(totalMl < goalMl) "${unit.format(goalMl-totalMl)} remaining" else "Daily goal reached. Drink to your needs.",
            style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
