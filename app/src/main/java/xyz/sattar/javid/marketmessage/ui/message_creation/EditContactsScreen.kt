package xyz.sattar.javid.marketmessage.ui.message_creation

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun EditContactsScreen(
    viewModel: MessageCreationViewModel,
    onNext: () -> Unit
) {
    val selectedContacts by viewModel.selectedContacts.collectAsState()

    EditContactsContent(
        selectedContacts = selectedContacts,
        onContactToggle = { contact -> viewModel.toggleContactSelection(contact) },
        onNext = onNext
    )
}

@Composable
fun EditContactsContent(
    selectedContacts: List<String>,
    onContactToggle: (String) -> Unit,
    onNext: () -> Unit
) {
    Scaffold(
        topBar = {
            AppToolbar(title = "ویرایش مخاطبین")
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
            ) {
                items(selectedContacts.toList()) { contact ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = contact)
                        IconButton(onClick = { onContactToggle(contact) }) {
                            Icon(Icons.Default.Delete, contentDescription = "حذف")
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
            selectedContacts = listOf("علی", "رضا", "مریم"),
            onContactToggle = {},
            onNext = {}
        )
    }
}
