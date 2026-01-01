package xyz.sattar.javid.marketmessage.ui.message_creation.selectContacts

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.sattar.javid.marketmessage.domain.model.DeviceContact
import xyz.sattar.javid.marketmessage.ui.components.AppButton
import xyz.sattar.javid.marketmessage.ui.components.AppToolbar
import xyz.sattar.javid.marketmessage.ui.theme.MarketMessageTheme
import xyz.sattar.javid.marketmessage.utils.collectWithLifecycleAware

@Composable
fun SelectContactsScreen(
    viewModel: SelectContactsViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.sendIntent(SelectContactsIntent.LoadContacts)
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    viewModel.events.collectWithLifecycleAware { event ->
        when (event) {
            is SelectContactsEvent.NavigateToEditContacts -> onNext()
            is SelectContactsEvent.NavigateBack -> onBack()
        }
    }

    SelectContactsContent(
        uiState = uiState,
        onSearchQueryChange = { viewModel.sendIntent(SelectContactsIntent.SearchContacts(it)) },
        onSortToggle = { viewModel.sendIntent(SelectContactsIntent.ToggleSort(!uiState.isSortedByName)) },
        onContactToggle = { contact -> viewModel.sendIntent(SelectContactsIntent.ToggleContactSelection(contact)) },
        onNext = { viewModel.sendIntent(SelectContactsIntent.GoToEditContacts) },
        onBackClick = onBack
    )
}

@Composable
fun SelectContactsContent(
    uiState: SelectContactsState,
    onSearchQueryChange: (String) -> Unit,
    onSortToggle: () -> Unit,
    onContactToggle: (DeviceContact) -> Unit,
    onNext: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppToolbar(title = "انتخاب مخاطبین", onBackClick = onBackClick)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Search and Sort Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("جستجو...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onSortToggle) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Sort"
                    )
                }
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (!uiState.permissionGranted) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("دسترسی به مخاطبین داده نشده است.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    items(uiState.filteredContacts) { contact ->
                        ContactItem(
                            contact = contact,
                            isSelected = uiState.selectedContacts.contains(contact),
                            onToggle = { onContactToggle(contact) }
                        )
                    }
                }
            }

            AppButton(
                text = "مرحله بعد",
                onClick = onNext,
                modifier = Modifier.padding(top = 16.dp),
                enabled = uiState.selectedContacts.isNotEmpty()
            )
        }
    }
}

@Composable
fun ContactItem(
    contact: DeviceContact,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggle() }
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = contact.name)
            Text(text = contact.phoneNumber, style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectContactsContentPreview() {
    MarketMessageTheme {
        SelectContactsContent(
            uiState = SelectContactsState(
                filteredContacts = listOf(
                    DeviceContact("1", "علی رضایی", "09123456789"),
                    DeviceContact("2", "مریم حسینی", "09351234567")
                ),
                permissionGranted = true
            ),
            onSearchQueryChange = {},
            onSortToggle = {},
            onContactToggle = {},
            onNext = {},
            onBackClick = {}
        )
    }
}
