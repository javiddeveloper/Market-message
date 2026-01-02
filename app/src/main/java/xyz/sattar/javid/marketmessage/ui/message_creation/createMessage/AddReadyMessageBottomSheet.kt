package xyz.sattar.javid.marketmessage.ui.message_creation.createMessage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import xyz.sattar.javid.marketmessage.domain.model.MessageVariable
import xyz.sattar.javid.marketmessage.ui.components.AppButton
import xyz.sattar.javid.marketmessage.ui.components.AppTextField

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddReadyMessageBottomSheet(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var messageContent by remember { mutableStateOf("") }
    
    val previewText by remember {
        derivedStateOf {
            MessageVariable.replaceVariables(messageContent) { it.fakeValue }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 24.dp) // Extra padding for safe area
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "افزودن پیام آماده",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "بستن")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Variable Chips
            Text(
                text = "متغیرهای هوشمند:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MessageVariable.entries.forEach { variable ->
                    FilterChip(
                        selected = false,
                        onClick = { messageContent += " ${variable.value} " },
                        label = { Text(variable.label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Message Input with Clear Button
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (messageContent.isNotEmpty()) {
                        TextButton(
                            onClick = { messageContent = "" },
                            content = {
                                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("پاک کردن متن", color = MaterialTheme.colorScheme.error)
                            }
                        )
                    }
                }
                
                AppTextField(
                    value = messageContent,
                    onValueChange = { messageContent = it },
                    label = "متن پیام",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp), // Approximate 3 lines height
                    maxLines = 5
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Preview Section
            if (messageContent.isNotBlank()) {
                Text(
                    text = "پیش‌نمایش:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = previewText,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            AppButton(
                text = "ذخیره",
                onClick = {
                    onSave(messageContent)
                    onDismiss()
                },
                enabled = messageContent.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
