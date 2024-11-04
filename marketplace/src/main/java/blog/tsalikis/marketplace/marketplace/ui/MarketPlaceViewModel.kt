package blog.tsalikis.marketplace.marketplace.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarketPlaceViewModel @Inject constructor() : ViewModel() {

    val symbols = "ALL"

}