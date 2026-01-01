package xyz.sattar.javid.marketmessage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import dagger.hilt.android.AndroidEntryPoint
import xyz.sattar.javid.marketmessage.ui.navigation.MarketMessageApp
import xyz.sattar.javid.marketmessage.ui.theme.MarketMessageTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarketMessageTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    MarketMessageApp()
                }
            }
        }
    }
}

