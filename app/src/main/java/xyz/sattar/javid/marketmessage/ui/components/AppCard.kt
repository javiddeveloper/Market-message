package xyz.sattar.javid.marketmessage.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class AppCardType {
    PRIMARY,
    SECONDARY,
    SURFACE,
    INFO,
    HIGHLIGHT
}

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    type: AppCardType = AppCardType.SURFACE,
    content: @Composable ColumnScope.() -> Unit
) {
    val containerColor = when (type) {
        AppCardType.PRIMARY -> MaterialTheme.colorScheme.primaryContainer
        AppCardType.SECONDARY -> MaterialTheme.colorScheme.secondaryContainer
        AppCardType.SURFACE -> MaterialTheme.colorScheme.surface
        AppCardType.INFO -> MaterialTheme.colorScheme.tertiaryContainer
        AppCardType.HIGHLIGHT -> MaterialTheme.colorScheme.errorContainer
    }

    val contentColor = when (type) {
        AppCardType.PRIMARY -> MaterialTheme.colorScheme.onPrimaryContainer
        AppCardType.SECONDARY -> MaterialTheme.colorScheme.onSecondaryContainer
        AppCardType.SURFACE -> MaterialTheme.colorScheme.onSurface
        AppCardType.INFO -> MaterialTheme.colorScheme.onTertiaryContainer
        AppCardType.HIGHLIGHT -> MaterialTheme.colorScheme.onErrorContainer
    }

    // Border color harmonized with the container color but slightly distinct if needed, 
    // or just the same family. Let's use outline variant or similar for subtle border.
    // User requested "harmony with the card color".
    val borderColor = when (type) {
        AppCardType.PRIMARY -> MaterialTheme.colorScheme.primary
        AppCardType.SECONDARY -> MaterialTheme.colorScheme.secondary
        AppCardType.SURFACE -> MaterialTheme.colorScheme.outline
        AppCardType.INFO -> MaterialTheme.colorScheme.tertiary
        AppCardType.HIGHLIGHT -> MaterialTheme.colorScheme.error
    }.copy(alpha = 0.5f)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(1.dp, borderColor),
        content = {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    )
}
