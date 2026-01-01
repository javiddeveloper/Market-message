package xyz.sattar.javid.marketmessage.ui.message_creation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
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
fun SelectContactsScreen(
    viewModel: MessageCreationViewModel,
    onNext: () -> Unit
) {
    val selectedContacts by viewModel.selectedContacts.collectAsState()
    val allContacts = listOf("علی", "حسن", "رضا", "محمد", "مریم", "زهرا") // Mock data

    SelectContactsContent(
        selectedContacts = selectedContacts,
        allContacts = allContacts,
        onContactToggle = { contact -> viewModel.toggleContactSelection(contact) },
        onNext = onNext
    )
}

@Composable
fun SelectContactsContent(
    selectedContacts: List<String>,
    allContacts: List<String>,
    onContactToggle: (String) -> Unit,
    onNext: () -> Unit
) {
    Scaffold(
        topBar = {
            AppToolbar(title = "انتخاب مخاطبین")
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
                items(allContacts) { contact ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onContactToggle(contact) }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedContacts.contains(contact),
                            onCheckedChange = { onContactToggle(contact) }
                        )
                        Text(text = contact, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

            AppButton(
                text = "مرحله بعد",
                onClick = onNext,
                modifier = Modifier.padding(top = 16.dp),
                enabled = selectedContacts.isNotEmpty()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectContactsContentPreview() {
    MarketMessageTheme {
        SelectContactsContent(
            selectedContacts = listOf("علی", "رضا"),
            allContacts = listOf("علی", "حسن", "رضا", "محمد"),
            onContactToggle = {},
            onNext = {}
        )
    }
}
