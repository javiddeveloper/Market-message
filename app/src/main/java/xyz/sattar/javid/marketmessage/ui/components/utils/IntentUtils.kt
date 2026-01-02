package xyz.sattar.javid.marketmessage.ui.components.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

object IntentUtils {
    private fun formatPhoneNumberForAction(phone: String): String {
        return if (phone.startsWith("0")) "+98${phone.substring(1)}" else phone
    }

    private fun launchUri(context: Context, uri: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openSms(context: Context, phone: String, message: String?) {
        try {
            val uri = Uri.parse("smsto:${formatPhoneNumberForAction(phone)}")
            val intent = Intent(Intent.ACTION_SENDTO, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (!message.isNullOrBlank()) intent.putExtra("sms_body", message)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openWhatsApp(context: Context, phone: String, message: String?) {
        val encoded = if (message.isNullOrBlank()) "" else "?text=" + Uri.encode(message)
        launchUri(context, "https://wa.me/${formatPhoneNumberForAction(phone)}$encoded")
    }

    fun openTelegram(context: Context, phone: String, message: String?) {
        val encoded = if (message.isNullOrBlank()) "" else "?url=&text=" + Uri.encode(message)
        launchUri(context, "https://t.me/share/url$encoded")
    }

    fun openPhoneDial(context: Context, phone: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, "tel:${formatPhoneNumberForAction(phone)}".toUri()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openUrl(context: Context, url: String) {
        launchUri(context, url)
    }

    fun openInstagram(context: Context, username: String) {
        launchUri(context, "https://instagram.com/$username")
    }

    fun openTwitter(context: Context, username: String) {
        launchUri(context, "https://twitter.com/$username")
    }
}
