package xyz.sattar.javid.marketmessage.domain.model

enum class MessageType(val value: String, val label: String) {
    SMS("SMS", "پیامک"),
    EMAIL("Email", "ایمیل"),
    PUSH("Push", "نوتیفیکیشن");

    companion object {
        fun fromValue(value: String): MessageType {
            return entries.find { it.value.equals(value, ignoreCase = true) } ?: SMS
        }
    }
}
