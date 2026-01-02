package xyz.sattar.javid.marketmessage.ui.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.sattar.javid.marketmessage.domain.usecase.GetHomeStatsUseCase
import xyz.sattar.javid.marketmessage.ui.components.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeStatsUseCase: GetHomeStatsUseCase
) : BaseViewModel<HomeState, HomeViewModel.PartialState, HomeEvent, HomeIntent>(
    initialState = HomeState()
) {

    sealed interface PartialState {
        data class StatisticsLoaded(val statistics: xyz.sattar.javid.marketmessage.domain.model.HomeStatistics) :
            PartialState

        data object Loading : PartialState
        data class Error(val message: String) : PartialState
    }

    init {
        sendIntent(HomeIntent.LoadStatistics)
    }

    override fun reduceState(currentState: HomeState, partialState: PartialState): HomeState {
        return when (partialState) {
            is PartialState.Loading -> currentState.copy(isLoading = true, error = null)
            is PartialState.StatisticsLoaded -> currentState.copy(
                isLoading = false,
                statistics = partialState.statistics,
                error = null
            )

            is PartialState.Error -> currentState.copy(
                isLoading = false,
                error = partialState.message
            )
        }
    }

    override fun createErrorState(message: String) =
        PartialState.Error(message)


    override fun handleIntent(intent: HomeIntent): Flow<PartialState> {
        return when (intent) {
            is HomeIntent.LoadStatistics -> flow {
                emit(PartialState.Loading)
                try {
                    getHomeStatsUseCase().collect { stats ->
                        emit(PartialState.StatisticsLoaded(stats))
                    }
                } catch (e: Exception) {
                    emit(PartialState.Error(e.message ?: "Unknown error"))
                }
            }

            is HomeIntent.CreateNewMessage -> flow {
                sendEvent(HomeEvent.NavigateToCreateMessage)
            }
        }
    }
}
