package xyz.sattar.javid.marketmessage.data.local.model

import androidx.room.ColumnInfo

data class RecentMessageCount(
    @ColumnInfo(name = "messageType") val messageType: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "count") val count: Int,
    @ColumnInfo(name = "lastSent") val lastSent: Long
)
