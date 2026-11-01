package app.hydronudge.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.hydronudge.data.preferences.HydroSettings
import app.hydronudge.data.preferences.ThemeMode

val Ocean = Color(0xFF075B73)
val Aqua = Color(0xFF25C5BD)
internal val HydroLightColors = lightColorScheme(primary = Ocean, secondary = Color(0xFF006B63), tertiary = Color(0xFF466576),
    onPrimary=Color.White, onSecondary=Color.White, onTertiary=Color.White,
    onSurfaceVariant=Color(0xFF47636D), inverseSurface=Color(0xFF183440), inverseOnSurface=Color(0xFFE1F2F6), inversePrimary=Color(0xFF75D6E6),
    background = Color(0xFFF3F9FB), onBackground=Color(0xFF102F3A), surface = Color(0xFFF9FDFF), surfaceVariant = Color(0xFFE1EFF2), onSurface = Color(0xFF102F3A),
    primaryContainer=Color(0xFFD4EEF5), onPrimaryContainer=Color(0xFF063D50),
    secondaryContainer=Color(0xFFD3F3ED), onSecondaryContainer=Color(0xFF075449),
    tertiaryContainer=Color(0xFFDDEBF3), onTertiaryContainer=Color(0xFF253F4D),
    surfaceContainerLowest=Color.White, surfaceContainerLow=Color(0xFFEDF6F8), surfaceContainer=Color(0xFFE7F2F5),
    surfaceContainerHigh=Color(0xFFDFEDF1), surfaceContainerHighest=Color(0xFFD8E8ED), outline=Color(0xFF6D858D), outlineVariant=Color(0xFFBCCFD5))
internal val HydroDarkColors = darkColorScheme(primary = Color(0xFF75D6E6), secondary = Color(0xFF6EDACE), tertiary = Color(0xFFA8CADA),
    onPrimary=Color(0xFF003642), onSecondary=Color(0xFF003832), onTertiary=Color(0xFF153848),
    onSurfaceVariant=Color(0xFFB6CCD4), inverseSurface=Color(0xFFD6E9F0), inverseOnSurface=Color(0xFF183440), inversePrimary=Ocean,
    background = Color(0xFF09171E), onBackground=Color(0xFFE1F2F6), surface = Color(0xFF10232D), surfaceVariant = Color(0xFF233B45), onSurface = Color(0xFFE1F2F6),
    primaryContainer=Color(0xFF123F50), onPrimaryContainer=Color(0xFFC4EDF5),
    secondaryContainer=Color(0xFF1A4745), onSecondaryContainer=Color(0xFFABF3E8),
    tertiaryContainer=Color(0xFF304956), onTertiaryContainer=Color(0xFFD3EAF5),
    surfaceContainerLowest=Color(0xFF061218), surfaceContainerLow=Color(0xFF10232D), surfaceContainer=Color(0xFF142B36),
    surfaceContainerHigh=Color(0xFF1D3540), surfaceContainerHighest=Color(0xFF263F49), outline=Color(0xFF87A1AA), outlineVariant=Color(0xFF3B5660))
object Space { val small = 8.dp; val medium = 16.dp; val large = 24.dp }
@Composable
fun HydroTheme(settings: HydroSettings, content: @Composable () -> Unit) {
    val dark = when(settings.theme) { ThemeMode.SYSTEM -> isSystemInDarkTheme(); ThemeMode.DARK -> true; ThemeMode.LIGHT -> false }
    val context = LocalContext.current
    val colors = if (settings.dynamicColors && Build.VERSION.SDK_INT >= 31) {
        if(dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else if(dark) HydroDarkColors else HydroLightColors
    SideEffect {
        (context as? android.app.Activity)?.let { activity ->
            WindowCompat.getInsetsController(activity.window,activity.window.decorView).apply {
                isAppearanceLightStatusBars=!dark
                isAppearanceLightNavigationBars=!dark
            }
        }
    }
    MaterialTheme(colorScheme = colors, typography = Typography(
        headlineLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 40.sp),
        headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 34.sp),
        titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp)
    ), shapes = Shapes(medium = RoundedCornerShape(20.dp), large = RoundedCornerShape(28.dp)), content = content)
}
