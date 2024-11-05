package blog.tsalikis.marketplace.marketplace.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import blog.tsalikis.marketplace.marketplace.datasource.TickerRepository
import blog.tsalikis.marketplace.marketplace.domain.BitfinexTicker
import blog.tsalikis.marketplace.marketplace.domain.ContentError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketPlaceViewModel @Inject constructor(private val repository: TickerRepository) :
    ViewModel() {

    val symbols =
        "tBTCUSD,tETHUSD,tCHSB:USD,tLTCUSD,tXRPUSD,tDSHUSD,tRRTUSD,tEOSUSD,tSANUSD,tDATUSD,tSNTUSD,tDOGE:USD,tLUNA:USD,tMATIC:USD,tNEXO:USD,tOCEAN:USD,tBEST:USD,tAAVE:USD,tPLUUSD,tFILUSD"

    private val _state = MutableStateFlow<MarketPlaceState>(MarketPlaceState.Loading)
    val state: StateFlow<MarketPlaceState>
        get() = _state

    private var pollingJob: Job? = null
    private var query = ""
    private var cachedTickerResults: List<BitfinexTicker> = emptyList()

    fun startPolling() {
        pollingJob = viewModelScope.launch {
            while (isActive) {
                when (val tickers = repository.getTickers(symbols)) {
                    is ContentError.Error -> {
                        _state.update { MarketPlaceState.Error }
                    }

                    is ContentError.Success -> {
                        cachedTickerResults = tickers.result
                        _state.update {
                            filterResults(
                                query,
                                cachedTickerResults.toPersistentList()
                            )
                        }
                    }
                }
                delay(5000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }

    fun stopPolling() {
        pollingJob?.cancel()
    }

    private fun filterResults(
        query: String,
        tickers: List<BitfinexTicker>
    ): MarketPlaceState.Success {
        val filtered = if (query.isNotBlank() || query.isNotEmpty()) {
            tickers.filter { it.symbolFrom.contains(query, ignoreCase = true) }
        } else {
            tickers
        }.toPersistentList()
        return MarketPlaceState.Success(
            query = query,
            values = filtered
        )
    }

    fun filterFromSearched(text: String) {
        viewModelScope.launch {
            query = text
            _state.update { currentState ->
                when (currentState) {
                    is MarketPlaceState.Success -> filterResults(query, cachedTickerResults)
                    else -> {
                        currentState
                    }
                }
            }
        }
    }
}

sealed class MarketPlaceState {
    data object Loading : MarketPlaceState()
    data class Success(val values: PersistentList<BitfinexTicker>, val query: String) : MarketPlaceState()
    data object Error : MarketPlaceState()
}