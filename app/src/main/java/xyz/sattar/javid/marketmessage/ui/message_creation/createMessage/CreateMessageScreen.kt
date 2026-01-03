package xyz.sattar.javid.marketmessage.ui.message_creation.createMessage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.sattar.javid.marketmessage.domain.model.ReadyMessage
import xyz.sattar.javid.marketmessage.ui.components.AppToolbar
import xyz.sattar.javid.marketmessage.ui.home.EmptyStateText
import xyz.sattar.javid.marketmessage.ui.theme.MarketMessageTheme
import xyz.sattar.javid.marketmessage.utils.collectWithLifecycleAware

@Composable
fun CreateMessageScreen(
    viewModel: CreateMessageViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    HandleEvents(viewModel, onNext, onBack)

    CreateMessageContent(
        uiState = uiState,
        onAddReadyMessage = { content ->
            viewModel.sendIntent(CreateMessageIntent.AddReadyMessage(content))
        },
        onDeleteReadyMessage = { id ->
            viewModel.sendIntent(CreateMessageIntent.DeleteReadyMessage(id))
        },
        onMessageClick = { message ->
            viewModel.sendIntent(CreateMessageIntent.SelectMessage(message.id, message.content))
        },
        onBackClick = onBack
    )
}

@Composable
private fun HandleEvents(
    viewModel: CreateMessageViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    viewModel.events.collectWithLifecycleAware { event ->
        when (event) {
            is CreateMessageEvent.NavigateToSelectContacts -> onNext()
            is CreateMessageEvent.NavigateBack -> onBack()
        }
    }
}

@Composable
fun CreateMessageContent(
    uiState: CreateMessageState,
    onAddReadyMessage: (String) -> Unit,
    onDeleteReadyMessage: (Long) -> Unit,
    onMessageClick: (ReadyMessage) -> Unit,
    onBackClick: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppToolbar(title = "پیام‌های آماده", onBackClick = onBackClick)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "افزودن پیام")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.readyMessages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyStateText("هنوز پیام آماده‌ای ندارید. با دکمه + پایین صفحه پیام آماده جدید اضافه کنید.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.readyMessages) { message ->
                        ReadyMessageItem(
                            readyMessage = message,
                            onMessageClick = onMessageClick,
                            onDeleteClick = onDeleteReadyMessage
                        )
                    }
                }
            }

            if (showBottomSheet) {
                AddReadyMessageBottomSheet(
                    onDismiss = { showBottomSheet = false },
                    onSave = onAddReadyMessage
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateMessageContentPreview() {
    MarketMessageTheme {
        CreateMessageContent(
            uiState = CreateMessageState(
                readyMessages = listOf(
                    ReadyMessage(1L, "پیام آماده ۱"),
                    ReadyMessage(2L, "پیام آماده ۲")
                )
            ),
            onAddReadyMessage = {},
            onDeleteReadyMessage = {},
            onMessageClick = {},
            onBackClick = {}
        )
    }
}
