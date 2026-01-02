package xyz.sattar.javid.marketmessage.domain.model

data class RecentMessageStat(
    val messageType: String,
    val content: String,
    val count: Int,
    val lastSent: Long
)
