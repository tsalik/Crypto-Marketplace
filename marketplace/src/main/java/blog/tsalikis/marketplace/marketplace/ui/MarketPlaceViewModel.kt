package blog.tsalikis.marketplace.marketplace.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import blog.tsalikis.marketplace.marketplace.datasource.TickerRepository
import blog.tsalikis.marketplace.marketplace.domain.BitfinexTicker
import blog.tsalikis.marketplace.marketplace.domain.ContentError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketPlaceViewModel @Inject constructor(private val repository: TickerRepository) : ViewModel() {

    val symbols = "tBTCUSD,tETHUSD,tCHSB:USD,tLTCUSD,tXRPUSD,tDSHUSD,tRRTUSD,t\n" +
            "EOSUSD,tSANUSD,tDATUSD,tSNTUSD,tDOGE:USD,tLUNA:USD,tMATIC:USD,tNEXO\n" +
            ":USD,tOCEAN:USD,tBEST :USD,tAAVE:USD,tPLUUSD,tFILUSD"

    private val _state = MutableStateFlow<MarketPlaceState>(MarketPlaceState.Loading)
    val state: StateFlow<MarketPlaceState>
        get() = _state

    init {
        viewModelScope.launch {
            _state.update { MarketPlaceState.Loading }
            when (val tickers = repository.getTickers(symbols)) {
                is ContentError.Error -> {
                    _state.update { MarketPlaceState.Error }
                }
                is ContentError.Success -> {
                    _state.update { MarketPlaceState.Success(tickers.result) }
                }
            }
        }
    }

}

sealed class MarketPlaceState {
    data object Loading: MarketPlaceState()
    data class Success(val values: List<BitfinexTicker>): MarketPlaceState()
    data object Error: MarketPlaceState()
}