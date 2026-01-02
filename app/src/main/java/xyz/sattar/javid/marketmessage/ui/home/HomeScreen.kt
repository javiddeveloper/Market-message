package xyz.sattar.javid.marketmessage.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import xyz.sattar.javid.marketmessage.domain.model.CustomerStats
import xyz.sattar.javid.marketmessage.domain.model.HomeStatistics
import xyz.sattar.javid.marketmessage.domain.model.MessageType
import xyz.sattar.javid.marketmessage.domain.model.MessageTypeStats
import xyz.sattar.javid.marketmessage.domain.model.RecentMessageStat
import xyz.sattar.javid.marketmessage.ui.components.AppButton
import xyz.sattar.javid.marketmessage.ui.components.AppCard
import xyz.sattar.javid.marketmessage.ui.components.AppCardType
import xyz.sattar.javid.marketmessage.ui.components.AppToolbar
import xyz.sattar.javid.marketmessage.ui.components.utils.formatPhoneNumberForAction
import xyz.sattar.javid.marketmessage.ui.theme.MarketMessageTheme
import xyz.sattar.javid.marketmessage.utils.collectWithLifecycleAware

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onCreateMessageClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    HandleEvents(viewModel, onCreateMessageClick)

    Scaffold(
        topBar = {
            AppToolbar(title = "خانه")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "خطای ناشناخته",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                uiState.statistics?.let { stats ->
                    HomeContent(
                        statistics = stats,
                        onCreateMessageClick = { viewModel.sendIntent(HomeIntent.CreateNewMessage) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HandleEvents(
    viewModel: HomeViewModel,
    onCreateMessageClick: () -> Unit
) {
    viewModel.events.collectWithLifecycleAware { event ->
        when (event) {
            is HomeEvent.NavigateToCreateMessage -> onCreateMessageClick()
        }
    }
}

@Composable
fun HomeContent(
    statistics: HomeStatistics,
    onCreateMessageClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Most Frequent Message Type
        item {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { -40 }) + fadeIn(animationSpec = tween(500))
            ) {
                MostFrequentTypeCard(statistics.mostFrequentMessageType)
            }
        }

        // 2. New Message Section
        item {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { -20 }) + fadeIn(animationSpec = tween(600))
            ) {
                NewMessageSection(onCreateMessageClick)
            }
        }

        // 3. Top 5 Frequent Contacts
        item {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { 20 }) + fadeIn(animationSpec = tween(700))
            ) {
                TopContactsSection(statistics.topFrequentContacts)
            }
        }

        // 4. Recent Messages
        item {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { 40 }) + fadeIn(animationSpec = tween(800))
            ) {
                RecentMessagesSection(statistics.recentMessages)
            }
        }
    }
}

@Composable
fun MostFrequentTypeCard(stats: MessageTypeStats?) {
    AppCard(type = AppCardType.PRIMARY) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "بیشترین نوع ارسال پیام",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (stats != null) {
                val translatedType = MessageType.fromValue(stats.messageType).label
                Text(
                    text = "$translatedType: ${stats.count} پیام",
                    style = MaterialTheme.typography.titleMedium,
                )
            } else {
                Text(
                    text = "هنوز پیامی ارسال نشده است",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun NewMessageSection(onClick: () -> Unit) {
    AppCard(type = AppCardType.SURFACE) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ارسال پیام جدید",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(
                text = "شروع",
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(0.5f)
            )
        }
    }
}

@Composable
fun TopContactsSection(contacts: List<CustomerStats>) {
    Column {
        Text(
            text = "مخاطبین پر تکرار",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (contacts.isEmpty()) {
            EmptyStateText("مخاطبی یافت نشد")
        } else {
            contacts.forEach { contactStats ->
                ContactStatItem(contactStats)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ContactStatItem(stats: CustomerStats) {
    AppCard(type = AppCardType.SURFACE) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stats.customer.editFullName ?: stats.customer.fullName ?: "بدون نام",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatPhoneNumberForAction( stats.customer.phoneNumber,true),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${stats.messageCount} پیام",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun RecentMessagesSection(messages: List<RecentMessageStat>) {
    Column {
        Text(
            text = "آخرین پیام‌ها",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (messages.isEmpty()) {
            EmptyStateText("پیامی یافت نشد")
        } else {
            messages.forEach { message ->
                RecentMessageItem(message)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun RecentMessageItem(message: RecentMessageStat) {
    AppCard(type = AppCardType.SURFACE) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val translatedType = MessageType.fromValue(message.messageType).label
                Text(
                    text = translatedType,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${message.count} ارسال",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
        }
    }
}

@Composable
fun EmptyStateText(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MarketMessageTheme {
        HomeContent(
            statistics = HomeStatistics(
                mostFrequentMessageType = MessageTypeStats("SMS", 150),
                topFrequentContacts = listOf(),
                recentMessages = listOf()
            ),
            onCreateMessageClick = {}
        )
    }
}
