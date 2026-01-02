package xyz.sattar.javid.marketmessage.ui.components.utils

fun formatPhoneNumberForAction(phone: String, showUI: Boolean = false): String {
    var formatted = phone.replace(" ", "")
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


    if (showUI) {
        if (formatted.startsWith("+989")) {
           formatted = formatted.replace("+989","09")
        }
    } else {
        if (formatted.startsWith("09")) {
            formatted = "+98" + formatted.substring(1)
        }
    }

    return formatted
}
