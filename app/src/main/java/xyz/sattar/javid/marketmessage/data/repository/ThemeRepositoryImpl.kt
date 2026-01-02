package xyz.sattar.javid.marketmessage.data.repository

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import xyz.sattar.javid.marketmessage.domain.model.AppThemeMode
import xyz.sattar.javid.marketmessage.domain.repository.ThemeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ThemeRepository {

    private val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    private val _themeMode = MutableStateFlow(loadThemeMode())
    override val themeMode: Flow<AppThemeMode> = _themeMode.asStateFlow()

    override suspend fun setThemeMode(mode: AppThemeMode) {
        prefs.edit {
            putString("theme_mode", mode.name)
        }
        _themeMode.value = mode
    }

    private fun loadThemeMode(): AppThemeMode {
        val modeName = prefs.getString("theme_mode", AppThemeMode.SYSTEM.name)
        return try {
            AppThemeMode.valueOf(modeName ?: AppThemeMode.SYSTEM.name)
        } catch (e: Exception) {
            AppThemeMode.SYSTEM
        }
    }
}
