package blog.tsalikis.marketplace.crypto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import blog.tsalikis.marketplace.crypto.design.theme.CryptoMarketplaceTheme
import blog.tsalikis.marketplace.marketplace.ui.marketplace
import blog.tsalikis.marketplace.marketplace.ui.marketplaceDestination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoMarketplaceTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = marketplaceDestination) {
                    marketplace()
                }
            }
        }
    }
}
