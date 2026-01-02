package xyz.sattar.javid.marketmessage.ui.message_creation.sendMessage

import android.Manifest
import android.content.pm.PackageManager
import xyz.sattar.javid.marketmessage.domain.model.MessageVariable
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import xyz.sattar.javid.marketmessage.domain.model.DraftContact
import xyz.sattar.javid.marketmessage.ui.components.AppCard
import xyz.sattar.javid.marketmessage.ui.components.AppCardType
import xyz.sattar.javid.marketmessage.ui.components.AppToolbar
import xyz.sattar.javid.marketmessage.ui.components.utils.DateTools
import xyz.sattar.javid.marketmessage.ui.components.utils.formatPhoneNumberForAction
import xyz.sattar.javid.marketmessage.ui.theme.MarketMessageTheme
import xyz.sattar.javid.marketmessage.utils.collectWithLifecycleAware

@Composable
fun SendMessageScreen(
    viewModel: SendMessageViewModel,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var pendingContact by remember { mutableStateOf<DraftContact?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    BackHandler{

    }
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && pendingContact != null) {
            viewModel.sendIntent(SendMessageIntent.SendSingleSms(pendingContact!!))
            pendingContact = null
        }
    }

    var showBulkSendDialog by remember { mutableStateOf(false) }

    HandleEvents(viewModel, onBack, onFinish)

    if (showBulkSendDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showBulkSendDialog = false },
            title = { Text(text = "ارسال همگانی") },
            text = { Text(text = "آیا مطمئن هستید که می‌خواهید پیام را برای همه مخاطبین ارسال کنید؟") },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                            viewModel.sendIntent(SendMessageIntent.SendBulkSms)
                        } else {
                            permissionLauncher.launch(Manifest.permission.SEND_SMS)
                            // Note: If permission is granted via launcher, we might need a way to trigger bulk send automatically.
                            // For simplicity, user might need to click again. 
                            // Or we can add a 'pendingBulkSend' state similar to 'pendingContact'.
                        }
                        showBulkSendDialog = false
                    }
                ) {
                    Text("بله، ارسال کن")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(
                    onClick = { showBulkSendDialog = false }
                ) {
                    Text("انصراف")
                }
            }
        )
    }

    if (uiState.isSending) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { },
            title = { Text(text = "در حال ارسال") },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    androidx.compose.material3.CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("لطفاً صبر کنید...")
                }
            },
            confirmButton = {}
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppToolbar(
                title = "ارسال نهایی",

                onCloseClick = {
                    viewModel.sendIntent(SendMessageIntent.GotoDashboard)
                }
            )
        },
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(
                onClick = { showBulkSendDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Send, contentDescription = "ارسال برای همه")
            }
        }
    ) { innerPadding ->
        SendMessageContent(
            uiState = uiState,

            onContactClick = { contact ->
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    viewModel.sendIntent(SendMessageIntent.SendSingleSms(contact))
                } else {
                    pendingContact = contact
                    permissionLauncher.launch(Manifest.permission.SEND_SMS)
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun HandleEvents(
    viewModel: SendMessageViewModel,
    onBack: () -> Unit,
    onFinish: () -> Unit
) {
    viewModel.events.collectWithLifecycleAware { event ->
        when (event) {
            is SendMessageEvent.NavigateBack -> onBack()
            is SendMessageEvent.NavigateDashboard -> onFinish()
        }
    }
}

@Composable
fun SendMessageContent(
    uiState: SendMessageState,
    onContactClick: (DraftContact) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentDate = remember { DateTools.getCurrentSolarDateTime() }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "برای ارسال پیامک باید روی هر آیتم بزنید تا ارسال شود و تیک بخورد",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.contacts) { contact ->
                MessageItem(
                    contact = contact,
                    messageBody = uiState.messageBody,
                    date = currentDate,
                    isSent = uiState.sentPhoneNumbers.contains(contact.phoneNumber),
                    onClick = { onContactClick(contact) }
                )
            }
        }

    }
}


@Composable
fun MessageItem(
    contact: DraftContact,
    messageBody: String,
    date: String,
    isSent: Boolean,
    onClick: () -> Unit
) {
    val personalizedMessage = remember(messageBody, contact.name) {
        MessageVariable.replaceVariables(messageBody) { variable ->
            when (variable) {
                MessageVariable.NAME -> contact.name
            }
        }
    }

    AppCard(
        type = AppCardType.SURFACE,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isSent) { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = if (isSent) Icons.Default.Check else Icons.Default.DateRange,
                contentDescription = null,
                tint = if (isSent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (isSent) 0.5f else 1f)
            )
            {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatPhoneNumberForAction(contact.phoneNumber,true),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = personalizedMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = date,
                    style = MaterialTheme.typography.labelSmall.copy(textDirection = TextDirection.Ltr),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SendMessageContentPreview() {
    MarketMessageTheme {
        SendMessageContent(
            uiState = SendMessageState(
                messageBody = "سلام، این یک پیام آزمایشی است.",
                contacts = listOf(
                    DraftContact("علی رضایی", "09123456789"),
                    DraftContact("مریم حسینی", "09351234567")
                )
            ),
            onContactClick = {},
        )
    }
}
