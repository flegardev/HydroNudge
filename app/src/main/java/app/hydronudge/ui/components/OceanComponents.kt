package app.hydronudge.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/** Original 24-unit line icons. No font glyphs or platform-dependent emoji. */
object HydroIcons {
    val Drop = icon("Water") { moveTo(12f,2f); curveTo(10f,6f,4f,11f,4f,15f); curveTo(4f,24f,20f,24f,20f,15f); curveTo(20f,11f,14f,6f,12f,2f); close() }
    val Chart = icon("History") { moveTo(4f,20f); lineTo(4f,13f); moveTo(12f,20f); lineTo(12f,4f); moveTo(20f,20f); lineTo(20f,9f) }
    val Settings = icon("Settings") { moveTo(4f,6f); lineTo(20f,6f); moveTo(4f,18f); lineTo(20f,18f); moveTo(8f,3f); lineTo(8f,9f); moveTo(16f,15f); lineTo(16f,21f) }
    val Plus = icon("Add") { moveTo(12f,4f); lineTo(12f,20f); moveTo(4f,12f); lineTo(20f,12f) }
    val Close = icon("Close") { moveTo(6f,6f); lineTo(18f,18f); moveTo(18f,6f); lineTo(6f,18f) }
    val Arrow = icon("Continue") { moveTo(4f,12f); lineTo(20f,12f); moveTo(14f,6f); lineTo(20f,12f); lineTo(14f,18f) }
    val More = icon("More") { moveTo(5f,11f); lineTo(5f,13f); moveTo(12f,11f); lineTo(12f,13f); moveTo(19f,11f); lineTo(19f,13f) }
    private fun icon(name: String, block: androidx.compose.ui.graphics.vector.PathBuilder.() -> Unit) =
        ImageVector.Builder(name,24.dp,24.dp,24f,24f).apply {
            path(fill=null, stroke=SolidColor(Color.Black), strokeLineWidth=1.65f,
                strokeLineCap=StrokeCap.Round, strokeLineJoin=StrokeJoin.Round, pathBuilder=block)
        }.build()
}

@Composable
fun PremiumGlassCard(modifier: Modifier = Modifier, hero: Boolean = false, content: @Composable ColumnScope.() -> Unit) {
    val colors = MaterialTheme.colorScheme
    Surface(modifier, shape=RoundedCornerShape(if(hero) 32.dp else 28.dp), color=colors.surface,
        border=BorderStroke(0.5.dp, colors.primary.copy(alpha=0.16f))) {
        Column(Modifier.background(Brush.linearGradient(listOf(colors.primary.copy(alpha=if(hero) 0.12f else 0.045f),Color.Transparent)))
            .padding(20.dp), verticalArrangement=Arrangement.spacedBy(12.dp), content=content)
    }
}

@Composable
fun OceanCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) =
    PremiumGlassCard(modifier, content=content)

@Composable
fun OceanHeroCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) =
    PremiumGlassCard(modifier, hero=true, content=content)

@Composable
fun OceanBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val colors=MaterialTheme.colorScheme
    Box(modifier.background(Brush.verticalGradient(listOf(colors.surfaceContainerLowest, colors.background, colors.background))), content=content)
}

@Composable
fun HydroPrimaryButton(onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true, content: @Composable RowScope.() -> Unit) {
    Button(onClick, modifier.heightIn(min=56.dp), enabled=enabled, shape=RoundedCornerShape(20.dp),
        contentPadding=PaddingValues(horizontal=20.dp,vertical=14.dp), content=content)
}

@Composable
fun HydroQuickAddButton(amount: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val interactions=androidx.compose.runtime.remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val pressed by interactions.collectIsPressedAsState()
    val scale by androidx.compose.animation.core.animateFloatAsState(if(pressed) 0.97f else 1f,
        androidx.compose.animation.core.tween(120), label="Quick add press")
    Surface(onClick=onClick, modifier=modifier.graphicsLayer { scaleX=scale; scaleY=scale }.heightIn(min=64.dp),
        interactionSource=interactions, shape=RoundedCornerShape(20.dp), color=MaterialTheme.colorScheme.primaryContainer,
        border=BorderStroke(0.5.dp,MaterialTheme.colorScheme.primary.copy(alpha=0.18f))) {
        Row(Modifier.background(Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary.copy(alpha=0.08f),Color.Transparent)))
            .padding(horizontal=16.dp,vertical=16.dp),verticalAlignment=Alignment.CenterVertically) {
            Text("+ $amount", style=MaterialTheme.typography.titleMedium, modifier=Modifier.weight(1f), color=MaterialTheme.colorScheme.onPrimaryContainer)
            Icon(HydroIcons.Drop,null,Modifier.size(18.dp),tint=MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Composable
fun HydroSectionHeader(title: String, detail: String? = null) {
    Column(verticalArrangement=Arrangement.spacedBy(4.dp)) {
        Text(title,style=MaterialTheme.typography.titleLarge)
        if(detail!=null) Text(detail,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun HydroEmptyState(title: String, message: String, action: (() -> Unit)? = null) {
    PremiumGlassCard(Modifier.fillMaxWidth()) {
        WaterBadge()
        Text(title,style=MaterialTheme.typography.titleMedium)
        Text(message,style=MaterialTheme.typography.bodyMedium,color=MaterialTheme.colorScheme.onSurfaceVariant)
        if(action!=null) TextButton(onClick=action,modifier=Modifier.heightIn(min=48.dp)) { Text("Add water") }
    }
}

@Composable
fun HydroSettingsGroup(title: String, icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    PremiumGlassCard(Modifier.fillMaxWidth()) {
        Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(12.dp)) {
            Icon(icon,null,Modifier.size(20.dp),tint=MaterialTheme.colorScheme.primary)
            Text(title,style=MaterialTheme.typography.titleMedium)
        }
        HorizontalDivider(color=MaterialTheme.colorScheme.outlineVariant.copy(alpha=0.5f))
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HydroBottomSheet(onDismiss: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    ModalBottomSheet(onDismissRequest=onDismiss, sheetState=rememberModalBottomSheetState(skipPartiallyExpanded=true),
        containerColor=MaterialTheme.colorScheme.surface, shape=RoundedCornerShape(topStart=32.dp,topEnd=32.dp),content=content)
}

@Composable
fun WaterBadge(modifier: Modifier = Modifier) {
    Surface(modifier.size(48.dp),shape=RoundedCornerShape(16.dp),color=MaterialTheme.colorScheme.primaryContainer) {
        Box(contentAlignment=Alignment.Center) {
            Icon(HydroIcons.Drop,null,Modifier.size(24.dp),tint=MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Composable
fun PageHeading(eyebrow: String, title: String, subtitle: String? = null) {
    Column(verticalArrangement=Arrangement.spacedBy(6.dp)) {
        Text(eyebrow.uppercase(),style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.primary)
        Text(title,style=MaterialTheme.typography.headlineLarge)
        subtitle?.let { Text(it,style=MaterialTheme.typography.bodyMedium,color=MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}
