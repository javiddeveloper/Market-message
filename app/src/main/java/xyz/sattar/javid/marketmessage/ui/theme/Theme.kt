package xyz.sattar.javid.marketmessage.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = White,
    primaryContainer = LightGreen,
    onPrimaryContainer = DarkGreen,
    secondary = SecondaryTeal,
    onSecondary = White,
    secondaryContainer = SecondaryDark,
    background = Gray100,
    onBackground = Gray900,
    surface = White,
    onSurface = Gray900,
    error = ErrorRed,
    onError = White
)

// For now, we use the same palette for dark mode or a slightly tweaked one if needed.
// Usually, dark mode needs desaturated colors. Let's define a basic dark one.
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen, // Keep brand color or desaturate to 0xFF4CAF50
    onPrimary = White,
    primaryContainer = DarkGreen,
    onPrimaryContainer = LightGreen,
    background = Black,
    onBackground = White,
    surface = Gray900,
    onSurface = White
)

@Composable
fun MarketMessageTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Disable dynamic color to enforce brand theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
