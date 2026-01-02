package xyz.sattar.javid.marketmessage.domain.repository

import kotlinx.coroutines.flow.Flow
import xyz.sattar.javid.marketmessage.domain.model.AppThemeMode

interface ThemeRepository {
    val themeMode: Flow<AppThemeMode>
    suspend fun setThemeMode(mode: AppThemeMode)
}
