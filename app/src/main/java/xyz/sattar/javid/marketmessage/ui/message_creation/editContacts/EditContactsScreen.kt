package xyz.sattar.javid.marketmessage.ui.message_creation.editContacts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import xyz.sattar.javid.marketmessage.domain.model.DraftContact
import xyz.sattar.javid.marketmessage.ui.components.AppButton
import xyz.sattar.javid.marketmessage.ui.components.AppCard
import xyz.sattar.javid.marketmessage.ui.components.AppCardType
import xyz.sattar.javid.marketmessage.ui.components.AppToolbar
import xyz.sattar.javid.marketmessage.ui.components.utils.formatPhoneNumberForAction
import xyz.sattar.javid.marketmessage.ui.theme.MarketMessageTheme
import xyz.sattar.javid.marketmessage.utils.collectWithLifecycleAware

@Composable
fun EditContactsScreen(
    viewModel: EditContactsViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    HandleEvents(viewModel, onNext, onBack)

    EditContactsContent(
        selectedContacts = uiState.selectedContacts,
        onContactRemove = { contact -> viewModel.sendIntent(EditContactsIntent.RemoveContact(contact)) },
        onContactUpdate = { contact, newName -> viewModel.sendIntent(EditContactsIntent.UpdateContactName(contact, newName)) },
        onNext = { viewModel.sendIntent(EditContactsIntent.GoToSend) },
        onBackClick = { viewModel.sendIntent(EditContactsIntent.GoBack) }
    )
}

@Composable
private fun HandleEvents(
    viewModel: EditContactsViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    viewModel.events.collectWithLifecycleAware { event ->
        when (event) {
            is EditContactsEvent.NavigateToSend -> onNext()
            is EditContactsEvent.NavigateBack -> onBack()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactsContent(
    selectedContacts: List<DraftContact>,
    onContactRemove: (DraftContact) -> Unit,
    onContactUpdate: (DraftContact, String) -> Unit,
    onNext: () -> Unit,
    onBackClick: () -> Unit
) {
    var editingContact by remember { mutableStateOf<DraftContact?>(null) }
    var editedName by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()

    if (editingContact != null) {
        ModalBottomSheet(
            onDismissRequest = { editingContact = null },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "ویرایش نام مخاطب", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("نام") },
                    modifier = Modifier.fillMaxWidth()
                )
                AppButton(
                    text = "ذخیره",
                    onClick = {
                        editingContact?.let { contact ->
                            onContactUpdate(contact, editedName)
                        }
                        editingContact = null
                    }
                )
            }
        }
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "ویرایش مخاطبین",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(selectedContacts) { contact ->
                    AppCard(type = AppCardType.SURFACE) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = contact.name)
                                Text(
                                    text = formatPhoneNumberForAction(contact.phoneNumber,true),
                                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                                )
                            }
                            Row {
                                IconButton(onClick = {
                                    editingContact = contact
                                    editedName = contact.name
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "ویرایش")
                                }
                                IconButton(onClick = { onContactRemove(contact) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "حذف")
                                }
                            }
                        }
                    }
                }
            }

            AppButton(
                text = "مرحله بعد (ارسال نهایی)",
                onClick = onNext,
                modifier = Modifier.padding(top = 16.dp),
                enabled = selectedContacts.isNotEmpty()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditContactsContentPreview() {
    MarketMessageTheme {
        EditContactsContent(
            selectedContacts = listOf(
                DraftContact(name = "علی", phoneNumber = "09123456789"),
                DraftContact(name = "رضا", phoneNumber = "09351234567"),
                DraftContact(name = "مریم", phoneNumber = "09119876543")
            ),
            onContactRemove = {},
            onContactUpdate = { _, _ -> },
            onNext = {},
            onBackClick = {}
        )
    }
}
