package xyz.sattar.javid.marketmessage.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import xyz.sattar.javid.marketmessage.domain.model.AppThemeMode
import xyz.sattar.javid.marketmessage.domain.repository.ThemeRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    themeRepository: ThemeRepository
) : ViewModel() {
    val themeMode = themeRepository.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppThemeMode.SYSTEM
        )
}
