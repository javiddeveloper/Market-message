package xyz.sattar.javid.marketmessage.ui.settings

import xyz.sattar.javid.marketmessage.domain.model.AppThemeMode

sealed interface SettingsIntent {
    data object LoadSettings : SettingsIntent
    data class ChangeTheme(val themeMode: AppThemeMode) : SettingsIntent
}

data class SettingsState(
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val appVersion: String = "1.0.0",
    val errorMessage: String? = null
) {
    sealed interface PartialState {
        data class ThemeChanged(val themeMode: AppThemeMode) : PartialState
        data class Error(val message: String) : PartialState
    }
}

sealed interface SettingsEvent {
    // Add navigation events if needed
}

