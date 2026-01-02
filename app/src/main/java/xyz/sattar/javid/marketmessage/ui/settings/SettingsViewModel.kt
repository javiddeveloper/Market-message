package xyz.sattar.javid.marketmessage.ui.settings

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import xyz.sattar.javid.marketmessage.domain.repository.ThemeRepository
import xyz.sattar.javid.marketmessage.ui.components.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
) : BaseViewModel<SettingsState, SettingsState.PartialState, SettingsEvent, SettingsIntent>(
    initialState = SettingsState()
) {

    override fun handleIntent(intent: SettingsIntent): Flow<SettingsState.PartialState> = flow {
        when (intent) {
            is SettingsIntent.LoadSettings -> {
                val currentTheme = themeRepository.themeMode.first()
                emit(SettingsState.PartialState.ThemeChanged(currentTheme))
            }

            is SettingsIntent.ChangeTheme -> {
                themeRepository.setThemeMode(intent.themeMode)
                emit(SettingsState.PartialState.ThemeChanged(intent.themeMode))
            }
        }
    }

    override fun reduceState(
        currentState: SettingsState,
        partialState: SettingsState.PartialState
    ): SettingsState {
        return when (partialState) {
            is SettingsState.PartialState.ThemeChanged -> currentState.copy(themeMode = partialState.themeMode)
            is SettingsState.PartialState.Error -> currentState.copy(errorMessage = partialState.message)
        }
    }

    override fun createErrorState(message: String) = SettingsState.PartialState.Error(message)
}
