package com.example.calculator.ui.theme

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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.calculator.LocalTheme
import com.example.calculator.ThemeViewModel

private val DarkColorScheme = darkColorScheme(
    primary = Black200,
    secondary = Black500,
    background = Black500,
)

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    secondary = DarkWhite,
    background = Black200,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun CalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val view = LocalView.current
    val viewModel = ThemeViewModel(initialDarkMode = darkTheme)
    val darkModeEnabled by viewModel.darkMode.collectAsState()
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkModeEnabled) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkModeEnabled -> DarkColorScheme
        else -> LightColorScheme
    }
    if(!view.isInEditMode){
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    CompositionLocalProvider(value = LocalTheme provides viewModel) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )

    }
}