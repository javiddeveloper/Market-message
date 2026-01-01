package xyz.sattar.javid.marketmessage.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.sattar.javid.marketmessage.ui.components.AppButton
import xyz.sattar.javid.marketmessage.ui.components.AppToolbar
import xyz.sattar.javid.marketmessage.ui.theme.MarketMessageTheme

@Composable
fun HomeScreen(
    onCreateMessageClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppToolbar(title = "خانه")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                AppButton(
                    text = "ایجاد پیام جدید",
                    onClick = onCreateMessageClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MarketMessageTheme {
        HomeScreen(onCreateMessageClick = {})
    }
}
