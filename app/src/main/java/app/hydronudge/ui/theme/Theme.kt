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

/** Ocean 3.0 brand tokens. UI consumes semantic Material colors. */
object OceanTokens {
    val Background = Color(0xFF061522)
    val Deep = Color(0xFF04101B)
    val Surface = Color(0xFF102B3C)
    val Elevated = Color(0xFF17394A)
    val Interactive = Color(0xFF1B4258)
    val Aqua = Color(0xFF7EDCEB)
    val Blue = Color(0xFF57B8F1)
    val Turquoise = Color(0xFF80E6D9)
    val Text = Color(0xFFF1FAFF)
    val Secondary = Color(0xFFACC7D4)
    val Muted = Color(0xFF829DAD)
    val LightBackground = Color(0xFFF1F7FA)
    val LightSurface = Color.White
    val LightElevated = Color(0xFFE7F2F8)
    val LightText = Color(0xFF102F42)
    val LightSecondary = Color(0xFF55717E)
    val LightBlue = Color(0xFF126A90)
    val LightAqua = Color(0xFF58C4D7)
    val LightBorder = Color(0xFFD5E8F0)
}
val Ocean = OceanTokens.LightBlue
val Aqua = OceanTokens.Aqua
internal val HydroLightColors = lightColorScheme(
    primary=OceanTokens.LightBlue, onPrimary=Color.White,
    secondary=OceanTokens.LightBlue, tertiary=OceanTokens.LightAqua,
    background=OceanTokens.LightBackground, onBackground=OceanTokens.LightText,
    surface=OceanTokens.LightSurface, onSurface=OceanTokens.LightText,
    surfaceVariant=OceanTokens.LightElevated, onSurfaceVariant=OceanTokens.LightSecondary,
    primaryContainer=OceanTokens.LightElevated, onPrimaryContainer=OceanTokens.LightBlue,
    secondaryContainer=OceanTokens.LightElevated, onSecondaryContainer=OceanTokens.LightText,
    surfaceContainerLowest=OceanTokens.LightSurface, surfaceContainerLow=OceanTokens.LightBackground,
    surfaceContainer=OceanTokens.LightElevated, surfaceContainerHigh=OceanTokens.LightElevated,
    surfaceContainerHighest=OceanTokens.LightBorder,
    outline=OceanTokens.LightSecondary, outlineVariant=OceanTokens.LightBorder
)
internal val HydroDarkColors = darkColorScheme(
    primary=OceanTokens.Aqua, onPrimary=OceanTokens.Deep,
    secondary=OceanTokens.Blue, onSecondary=OceanTokens.Deep,
    tertiary=OceanTokens.Turquoise, onTertiary=OceanTokens.Deep,
    background=OceanTokens.Background, onBackground=OceanTokens.Text,
    surface=OceanTokens.Surface, onSurface=OceanTokens.Text,
    surfaceVariant=OceanTokens.Elevated, onSurfaceVariant=OceanTokens.Secondary,
    primaryContainer=OceanTokens.Interactive, onPrimaryContainer=OceanTokens.Aqua,
    secondaryContainer=OceanTokens.Elevated, onSecondaryContainer=OceanTokens.Text,
    surfaceContainerLowest=OceanTokens.Deep, surfaceContainerLow=OceanTokens.Surface,
    surfaceContainer=OceanTokens.Elevated, surfaceContainerHigh=OceanTokens.Interactive,
    surfaceContainerHighest=OceanTokens.Interactive,
    outline=OceanTokens.Muted, outlineVariant=OceanTokens.Elevated
)
object Space {
    val base=4.dp; val small=8.dp; val gap=12.dp; val medium=16.dp
    val page=20.dp; val large=24.dp; val major=32.dp
}
@Composable
fun HydroTheme(settings: HydroSettings, content: @Composable () -> Unit) {
    val dark = when(settings.theme) { ThemeMode.SYSTEM -> isSystemInDarkTheme(); ThemeMode.DARK -> true; ThemeMode.LIGHT -> false }
    val context = LocalContext.current
    val colors = if (settings.dynamicColors && Build.VERSION.SDK_INT >= 31) {
        // Keep the ocean action colors and adapt only the supporting surfaces.
        val device = if(dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        val brand = if(dark) HydroDarkColors else HydroLightColors
        brand.copy(outline=device.outline, outlineVariant=device.outlineVariant)
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
        displayLarge = TextStyle(fontWeight = FontWeight.Light, fontSize = 48.sp, lineHeight = 56.sp, letterSpacing = (-2).sp),
        headlineLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 32.sp, lineHeight = 40.sp, letterSpacing = (-0.8).sp),
        headlineMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 26.sp, lineHeight = 34.sp),
        titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp)
    ), shapes = Shapes(extraSmall = RoundedCornerShape(16.dp), small = RoundedCornerShape(20.dp), medium = RoundedCornerShape(24.dp), large = RoundedCornerShape(28.dp), extraLarge = RoundedCornerShape(36.dp)), content = content)
}
