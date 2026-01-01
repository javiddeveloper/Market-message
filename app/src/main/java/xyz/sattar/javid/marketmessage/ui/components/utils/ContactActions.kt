package xyz.sattar.javid.marketmessage.ui.components.utils

fun formatPhoneNumberForAction(phone: String): String {
    // 1. Convert Persian/Arabic digits to English digits
    var formatted = phone
        .replace("۰", "0")
        .replace("۱", "1")
        .replace("۲", "2")
        .replace("۳", "3")
        .replace("۴", "4")
        .replace("۵", "5")
        .replace("۶", "6")
        .replace("۷", "7")
        .replace("۸", "8")
        .replace("۹", "9")

    // 2. If starts with 09, replace with +989
    if (formatted.startsWith("09")) {
        formatted = "+98" + formatted.substring(1)
    }

    return formatted
}
