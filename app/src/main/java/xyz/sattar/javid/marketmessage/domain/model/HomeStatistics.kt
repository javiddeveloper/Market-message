package xyz.sattar.javid.marketmessage.domain.model

data class MessageTypeStats(
    val messageType: String,
    val count: Int
)

data class CustomerStats(
    val customer: Customer,
    val messageCount: Int
)

data class HomeStatistics(
    val mostFrequentMessageType: MessageTypeStats?,
    val topFrequentContacts: List<CustomerStats>,
    val recentMessages: List<RecentMessageStat>
)
