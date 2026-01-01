package xyz.sattar.javid.marketmessage.ui.message_creation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.sattar.javid.marketmessage.ui.components.AppButton
import xyz.sattar.javid.marketmessage.ui.components.AppToolbar
import xyz.sattar.javid.marketmessage.ui.theme.MarketMessageTheme

@Composable
fun SendMessageScreen(
    viewModel: MessageCreationViewModel,
    onFinish: () -> Unit
) {
    val messageBody by viewModel.messageBody.collectAsState()
    val selectedContacts by viewModel.selectedContacts.collectAsState()

    SendMessageContent(
        messageBody = messageBody,
        contactCount = selectedContacts.size,
        onSend = {
            viewModel.clearData()
            onFinish()
        }
    )
}

@Composable
fun SendMessageContent(
    messageBody: String,
    contactCount: Int,
    onSend: () -> Unit
) {
    Scaffold(
        topBar = {
            AppToolbar(title = "ارسال نهایی")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "خلاصه پیام",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("پیام: $messageBody", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("تعداد مخاطبین: $contactCount", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                text = "ارسال پیام",
                onClick = onSend
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SendMessageContentPreview() {
    MarketMessageTheme {
        SendMessageContent(
            messageBody = "سلام، این یک پیام آزمایشی است.",
            contactCount = 5,
            onSend = {}
        )
    }
}
