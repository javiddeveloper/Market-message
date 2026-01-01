package xyz.sattar.javid.marketmessage.ui.message_creation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.sattar.javid.marketmessage.ui.components.AppButton
import xyz.sattar.javid.marketmessage.ui.components.AppTextField
import xyz.sattar.javid.marketmessage.ui.components.AppToolbar
import xyz.sattar.javid.marketmessage.ui.theme.MarketMessageTheme

@Composable
fun CreateMessageScreen(
    viewModel: MessageCreationViewModel,
    onNext: () -> Unit
) {
    val messageBody by viewModel.messageBody.collectAsState()

    CreateMessageContent(
        messageBody = messageBody,
        onMessageBodyChange = viewModel::updateMessageBody,
        onNext = onNext
    )
}

@Composable
fun CreateMessageContent(
    messageBody: String,
    onMessageBodyChange: (String) -> Unit,
    onNext: () -> Unit
) {
    Scaffold(
        topBar = {
            AppToolbar(title = "نوشتن پیام")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppTextField(
                value = messageBody,
                onValueChange = onMessageBodyChange,
                label = "متن پیام",
                modifier = Modifier.padding(top = 16.dp),
                maxLines = 5
            )

            AppButton(
                text = "مرحله بعد",
                onClick = onNext,
                modifier = Modifier.padding(top = 24.dp),
                enabled = messageBody.isNotEmpty()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateMessageContentPreview() {
    MarketMessageTheme {
        CreateMessageContent(
            messageBody = "این یک پیام تستی است",
            onMessageBodyChange = {},
            onNext = {}
        )
    }
}
